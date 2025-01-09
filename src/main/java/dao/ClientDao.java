package dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import objects.Client;

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
    void update(Client client);

    @Delete
    void delete(Client client);
}
