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
        Client c1 = new Client("Jan", "Nowak", "1234", 10);
        clientRepository.create(c1);
        assertEquals(clientRepository.getAll().get(1), c1);
        clientRepository.delete(c1.getId());
        assertEquals(clientRepository.getAll().size(), 1);
    }

    @Test
    public void clientUpdateTest() {
        Client c = new Client("Marcin", "Kowalski", "456", 10);
        clientRepository.create(c);
        clientRepository.update(c.getId());
        assertEquals(clientRepository.getById(c.getId()).getFirstName(), "Marcin");
    }
}
