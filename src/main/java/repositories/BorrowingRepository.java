package repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.schema.Drop;
import com.datastax.oss.driver.api.querybuilder.update.Update;
import exceptions.LiteratureBorrowedException;
import exceptions.WeightExceededException;
import objects.Borrowing;
import objects.Client;
import objects.Literature;
import providers.ZonedDateTimeConverter;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class BorrowingRepository extends AbstractCassandraRepository {

    private ClientRepository clientRepository = new ClientRepository();
    private LiteratureRepository literatureRepository = new LiteratureRepository();

    public BorrowingRepository() {
        initSession();
        SimpleStatement createBorrowingsByClient =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("borrowings_by_client"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("client"), DataTypes.UUID)
                        .withClusteringColumn(CqlIdentifier.fromCql("begin_date"), DataTypes.TIMESTAMP)
                        .withClusteringColumn(CqlIdentifier.fromCql("borrowing_id"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("end_date"), DataTypes.TIMESTAMP)
                        .withColumn(CqlIdentifier.fromCql("literature"), DataTypes.UUID)
                        .build();
        getSession().execute(createBorrowingsByClient);

        SimpleStatement createBorrowingsByLiterature =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("borrowings_by_literature"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("literature"), DataTypes.UUID)
                        .withClusteringColumn(CqlIdentifier.fromCql("begin_date"), DataTypes.TIMESTAMP)
                        .withClusteringColumn(CqlIdentifier.fromCql("borrowing_id"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("end_date"), DataTypes.TIMESTAMP)
                        .withColumn(CqlIdentifier.fromCql("client"), DataTypes.UUID)
                        .build();
        getSession().execute(createBorrowingsByLiterature);

    }

    public void create(Borrowing obj) {
        Client client = clientRepository.getById(obj.getClient().getClientId());
        Literature literature = literatureRepository.getById(obj.getLiterature().getLiteratureId());
        if (client.getCurrentWeight() + obj.getLiterature().calculateTotalWeight() > client.getMaxWeight()) {
            throw new WeightExceededException();
        }
        List<Borrowing> list = this.getAllBorrowingsByLiteratureId(obj.getLiterature().getLiteratureId());
        for (Borrowing borrowing : list) {
            if (borrowing.getEndDate().toInstant().toEpochMilli() == 0L) {
                throw new LiteratureBorrowedException();
            }
        }

        client.setCurrentWeight(client.getCurrentWeight() + obj.getLiterature().calculateTotalWeight());
        clientRepository.update(client);
        literature.setIsBorrowed(1);
        literatureRepository.update(literature);
//        if (this.getAllBorrowingsByLiteratureId(obj.getLiterature().getLiteratureId()).size() == 0) {
        Insert insertRentsByClient = QueryBuilder.insertInto(CqlIdentifier.fromCql("borrowings_by_client"))
                .value(CqlIdentifier.fromCql("borrowing_id"), literal(obj.getBorrowingId()))
                .value(CqlIdentifier.fromCql("begin_date"), literal(ZonedDateTimeConverter.toCassandraValue(obj.getBeginDate())))
                .value(CqlIdentifier.fromCql("end_date"), literal(ZonedDateTimeConverter.toCassandraValue(obj.getEndDate())))
                .value(CqlIdentifier.fromCql("client"), literal(obj.getClient().getClientId()))
                .value(CqlIdentifier.fromCql("literature"), literal(obj.getLiterature().getLiteratureId()));
        Insert insertRentsByLiterature = QueryBuilder.insertInto(CqlIdentifier.fromCql("borrowings_by_literature"))
                .value(CqlIdentifier.fromCql("borrowing_id"), literal(obj.getBorrowingId()))
                .value(CqlIdentifier.fromCql("begin_date"), literal(ZonedDateTimeConverter.toCassandraValue(obj.getBeginDate())))
                .value(CqlIdentifier.fromCql("end_date"), literal(ZonedDateTimeConverter.toCassandraValue(obj.getEndDate())))
                .value(CqlIdentifier.fromCql("client"), literal(obj.getClient().getClientId()))
                .value(CqlIdentifier.fromCql("literature"), literal(obj.getLiterature().getLiteratureId()));
        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(insertRentsByClient.build())
                .addStatement(insertRentsByLiterature.build())
                .build();
        getSession().execute(batchStatement);
    }

    public void endBorrowing(Borrowing obj) {
        Literature literature = literatureRepository.getById(obj.getLiterature().getLiteratureId());
        literature.setIsBorrowed(0);
        literatureRepository.update(literature);
        Client client = clientRepository.getById(obj.getClient().getClientId());
        client.subtractCurrentWeight(obj.getLiterature().calculateTotalWeight());
        clientRepository.update(client);
        update(obj);
    }

    public List<Borrowing> getAllBorrowingsByClientId(UUID clientId) {
        // Execute the query
        List<Row> rows = getSession().execute(QueryBuilder.selectFrom(CqlIdentifier.fromCql("borrowings_by_client"))
                .all()
                .where(Relation.column(CqlIdentifier.fromCql("client")).isEqualTo(literal(clientId)))
                .build()).all();

        return rows.stream()
                .map(this::mapRowToBorrowing)
                .collect(Collectors.toList());
    }

    private Borrowing mapRowToBorrowing(Row row) {
        Borrowing borrowing = new Borrowing(row.getUuid("borrowing_id"),
                ZonedDateTimeConverter.fromCassandraValue(Timestamp.from(row.getInstant("begin_date"))),
                ZonedDateTimeConverter.fromCassandraValue(Timestamp.from(row.getInstant("end_date"))),
                clientRepository.getById(row.getUuid("client")),
                literatureRepository.getById(row.getUuid("literature")));
        return borrowing;
    }

    public List<Borrowing> getAllBorrowingsByLiteratureId(UUID literatureId) {
        List<Row> rows = getSession().execute(QueryBuilder.selectFrom(CqlIdentifier.fromCql("borrowings_by_literature"))
                .all()
                .where(Relation.column(CqlIdentifier.fromCql("literature")).isEqualTo(literal(literatureId)))
                .build()).all();

        return rows.stream()
                .map(this::mapRowToBorrowing)
                .collect(Collectors.toList());
    }

    private void deleteBorrowingsByClientId(UUID clientId) {
        getSession().execute(QueryBuilder.deleteFrom(CqlIdentifier.fromCql("borrowings_by_client"))
                .where(Relation.column(CqlIdentifier.fromCql("client")).isEqualTo(literal(clientId)))
                .build());
    }

    private void deleteBorrowingsByLiteratureId(UUID literatureId) {
        getSession().execute(QueryBuilder.deleteFrom(CqlIdentifier.fromCql("borrowings_by_literature"))
                .where(Relation.column(CqlIdentifier.fromCql("literature")).isEqualTo(literal(literatureId)))
                .build());
    }

    public void delete(Borrowing obj) {
        getSession().execute(QueryBuilder.deleteFrom(CqlIdentifier.fromCql("borrowings_by_client"))
                .where(Relation.column(CqlIdentifier.fromCql("client")).isEqualTo(literal(obj.getClient().getClientId())))
                .where(Relation.column(CqlIdentifier.fromCql("begin_date")).isEqualTo(literal(ZonedDateTimeConverter.toCassandraValue(obj.getBeginDate()))))
                .where(Relation.column(CqlIdentifier.fromCql("borrowing_id")).isEqualTo(literal(obj.getBorrowingId())))
                .build());
        getSession().execute(QueryBuilder.deleteFrom(CqlIdentifier.fromCql("borrowings_by_literature"))
                .where(Relation.column(CqlIdentifier.fromCql("literature")).isEqualTo(literal(obj.getLiterature().getLiteratureId())))
                .where(Relation.column(CqlIdentifier.fromCql("begin_date")).isEqualTo(literal(ZonedDateTimeConverter.toCassandraValue(obj.getBeginDate()))))
                .where(Relation.column(CqlIdentifier.fromCql("borrowing_id")).isEqualTo(literal(obj.getBorrowingId())))
                .build());
    }

    public void emptyCollection() {
        SimpleStatement truncateTable = QueryBuilder.truncate(CqlIdentifier.fromCql("borrowings_by_client")).build();
        getSession().execute(truncateTable);
        truncateTable = QueryBuilder.truncate(CqlIdentifier.fromCql("borrowings_by_literature")).build();
        getSession().execute(truncateTable);
    }

    public void dropCollection() {
        Drop dropTableClients = SchemaBuilder.dropTable(CqlIdentifier.fromCql("borrowings_by_client")).ifExists();
        Drop dropTableLiterature = SchemaBuilder.dropTable(CqlIdentifier.fromCql("borrowings_by_literature")).ifExists();
        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(dropTableClients.build())
                .addStatement(dropTableLiterature.build())
                .build();
        getSession().execute(batchStatement);
    }

    public void update(Borrowing obj) {
        Update updateBorrowingsByClient = QueryBuilder.update(CqlIdentifier.fromCql("borrowings_by_client"))
                .setColumn(CqlIdentifier.fromCql("end_date"), literal(ZonedDateTimeConverter.toCassandraValue(obj.getEndDate())))
                .where(Relation.column(CqlIdentifier.fromCql("borrowing_id")).isEqualTo(literal(obj.getBorrowingId())))
                .where(Relation.column(CqlIdentifier.fromCql("client")).isEqualTo(literal(obj.getClient().getClientId())))
                .where(Relation.column(CqlIdentifier.fromCql("begin_date")).isEqualTo(literal(ZonedDateTimeConverter.toCassandraValue(obj.getBeginDate()))));
        Update updateBorrowingsByLiterature = QueryBuilder.update(CqlIdentifier.fromCql("borrowings_by_literature"))
                .setColumn(CqlIdentifier.fromCql("end_date"), literal(ZonedDateTimeConverter.toCassandraValue(obj.getEndDate())))
                .where(Relation.column(CqlIdentifier.fromCql("borrowing_id")).isEqualTo(literal(obj.getBorrowingId())))
                .where(Relation.column(CqlIdentifier.fromCql("literature")).isEqualTo(literal(obj.getLiterature().getLiteratureId())))
                .where(Relation.column(CqlIdentifier.fromCql("begin_date")).isEqualTo(literal(ZonedDateTimeConverter.toCassandraValue(obj.getBeginDate()))));
        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(updateBorrowingsByClient.build())
                .addStatement(updateBorrowingsByLiterature.build())
                .build();
        getSession().execute(batchStatement);
    }
}