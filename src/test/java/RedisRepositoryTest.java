import com.mongodb.MongoWriteException;
import mappers.MongoUniqueId;
import objects.Book;
import objects.Magazine;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.LiteratureRepository;
import repositories.RedisLiteratureRepository;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RedisRepositoryTest {
    private LiteratureRepository literatureRepository = new LiteratureRepository();
    private RedisLiteratureRepository redisLiteratureRepository = new RedisLiteratureRepository(literatureRepository);
    private Book b;
    private Magazine m;

    @BeforeEach
    public void prepareForTests() {
        literatureRepository.emptyCollection();
        redisLiteratureRepository.emptyCollection();
        b = new Book(new MongoUniqueId(new ObjectId()), "Pan Tadeusz", "Epopeja", "Mickiewicz", 2, 2, 1);
        m = new Magazine(new MongoUniqueId(new ObjectId()), "Swiat nauki", "2001/10", 2, 0);
    }

    @AfterAll
    public static void close() {

    }

    @Test
    public void literatureCreateTest() {
        try {
            redisLiteratureRepository.create(b);
            assertEquals(redisLiteratureRepository.getAll().size(), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(redisLiteratureRepository.getAll().size(), 1);
    }


    public void literatureCreateCacheTest() {
        redisLiteratureRepository.createInCache(m);

    }

    @Test
    public void literatureGettersTests() {
        redisLiteratureRepository.create(b);
        literatureRepository.create(m);
        assertEquals(literatureRepository.getAll().size(), 2);
        assertEquals(redisLiteratureRepository.getAll().size(), 1);
        assertEquals(b, redisLiteratureRepository.getById(b.getLiteratureId()));
        assertEquals(b, literatureRepository.getById(b.getLiteratureId()));
        // m nie ma w redisie
        assertEquals(m, redisLiteratureRepository.getById(m.getLiteratureId()));
        // po wyszukaniu zostal dodany do redis
        assertEquals(redisLiteratureRepository.getAll().size(), 2);
    }

    @Test
    public void literatureGetByIdRedisOffTest() {
        literatureRepository.create(b);
        assertEquals(redisLiteratureRepository.getById(b.getLiteratureId()), b);
    }

    @Test
    public void literatureDeleteTest() {
        redisLiteratureRepository.create(b);
        redisLiteratureRepository.create(m);
        assertEquals(literatureRepository.getAll().size(), 2);
        assertEquals(redisLiteratureRepository.getAll().size(), 2);
        redisLiteratureRepository.delete(b);
        assertEquals(literatureRepository.getAll().size(), 1);
        assertEquals(redisLiteratureRepository.getAll().size(), 1);
    }

    @Test
    public void literatureUpdate() {
        redisLiteratureRepository.create(b);
        b.setName("Dziady");
        b.setIsBorrowed(1);
        redisLiteratureRepository.update(b);
        b.setIsBorrowed(2);
        assertThrows(MongoWriteException.class, () -> {
            redisLiteratureRepository.update(b);
        });
        assertEquals(literatureRepository.getAll().getFirst().getName(), "Dziady");
        assertEquals(redisLiteratureRepository.getAll().getFirst().getName(), "Dziady");
    }

    @Test
    public void literatureCollectionEmptyCollectionTest() {
        redisLiteratureRepository.create(b);
        redisLiteratureRepository.create(m);
        redisLiteratureRepository.emptyCollection();
        assertEquals(redisLiteratureRepository.getAll().size(), 0);
        assertEquals(literatureRepository.getAll().size(), 0);
    }

    @Test
    public void literatureClearCache() {
        redisLiteratureRepository.create(b);
        redisLiteratureRepository.create(m);
        redisLiteratureRepository.clearCache();
        assertEquals(redisLiteratureRepository.getAll().size(), 0);
    }

}
