package dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import objects.Book;
import objects.Literature;
import objects.Magazine;
import providers.LiteratureGetByIdProvider;

import java.util.UUID;

@Dao
public interface LiteratureDao {
    @QueryProvider(providerClass = LiteratureGetByIdProvider.class, entityHelpers = {Book.class, Magazine.class})
    void create(Literature literature);

//    @QueryProvider(providerClass = LiteratureGetByIdProvider.class, entityHelpers = {Book.class, Magazine.class})
//    void update(Literature literature);  // czy jak nie jest mutable to mozna update

    @QueryProvider(providerClass = LiteratureGetByIdProvider.class, entityHelpers = {Book.class, Magazine.class})
    Literature getById(UUID literatureId);

    @Delete
    void remove(Literature literature);
}
