import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClientRepositoryTest {
    @Test
    public void clientTests(){
        Client c = new Client("Jan", "Kowalski", "123", 10);
        ClientRepository.create(c);
        assertEquals(ClientRepository.getByFirstName("Jan").get(0), c);
        assertThrows(NotFoundException.class, () -> {ClientRepository.getByFirstName("Adam");});
        ClientRepository.getByPersonalID("1234");
    }
}
