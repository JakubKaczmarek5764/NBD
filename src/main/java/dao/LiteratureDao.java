package dao;

import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.annotations.*;
import objects.Book;
import objects.Literature;
import objects.Magazine;
import providers.LiteratureGetByIdProvider;

import java.util.UUID;

@Dao
public interface LiteratureDao {
    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = LiteratureGetByIdProvider.class, entityHelpers = {Book.class, Magazine.class})
    void create(Literature literature);

    @Update
    void update(Literature literature);  // czy jak nie jest mutable to mozna update

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = LiteratureGetByIdProvider.class, entityHelpers = {Book.class, Magazine.class})
    Literature getById(UUID literatureId);

    @Delete
    void remove(Literature literature);
}
