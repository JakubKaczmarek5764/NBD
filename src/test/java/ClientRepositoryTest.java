import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClientRepositoryTest {
    @Test
    public void clientTests(){
        Client c = new Client("Jan", "Kowalski", "123", 10);
        ClientRepository.create(c);
        assertEquals(ClientRepository.getByFirstName("Jan").get(0), c);

    }

    @Test
    public void clientUpdateTest() {
        Client c = new Client("Jan", "Kowalski", "123", 10);
        ClientRepository.create(c);
        ClientRepository.updateClientFirstNameById(c.getId(), "Marcin");
        assertEquals(ClientRepository.getById(c.getId()).getFirstName(), "Marcin");
    }
}
