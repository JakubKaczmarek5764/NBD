package dao;

import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.annotations.*;
import objects.Client;

import java.util.List;
import java.util.UUID;

@Dao
public interface ClientDao {
    @StatementAttributes(consistencyLevel = "QUORUM")
    @Insert
    void create(Client client);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @Select
    Client getById(UUID clientId);


    @Update
    void update(Client client); // czy jak nie jest mutable to mozna update

    @Delete
    void delete(Client client);
}
