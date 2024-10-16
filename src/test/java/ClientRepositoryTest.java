import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClientRepositoryTest {
    ClientRepository clientRepository = new ClientRepository();
    @Test
    public void clientTests(){
        Client c = new Client("Jan", "Kowalski", "123", 10);
        clientRepository.create(c);
        assertEquals(clientRepository.getByFirstName("Jan").getFirst(), c);

    }

    @Test
    public void clientUpdateTest() {
        Client c = new Client("Marcin", "Kowalski", "456", 10);
        clientRepository.create(c);
        clientRepository.updateClientFirstNameById(c.getId(), "Marcin");
        assertEquals(clientRepository.getById(c.getId()).getFirstName(), "Marcin");
    }
}
