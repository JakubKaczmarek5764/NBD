package repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import exceptions.WeightExceededException;
import objects.Borrowing;
import objects.Client;
import objects.Literature;


import java.util.ArrayList;
import java.util.List;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class BorrowingRepository extends AbstractCassandraRepository {

    public BorrowingRepository() {
        initSession();
        SimpleStatement createBorrowingsByClient =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("borrowings_by_client"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("borrowing_id"), DataTypes.UUID)
                        .withClusteringColumn(CqlIdentifier.fromCql("begin_date"), DataTypes.TIMESTAMP)
                        .withClusteringColumn(CqlIdentifier.fromCql("end_date"), DataTypes.TIMESTAMP)
                        .withColumn(CqlIdentifier.fromCql("client"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("literature"), DataTypes.UUID)
                        .build();
        getSession().execute(createBorrowingsByClient);

        SimpleStatement createBorrowingsByLiterature =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("borrowings_by_literature"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("borrowing_id"), DataTypes.UUID)
                        .withClusteringColumn(CqlIdentifier.fromCql("begin_date"), DataTypes.TIMESTAMP)
                        .withClusteringColumn(CqlIdentifier.fromCql("end_date"), DataTypes.TIMESTAMP)
                        .withColumn(CqlIdentifier.fromCql("client"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("literature"), DataTypes.UUID)
                        .build();
        getSession().execute(createBorrowingsByLiterature);

    }
    public void create(Borrowing obj) {
        Insert insertRentsByClient = QueryBuilder.insertInto(CqlIdentifier.fromCql("borrowings_by_client"))
                .value(CqlIdentifier.fromCql("borrowing_id"), literal(obj.getBorrowingId()))
                .value(CqlIdentifier.fromCql("begin_date"), literal(obj.getBeginDate()))
                .value(CqlIdentifier.fromCql("end_date"), literal(obj.getEndDate()))
                .value(CqlIdentifier.fromCql("client"), literal(obj.getClient().getClientId()))
                .value(CqlIdentifier.fromCql("literature"), literal(obj.getLiterature().getLiteratureId()));
        Insert insertRentsByLiterature = QueryBuilder.insertInto(CqlIdentifier.fromCql("borrowings_by_literature"))
                .value(CqlIdentifier.fromCql("borrowing_id"), literal(obj.getBorrowingId()))
                .value(CqlIdentifier.fromCql("begin_date"), literal(obj.getBeginDate()))
                .value(CqlIdentifier.fromCql("end_date"), literal(obj.getEndDate()))
                .value(CqlIdentifier.fromCql("client"), literal(obj.getClient().getClientId()))
                .value(CqlIdentifier.fromCql("literature"), literal(obj.getLiterature().getLiteratureId()));
    BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
            .addStatement(insertRentsByClient.build())
            .addStatement(insertRentsByLiterature.build())
            .build();
    getSession().execute(batchStatement);
    }
}