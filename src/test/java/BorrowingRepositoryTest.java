import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BorrowingRepositoryTest {
    @Test
    public void persistBorrowing(){
        Client c = new Client("Jan", "Kowalski", "123", 10);
        ClientRepository.create(c);

    }
}
