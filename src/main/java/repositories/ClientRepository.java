package repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
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
    }


}
