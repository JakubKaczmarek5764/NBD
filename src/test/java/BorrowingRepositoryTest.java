import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

public class BorrowingRepositoryTest {
    private static final BorrowingRepository borrowingRepository = new BorrowingRepository();
    private static final ClientRepository clientRepository = new ClientRepository();
    private static final LiteratureRepository literatureRepository = new LiteratureRepository();
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("nbd");
    private static  final EntityManager em = emf.createEntityManager();
    private Client c;
    private Client c2;
    private Literature lit1;
    private Literature lit2;
    private Borrowing bor1;
    private Borrowing bor2;
    @BeforeEach
    public void prepareForTests() {
        em.getTransaction().begin();
        em.createQuery("delete from Borrowing").executeUpdate();
        em.createQuery("delete from Literature").executeUpdate();
        em.createQuery("delete from Client").executeUpdate();
        em.getTransaction().commit();
        c = new Client("Jan", "Kowalski", "123", 10);
        lit1 = new Book("Pan Tadeusz", "Epopeja", "Adam Mickiewicz", 2, 2);
        GregorianCalendar date = GregorianCalendar.from(ZonedDateTime.now());
        bor1 = new Borrowing(date, null, c, lit1);
        lit2 = new Magazine("Swiat Nauki", "2002/11", 8);
        bor2 = new Borrowing(date, null, c, lit2);
    }

    @AfterAll
    public static void close() {
        if (em != null) {
            emf.close();
        }
    }
    @Test
    public void borrowingCreateTest() {
        clientRepository.create(c);
        assertEquals(clientRepository.getById(c.getId()).getCurrentWeight(), 0);
        literatureRepository.create(lit1);
        assertFalse(literatureRepository.getById(lit1.getId()).isBorrowed());
        borrowingRepository.create(bor1);
        assertEquals(clientRepository.getById(c.getId()).getCurrentWeight(), lit1.getTotalWeight());
        assertTrue(literatureRepository.getById(lit1.getId()).isBorrowed());
    }

    @Test
    public void businessLogicTest() {
        clientRepository.create(c);
        literatureRepository.create(lit1);
        borrowingRepository.create(bor1);
        literatureRepository.create(lit2);
        assertThrows(WeightExceededException.class,() -> {borrowingRepository.create(bor2);});
    }

    @Test
    public void endBorrowingTest() {
        clientRepository.create(c);
        literatureRepository.create(lit1);
        borrowingRepository.create(bor1);
        borrowingRepository.endBorrowing(bor1);
        assertFalse(literatureRepository.getById(lit1.getId()).isBorrowed());
        assertEquals(clientRepository.getById(c.getId()).getCurrentWeight(), 0);
    }

}
