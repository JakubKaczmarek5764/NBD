import mappers.MongoUniqueId;
import objects.Client;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repositories.ClientRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientRepositoryTest {
    private ClientRepository clientRepository = new ClientRepository();
    private Client c;
    private Client c2;

    @BeforeEach
    public void prepareForTests() {
        clientRepository.emptyCollection();
        c = new Client(new MongoUniqueId(new ObjectId()),"Jan", "Kowalski", "123", 10,0);
        c2 = new Client(new MongoUniqueId(new ObjectId()),"Jan", "Kowalski", "456", 10, 0);
    }

    @AfterAll
    public static void close() {


    }

    @Test
    public void clientCreateTest() {
        clientRepository.create(c);
        assertEquals(1, clientRepository.getAll().size());
    }

    @Test
    public void clientUpdateTest() {
        clientRepository.create(c);
        c.setFirstName("Marcin");
        clientRepository.update(c);

        assertEquals(clientRepository.getById(c.getClientId()).getFirstName(), "Marcin");
    }

    @Test
    public void clientGettersTests() {
        clientRepository.create(c);
        clientRepository.create(c2);
        assertEquals(clientRepository.getAll().size(), 2);
    }

    @Test
    public void clientDeleteTest() {
        clientRepository.create(c);
        clientRepository.create(c2);
        assertEquals(clientRepository.getAll().size(), 2);
        clientRepository.delete(c);
        assertEquals(clientRepository.getAll().size(), 1);
    }

    @Test
    public void clientCollectionEmptyCollectionTest() {
        clientRepository.create(c);
        clientRepository.create(c2);
        clientRepository.emptyCollection();
        assertEquals(clientRepository.getAll().size(), 0);
    }
}
