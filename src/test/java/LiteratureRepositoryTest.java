import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LiteratureRepositoryTest {
    private static final LiteratureRepository literatureRepository = new LiteratureRepository();
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("nbd");
    private static  final EntityManager em = emf.createEntityManager();
    private Book b;
    private Magazine m;
    @BeforeEach
    public void prepareForTests() {
        em.getTransaction().begin();
        em.createQuery("delete from Literature").executeUpdate();
        em.getTransaction().commit();
        b = new Book("Pan Tadeusz", "Epopeja", "Mickiewicz", 2, 2);
        m = new Magazine("Swiat nauki", "2001/10", 2);
    }

    @AfterAll
    public static void close() {
        if (em != null) {
            emf.close();
        }
    }
    @Test
    public void literatureCreateTest(){
        literatureRepository.create(b);
        assertEquals(literatureRepository.getAll().getFirst(), b);
    }

    @Test
    public void literatureGettersTests() {
        literatureRepository.create(b);
        literatureRepository.create(m);
        assertEquals(literatureRepository.getBookByAuthor("Mickiewicz").getFirst(), b);
        assertEquals(literatureRepository.getByName("Swiat nauki").getFirst(), m);
        assertEquals(2, literatureRepository.getByWeight(2).size());
        assertEquals(literatureRepository.getAll().size(), 2);
    }

    @Test
    public void literatureDeleteTest() {
        literatureRepository.create(b);
        literatureRepository.create(m);
        assertEquals(literatureRepository.getAll().size(), 2);
        literatureRepository.delete(b.getId());
        assertEquals(literatureRepository.getAll().size(), 1);
    }
}
