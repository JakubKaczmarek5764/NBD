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
import com.datastax.oss.driver.api.querybuilder.update.Update;
import exceptions.WeightExceededException;
import objects.Borrowing;
import objects.Client;
import objects.Literature;
import providers.ZonedDateTimeConverter;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class BorrowingRepository extends AbstractCassandraRepository {

    public BorrowingRepository() {
        initSession();
        SimpleStatement createBorrowingsByClient =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("borrowings_by_client"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("client"), DataTypes.UUID)
                        .withClusteringColumn(CqlIdentifier.fromCql("begin_date"), DataTypes.TIMESTAMP)
                        .withClusteringColumn(CqlIdentifier.fromCql("borrowing"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("end_date"), DataTypes.TIMESTAMP)
                        .withColumn(CqlIdentifier.fromCql("literature"), DataTypes.UUID)
                        .build();
        getSession().execute(createBorrowingsByClient);

        SimpleStatement createBorrowingsByLiterature =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("borrowings_by_literature"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("literature"), DataTypes.UUID)
                        .withClusteringColumn(CqlIdentifier.fromCql("begin_date"), DataTypes.TIMESTAMP)
                        .withClusteringColumn(CqlIdentifier.fromCql("borrowing"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("end_date"), DataTypes.TIMESTAMP)
                        .withColumn(CqlIdentifier.fromCql("client"), DataTypes.UUID)
                        .build();
        getSession().execute(createBorrowingsByLiterature);

    }
    public void create(Borrowing obj) {
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
        Update updateBorrowingsByClient = QueryBuilder.update(CqlIdentifier.fromCql("borrowings_by_client"))
                .setColumn(CqlIdentifier.fromCql("end_date"), literal(ZonedDateTimeConverter.toCassandraValue(obj.getEndDate())))
                .where(Relation.column(CqlIdentifier.fromCql("borrowing")).isEqualTo(literal(obj.getBorrowingId())))
                .where(Relation.column(CqlIdentifier.fromCql("client")).isEqualTo(literal(obj.getClient().getClientId())))
                .where(Relation.column(CqlIdentifier.fromCql("begin_date")).isEqualTo(literal(ZonedDateTimeConverter.toCassandraValue(obj.getBeginDate()))));
        Update updateBorrowingsByLiterature = QueryBuilder.update(CqlIdentifier.fromCql("borrowings_by_literature"))
                .setColumn(CqlIdentifier.fromCql("end_date"), literal(ZonedDateTimeConverter.toCassandraValue(obj.getEndDate())))
                .where(Relation.column(CqlIdentifier.fromCql("borrowing")).isEqualTo(literal(obj.getBorrowingId())))
                .where(Relation.column(CqlIdentifier.fromCql("literature")).isEqualTo(literal(obj.getLiterature().getLiteratureId())))
                .where(Relation.column(CqlIdentifier.fromCql("begin_date")).isEqualTo(literal(ZonedDateTimeConverter.toCassandraValue(obj.getBeginDate()))));
        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(updateBorrowingsByClient.build())
                .addStatement(updateBorrowingsByLiterature.build())
                .build();
        getSession().execute(batchStatement);
    }
    public List<Row> getAllBorrowingsByClientId(UUID clientId) {
        return getSession().execute(QueryBuilder.selectFrom(CqlIdentifier.fromCql("borrowings_by_client"))
                .all()
                .where(Relation.column(CqlIdentifier.fromCql("client")).isEqualTo(literal(clientId)))
                .build()).all();
    }
    public List<Row> getAllBorrowingsByLiteratureId(UUID literatureId) {
        return getSession().execute(QueryBuilder.selectFrom(CqlIdentifier.fromCql("borrowings_by_literature"))
                .all()
                .where(Relation.column(CqlIdentifier.fromCql("literature")).isEqualTo(literal(literatureId)))
                .build()).all();
    }
    public void deleteBorrowingsByClientId(UUID clientId) {
        getSession().execute(QueryBuilder.deleteFrom(CqlIdentifier.fromCql("borrowings_by_client"))
                .where(Relation.column(CqlIdentifier.fromCql("client")).isEqualTo(literal(clientId)))
                .build());
    }

    public void deleteBorrowingsByLiteratureId(UUID literatureId) {
        getSession().execute(QueryBuilder.deleteFrom(CqlIdentifier.fromCql("borrowings_by_literature"))
                .where(Relation.column(CqlIdentifier.fromCql("literature")).isEqualTo(literal(literatureId)))
                .build());
    }
}