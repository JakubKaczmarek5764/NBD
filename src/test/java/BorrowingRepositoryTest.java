
import objects.Book;
import objects.Borrowing;
import objects.Magazine;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BorrowingRepositoryTest {
    private static final repositories.BorrowingRepository borrowingRepository = new repositories.BorrowingRepository();
    private static final repositories.ClientRepository clientRepository = new repositories.ClientRepository();
    private static final repositories.LiteratureRepository literatureRepository = new repositories.LiteratureRepository();
    private objects.Client c;
    private objects.Literature lit1;
    private objects.Literature lit2;
    private objects.Borrowing bor1;
    private objects.Borrowing bor2;

    @BeforeEach
    public void prepareForTests() {
        c = new objects.Client(UUID.randomUUID(), "Jan", "Kowalski", "123", 10, 0);
        lit1 = new Book(UUID.randomUUID(), "Epopeja", "Adam Mickiewicz", 2, "book", "Pan Tadeusz", 2, 0);
        lit2 = new Magazine(UUID.randomUUID(), "2002/11", "magazine", "Swiat Nauki", 8, 0);
        ZonedDateTime date = ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        bor1 = new objects.Borrowing(UUID.randomUUID(), date, null, c, lit1);
        bor2 = new objects.Borrowing(UUID.randomUUID(), date, null, c, lit2);
    }

    @AfterAll
    public static void close() {

    }

    @Test
    public void borrowingCreateTest() {
        borrowingRepository.create(bor1);
        assertEquals(borrowingRepository.getAllBorrowingsByClientId(c.getClientId()).size(), 1);
    }

    @Test
    public void borrowingGettersTests() {
        borrowingRepository.create(bor1);
        borrowingRepository.create(bor2);
        assertEquals(borrowingRepository.getAllBorrowingsByClientId(c.getClientId()).size(), 2);
    }

    @Test
    public void borrowingCollectionEmptyCollectionTest() {
        borrowingRepository.create(bor1);
        borrowingRepository.create(bor2);
        borrowingRepository.emptyCollection();
        assertEquals(borrowingRepository.getAllBorrowingsByClientId(c.getClientId()).size(), 0);
    }
    @Test
    public void borrowingDeleteTest() {
        borrowingRepository.create(bor1);
        borrowingRepository.create(bor2);
        borrowingRepository.delete(bor1);
        assertEquals(borrowingRepository.getAllBorrowingsByClientId(c.getClientId()).size(), 1);
        assertEquals(borrowingRepository.getAllBorrowingsByLiteratureId(lit2.getLiteratureId()).size(), 1);
    }
//
//    @Test
//    public void borrowingUpdateTest() {
//        borrowingRepository.create(bor1);
//        ZonedDateTime date = ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS);
//        bor1.setEndDate(date);
//        borrowingRepository.update(bor1);
//        assertEquals(borrowingRepository.getById(bor1.getBorrowingId()).getEndDate(), date);
//    }
//

//
//    @Test
//    public void businessLogicTest() {
//        clientRepository.create(c);
//        literatureRepository.create(lit1);
//        borrowingRepository.create(bor1);
//        literatureRepository.create(lit2);
//        assertThrows(exceptions.WeightExceededException.class, () -> {
//            borrowingRepository.create(bor2);
//        });
//    }

    @Test
    public void endBorrowingTest() {
        clientRepository.create(c);
        literatureRepository.create(lit1);
        assertEquals(literatureRepository.getById(lit1.getLiteratureId()).getIsBorrowed(), 1);
        assertEquals(clientRepository.getById(c.getClientId()).getCurrentWeight(), 4);
        borrowingRepository.create(bor1);
        borrowingRepository.endBorrowing(bor1);
        assertEquals(literatureRepository.getById(lit1.getLiteratureId()).getIsBorrowed(), 0);
        assertEquals(clientRepository.getById(c.getClientId()).getCurrentWeight(), 0);
        List<Borrowing> list = borrowingRepository.getAllBorrowingsByClientId(c.getClientId());
        assertEquals(list.size(), 1);
    }
}
