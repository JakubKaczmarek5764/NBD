import com.mongodb.MongoWriteException;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import mappers.MongoUniqueId;
import objects.Book;
import objects.Magazine;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.LiteratureRepository;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LiteratureRepositoryTest {
    private LiteratureRepository literatureRepository = new LiteratureRepository();

    private Book b;
    private Magazine m;

    @BeforeEach
    public void prepareForTests() {
        literatureRepository.emptyCollection();
        b = new Book(new MongoUniqueId(new ObjectId()), "Pan Tadeusz", "Epopeja", "Mickiewicz", 2, 2, 1);
        m = new Magazine(new MongoUniqueId(new ObjectId()), "Swiat nauki", "2001/10", 2, 0);
    }

    @AfterAll
    public static void close() {

    }

    @Test
    public void literatureCreateTest() {
        literatureRepository.create(b);
        assertEquals(literatureRepository.getAll().size(), 1);
    }

    @Test
    public void literatureGettersTests() {
        literatureRepository.create(b);
        literatureRepository.create(m);
        assertEquals(literatureRepository.getAll().size(), 2);
    }

    @Test
    public void literatureDeleteTest() {
        literatureRepository.create(b);
        literatureRepository.create(m);
        assertEquals(literatureRepository.getAll().size(), 2);
        literatureRepository.delete(b);
        assertEquals(literatureRepository.getAll().size(), 1);
    }

    @Test
    public void literatureUpdate() {
        literatureRepository.create(b);
        b.setName("Dziady");
        b.setIsBorrowed(1);
        literatureRepository.update(b);
        Bson filter = Filters.eq("_id", b.getLiteratureId());
        Bson update = Updates.inc("isBorrowed", 1);
        assertThrows(MongoWriteException.class, () -> {
            literatureRepository.getLiteratureCollection().updateOne(filter, update);
        });
        assertEquals(literatureRepository.getAll().getFirst().getName(), "Dziady");
    }

    @Test
    public void literatureCollectionEmptyCollectionTest() {
        literatureRepository.create(b);
        literatureRepository.create(m);
        literatureRepository.emptyCollection();
        assertEquals(literatureRepository.getAll().size(), 0);
    }
}
