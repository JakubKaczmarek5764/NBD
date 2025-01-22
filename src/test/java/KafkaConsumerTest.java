import kafka.Consumer;
import org.junit.jupiter.api.Test;

public class KafkaConsumerTest {
    @Test
    public void consumeTest() throws InterruptedException {
        Consumer.consumeTopicsByGroup("borrowings");

    }
}
