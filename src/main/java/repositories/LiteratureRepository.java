package repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import dao.LiteratureDao;
import mappers.LiteratureMapper;
import mappers.LiteratureMapperBuilder;
import objects.Literature;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class LiteratureRepository extends AbstractCassandraRepository {
    private LiteratureMapper literatureMapper;
    private LiteratureDao literatureDao;
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
        literatureMapper = new LiteratureMapperBuilder(getSession()).build();
        literatureDao = literatureMapper.literatureDao();
    }
    public void create(Literature literature) {
        literatureDao.create(literature);
    }
    public Literature getById(UUID literatureId) {
        return literatureDao.getById(literatureId);
    }
//    public void update(Literature literature) {
//        literatureDao.update(literature);
//    }
    public void delete(Literature literature) {
        literatureDao.remove(literature);
    }
    public void emptyCollection() {
        SimpleStatement truncateTable = QueryBuilder.truncate(CqlIdentifier.fromCql("literatures")).build();
        getSession().execute(truncateTable);
    }
    public void dropCollection() {
        SimpleStatement dropTable = SchemaBuilder.dropTable(CqlIdentifier.fromCql("literatures")).ifExists().build();
        getSession().execute(dropTable);
    }

}
