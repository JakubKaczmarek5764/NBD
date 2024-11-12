
import mappers.MongoUniqueId;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.BorrowingRepository;
import repositories.IBorrowingRepository;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;


import static org.junit.jupiter.api.Assertions.*;

public class BorrowingRepositoryTest {
    private static final repositories.BorrowingRepository borrowingRepository = new repositories.BorrowingRepository();
    private static final repositories.ClientRepository clientRepository = new repositories.ClientRepository();
    private static final repositories.LiteratureRepository literatureRepository = new repositories.LiteratureRepository();
    private objects.Client c;
    private objects.Client c2;
    private objects.Literature lit1;
    private objects.Literature lit2;
    private objects.Borrowing bor1;
    private objects.Borrowing bor2;

    @BeforeEach
    public void prepareForTests() {
        IBorrowingRepository borrowingRepository = new BorrowingRepository();
        borrowingRepository.drop();
        c = new objects.Client(new MongoUniqueId(new ObjectId()), "Jan", "Kowalski", "123", 10);
        lit1 = new objects.Book(new MongoUniqueId(new ObjectId()), "Pan Tadeusz", "Epopeja", "Adam Mickiewicz", 2, 2, 0);
        ZonedDateTime date = ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        bor1 = new objects.Borrowing(new MongoUniqueId(new ObjectId()), date, null, c, lit1);
        lit2 = new objects.Magazine(new MongoUniqueId(new ObjectId()), "Swiat Nauki", "2002/11", 8, 0);
        bor2 = new objects.Borrowing(new MongoUniqueId(new ObjectId()), date, null, c, lit2);
    }

    @AfterAll
    public static void close() {
    }

    @Test
    public void borrowingCreateTest() {
        borrowingRepository.create(bor1);
        assertEquals(borrowingRepository.getAll().size(), 1);
    }

    @Test
    public void borrowingGettersTests() {
        borrowingRepository.create(bor1);
        borrowingRepository.create(bor2);
        assertEquals(borrowingRepository.getAll().size(), 2);
    }

    @Test
    public void borrowingCollectionDropTest() {
        borrowingRepository.create(bor1);
        borrowingRepository.create(bor2);
        borrowingRepository.drop();
        assertEquals(borrowingRepository.getAll().size(), 0);
    }

    @Test
    public void borrowingUpdateTest() {
        borrowingRepository.create(bor1);
        ZonedDateTime date = ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        bor1.setEndDate(date);
        borrowingRepository.update(bor1);
        assertEquals(borrowingRepository.getById(bor1.getBorrowingId()).getEndDate(), date);
    }

    @Test
    public void borrowingDeleteTest() {
        borrowingRepository.create(bor1);
        borrowingRepository.create(bor2);
        borrowingRepository.delete(bor1);
        assertEquals(borrowingRepository.getAll().size(), 1);
    }

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

//    @Test
//    public void endBorrowingTest() {
//        clientRepository.create(c);
//        literatureRepository.create(lit1);
//        borrowingRepository.create(bor1);
//        borrowingRepository.endBorrowing(bor1);
//        assertFalse(literatureRepository.getById(lit1.getId()).isBorrowed());
//        assertEquals(clientRepository.getById(c.getId()).getCurrentWeight(), 0);
//        List<objects.Borrowing> list = borrowingRepository.getAllBorrowingsByClientId(c.getId());
//        assertEquals(list.size(), 1);
//    }
}
