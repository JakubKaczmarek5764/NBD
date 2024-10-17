import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class BorrowingRepositoryTest {
    BorrowingRepository borrowingRepository = new BorrowingRepository();
    ClientRepository clientRepository = new ClientRepository();
    LiteratureRepository literatureRepository = new LiteratureRepository();
    @Test
    public void borrowingTests() {
        Client c = new Client("Jan", "Kowalski", "123", 10);
        clientRepository.create(c);
        System.out.println(clientRepository.getById(c.getId()));
        Literature lit1 = new Book("Pan Tadeusz", "Epopeja", "Adam Mickiewicz", 2, 2);
        literatureRepository.create(lit1);
        System.out.println(literatureRepository.getById(lit1.getId()).getTotalWeight());
        GregorianCalendar date = GregorianCalendar.from(ZonedDateTime.now());
        Borrowing bor1 = new Borrowing(date, date, c, lit1);
        borrowingRepository.create(bor1);
        Client c2 = clientRepository.getById(bor1.getClient().getId());
        System.out.println("po bor1: " + c2.getCurrentWeight());
        Literature lit2 = new Magazine("Swiat Nauki", "2002/11", 8);
        literatureRepository.create(lit2);
        Borrowing bor2 = new Borrowing(date, date, c, lit2);
        System.out.println(c.getCurrentWeight());
        assertThrows(WeightExceededException.class,() -> {borrowingRepository.create(bor2);});
        borrowingRepository.endBorrowing(bor1);
    }

}
