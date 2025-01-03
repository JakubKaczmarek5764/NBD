package repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;

public class ClientRepository extends AbstractCassandraRepository {
    public ClientRepository() {
        initSession();
        SimpleStatement createClients =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("clients"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("client_id"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("first_name"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("last_name"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("personal_id"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("max_weight"), DataTypes.INT)
                        .withColumn(CqlIdentifier.fromCql("current_weight"), DataTypes.INT)
                        .build();
        getSession().execute(createClients);
    }   // jest jeszcze jakis clusteringKey

    public void dropCollection() {
        SimpleStatement dropTable = SchemaBuilder.dropTable(CqlIdentifier.fromCql("clients")).ifExists().build();
        getSession().execute(dropTable);
    }
    public void emptyCollection() {
        SimpleStatement truncateTable = QueryBuilder.truncate(CqlIdentifier.fromCql("clients")).build();
        getSession().execute(truncateTable);
    }

}
