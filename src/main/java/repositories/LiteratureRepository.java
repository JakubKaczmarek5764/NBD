package repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;

public class LiteratureRepository extends AbstractCassandraRepository {
    public LiteratureRepository() {
        initSession();
        SimpleStatement createLiteratures =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("literatures"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("literature_id"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("name"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("weight"), DataTypes.INT)
                        .withColumn(CqlIdentifier.fromCql("isBorrowed"), DataTypes.INT)
                        .withColumn(CqlIdentifier.fromCql("discriminator"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("genre"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("author"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("tier"), DataTypes.INT)
                        .withColumn(CqlIdentifier.fromCql("issue"), DataTypes.TEXT)
                        .build();
        getSession().execute(createLiteratures);
    }
}
