import mappers.MongoUniqueId;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static junit.framework.Assert.assertEquals;

public class LiteratureRepositoryTest {
    private ILiteratureRepository literatureRepository = new LiteratureRepository();

    private Book b;
    private Magazine m;
//
    @BeforeEach
    public void prepareForTests() {
        literatureRepository.drop();
        b = new Book(new MongoUniqueId(new ObjectId()), "Pan Tadeusz", "Epopeja", "Mickiewicz", 2, 2, 0);
        m = new Magazine(new MongoUniqueId(new ObjectId()), "Swiat nauki", "2001/10", 2, 0);
    }

    @AfterAll
    public static void close() {

    }

    @Test
    public void literatureCreateTest() {
        literatureRepository.create(b);
        assertEquals(literatureRepository.getAll().getFirst(), b);
    }
//
//    @Test
//    public void literatureGettersTests() {
//        literatureRepository.create(b);
//        literatureRepository.create(m);
//        assertEquals(literatureRepository.getBookByAuthor("Mickiewicz").getFirst(), b);
//        assertEquals(literatureRepository.getByName("Swiat nauki").getFirst(), m);
//        assertEquals(2, literatureRepository.getByWeight(2).size());
//        assertEquals(literatureRepository.getAll().size(), 2);
//    }
//
//    @Test
//    public void literatureDeleteTest() {
//        literatureRepository.create(b);
//        literatureRepository.create(m);
//        assertEquals(literatureRepository.getAll().size(), 2);
//        literatureRepository.delete(b.getId());
//        assertEquals(literatureRepository.getAll().size(), 1);
//    }
//
//    @Test
//    public void literatureUpdate() {
//        literatureRepository.create(b);
//        assertEquals(literatureRepository.getAll().getFirst().getName(), "Pan Tadeusz");
//        b.setName("Dziady");
//        literatureRepository.update(b);
//        assertEquals(literatureRepository.getAll().getFirst().getName(), "Dziady");
//    }
}
