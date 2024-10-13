import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.metamodel.Attribute;
import jakarta.persistence.metamodel.EntityType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BorrowingRepositoryTest {
    @Test
    public void borrowingTests(){
        Client c = new Client("Jan", "Kowalski", "123", 10);
        ClientRepository.create(c);
        System.out.println(ClientRepository.getById(c.getId()));
        Literature lit1 = new Book("Pan Tadeusz", "Epopeja", "Adam Mickiewicz", 2, 2);
        LiteratureRepository.create(lit1);
        GregorianCalendar date = GregorianCalendar.from(ZonedDateTime.now());
        Borrowing bor1 = new Borrowing(date, date, c, lit1);
        BorrowingRepository.create(bor1);
        assertTrue(BorrowingRepository.checkLiteratureById(lit1.getId()));
        Literature lit2 = new Magazine("Swiat Nauki", "2002/11", 8);
        LiteratureRepository.create(lit2);
        assertFalse(BorrowingRepository.checkLiteratureById(lit2.getId()));
        Borrowing bor2 = new Borrowing(date, date, c, lit2);
        assertThrows(WeightExceededException.class,() -> {BorrowingRepository.create(bor2);});
    }
}
