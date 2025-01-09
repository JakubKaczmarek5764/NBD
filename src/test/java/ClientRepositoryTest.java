import objects.Client;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.ClientRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientRepositoryTest {
    private ClientRepository clientRepository = new ClientRepository();
    private Client c;
    private Client c2;

    @BeforeEach
    public void prepareForTests() {
        c = new Client(UUID.randomUUID(), "Jan", "Kowalski", "123", 10, 0);
        c2 = new Client(UUID.randomUUID(), "Jan", "Kowalski", "123", 10, 0);

    }

    @AfterAll
    public static void close() {

    }

    @Test
    public void clientCreateTest() {
        clientRepository.create(c);
        Client c3 = clientRepository.getById(c.getClientId());
        assertEquals(c.getFirstName(), c3.getFirstName());
        assertEquals(c.getLastName(), c3.getLastName());
        assertEquals(c.getPersonalID(), c3.getPersonalID());

    }

    @Test
    public void clientUpdateTest() {
        clientRepository.create(c);
        c.setFirstName("Marcin");
        clientRepository.update(c);
        Client c3 = clientRepository.getById(c.getClientId());
        assertEquals(c.getFirstName(), c3.getFirstName());
        assertEquals(c.getLastName(), c3.getLastName());
        assertEquals(c.getPersonalID(), c3.getPersonalID());

    }

    @Test
    public void clientDeleteTest() {
        clientRepository.create(c);
        assertEquals(clientRepository.getById(c.getClientId()).getFirstName(), "Jan");
        clientRepository.delete(c);
        assertEquals(clientRepository.getById(c.getClientId()), null);
    }

    @Test
    public void clientCollectionEmptyCollectionTest() {
        clientRepository.create(c);
        clientRepository.emptyCollection();
        assertEquals(clientRepository.getById(c.getClientId()), null);
    }
}
