import kafka.Producer;
import mappers.MongoUniqueId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class KafkaProducerTest {
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
        borrowingRepository.emptyCollection();
        c = new objects.Client(new MongoUniqueId(UUID.randomUUID()), "Jan", "Kowalski", "123", 10, 0);
        lit1 = new objects.Book(new MongoUniqueId(UUID.randomUUID()), "Pan Tadeusz", "Epopeja", "Adam Mickiewicz", 2, 2, 0);
        ZonedDateTime date = ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS);
        bor1 = new objects.Borrowing(new MongoUniqueId(UUID.randomUUID()), date, null, c, lit1);
        lit2 = new objects.Magazine(new MongoUniqueId(UUID.randomUUID()), "Swiat Nauki", "2002/11", 8, 0);
        bor2 = new objects.Borrowing(new MongoUniqueId(UUID.randomUUID()), date, null, c, lit2);
    }
    @Test
    public void sendBorrowingTest() {
        borrowingRepository.create(bor1);
        borrowingRepository.create(bor2);
    }
}
