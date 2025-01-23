import kafka.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.BorrowingRepositoryConsumer;

public class KafkaConsumerTest {
    @BeforeEach
    public void init() {
        BorrowingRepositoryConsumer borrowingRepository = new BorrowingRepositoryConsumer();
        borrowingRepository.emptyCollection();
    }
    @Test
    public void consumeTest() throws InterruptedException {
        Consumer.consumeTopicsByGroup("borrowings");

    }
}
