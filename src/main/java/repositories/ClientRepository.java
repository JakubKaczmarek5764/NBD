package repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import dao.ClientDao;
import mappers.ClientMapper;
import mappers.ClientMapperBuilder;
import objects.Client;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientRepository extends AbstractCassandraRepository {

    private ClientMapper clientMapper;
    private ClientDao clientDao;
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
        clientMapper = new ClientMapperBuilder(getSession()).build();
        clientDao = clientMapper.clientDao();
    }   // jest jeszcze jakis clusteringKey
    public void create(Client client) {
        clientDao.create(client);
    }
    public Client getById(UUID clientId) {
        return clientDao.getById(clientId);
    }
    public void update(Client client) {
        clientDao.update(client);
    }
    public void delete(Client client) {
        clientDao.delete(client);
    }
    public void dropCollection() {
        SimpleStatement dropTable = SchemaBuilder.dropTable(CqlIdentifier.fromCql("clients")).ifExists().build();
        getSession().execute(dropTable);
    }
    public void emptyCollection() {
        SimpleStatement truncateTable = QueryBuilder.truncate(CqlIdentifier.fromCql("clients")).build();
        getSession().execute(truncateTable);
    }
}
