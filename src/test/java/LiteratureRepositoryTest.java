import objects.Book;
import objects.Magazine;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.LiteratureRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LiteratureRepositoryTest {
    private LiteratureRepository literatureRepository = new LiteratureRepository();
    private Book b;
    private Magazine m;

    @BeforeEach
    public void prepareForTests() {
        b = new Book(UUID.randomUUID(), "Epopeja", "Adam Mickiewicz", 2, "book", "Pan Tadeusz", 2, 0);
        m = new Magazine(UUID.randomUUID(), "2002/11", "magazine", "Swiat Nauki", 8, 0);
    }

    @AfterAll
    public static void close() {

    }

    @Test
    public void literatureCreateTest() {
        literatureRepository.create(b);
        assertEquals(literatureRepository.getById(b.getLiteratureId()).getName(), "Pan Tadeusz");
        literatureRepository.create(m);
        assertEquals(literatureRepository.getById(m.getLiteratureId()).getName(), "Swiat Nauki");
    }


    @Test
    public void literatureDeleteTest() {
        literatureRepository.create(b);
        assertEquals(literatureRepository.getById(b.getLiteratureId()).getName(), "Pan Tadeusz");
        literatureRepository.delete(b);
        assertEquals(literatureRepository.getById(b.getLiteratureId()), null);
    }

    @Test
    public void literatureUpdate() {
        literatureRepository.create(b);
        b.setName("Dziady");
        b.setIsBorrowed(1);
        literatureRepository.update(b);
        assertEquals(literatureRepository.getById(b.getLiteratureId()).getIsBorrowed(), 1);
    }

    @Test
    public void literatureCollectionEmptyCollectionTest() {
        literatureRepository.create(b);
        literatureRepository.emptyCollection();
        assertEquals(literatureRepository.getById(b.getLiteratureId()), null);
    }
}
