package providers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import objects.Book;
import objects.Literature;
import objects.Magazine;

import java.util.UUID;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class LiteratureGetByIdProvider {
    private final CqlSession session;

    private EntityHelper<Book> bookEntityHelper;
    private EntityHelper<Magazine> magazineEntityHelper;

    public LiteratureGetByIdProvider(MapperContext ctx, EntityHelper<Book> BookEntityHelper, EntityHelper<Magazine> MagazineEntityHelper) {
        this.session = ctx.getSession();
        this.bookEntityHelper = BookEntityHelper;
        this.magazineEntityHelper = MagazineEntityHelper;
    }

    void create(Literature literature) {
        session.execute(
                switch (literature.getDiscriminator()) {
                    case "book" -> {
                        Book book = (Book) literature;
                        yield session.prepare(bookEntityHelper.insert().build())
                                .bind()
                                .setUuid("literature_id", book.getLiteratureId())
                                .setString("genre", book.getGenre())
                                .setString("author", book.getAuthor())
                                .setInt("tier", book.getTier())
                                .setString("discriminator", book.getDiscriminator())
                                .setString("name", book.getName())
                                .setInt("weight", book.getWeight())
                                .setInt("isBorrowed", book.getIsBorrowed());
                    }

                    case "magazine" -> {
                        Magazine magazine = (Magazine) literature;
                        yield session.prepare(magazineEntityHelper.insert().build())
                                .bind()
                                .setUuid("literature_id", magazine.getLiteratureId())
                                .setString("issue", magazine.getIssue())
                                .setString("discriminator", magazine.getDiscriminator())
                                .setString("name", magazine.getName())
                                .setInt("weight", magazine.getWeight())
                                .setInt("isBorrowed", magazine.getIsBorrowed());
                    }

                    default -> throw new IllegalArgumentException("Unknown class " + literature.getDiscriminator());
                });
    }

    Literature getById(UUID literatureId) {
        Select selectClient = QueryBuilder
                .selectFrom(CqlIdentifier.fromCql("literatures"))
                .all()
                .where(Relation.column("literature_id").isEqualTo(literal(literatureId)));
        Row row = session.execute(selectClient.build()).one();
        String discriminator = row.getString("discriminator");
        return switch (discriminator) {
            case "book" -> getBook(row);
            case "magazine" -> getMagazine(row);
            default -> throw new IllegalArgumentException("Unknown class " + discriminator);
        };
    }

    private Book getBook(Row book) {
        return new Book(
                book.getUuid("literature_id"),
                book.getString("genre"),
                book.getString("author"),
                book.getInt("tier"),
                book.getString("discriminator"),
                book.getString("name"),
                book.getInt("weight"),
                book.getInt("isBorrowed")
        );
    }

    private Magazine getMagazine(Row magazine) {
        return new Magazine(
                magazine.getUuid("literature_id"),
                magazine.getString("issue"),
                magazine.getString("discriminator"),
                magazine.getString("name"),
                magazine.getInt("weight"),
                magazine.getInt("isBorrowed")
        );
    }
}

