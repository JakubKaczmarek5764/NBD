package dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import objects.Client;

import java.util.List;
import java.util.UUID;

@Dao
public interface ClientDao {
    @Insert
    void create(Client client);

    @Select
    Iterable<Client> getAll();  // nw czy okok

    @Select
    Client getById(UUID clientId);

    @Update
    void update(Client client); // czy jak nie jest mutable to mozna update

    @Delete
    void delete(Client client);
}
