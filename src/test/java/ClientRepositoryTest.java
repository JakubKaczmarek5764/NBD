import dao.ClientDao;
import mappers.ClientMapper;
import mappers.ClientMapperBuilder;
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
    private ClientMapper clientMapper = new ClientMapperBuilder(clientRepository.getSession()).build();
    private ClientDao clientDao = clientMapper.clientDao();

    @BeforeEach
    public void prepareForTests() {
        c = new Client(UUID.randomUUID(), "Jan", "Kowalski", "123", 10, 0);
        c2 = new Client(UUID.randomUUID(), "Jan", "Kowalski", "123", 10, 0);
//        clientRepository = new ClientRepository();
//        clientMapper = new ClientMapperBuilder(clientRepository.getSession()).build();
//        clientDao = clientMapper.clientDao();
    }

    @AfterAll
    public static void close() {

    }

    @Test
    public void clientCreateTest() {
        clientDao.create(c);
        Client c3 = clientDao.getById(c.getClientId());
        assertEquals(c.getFirstName(), c3.getFirstName());
        assertEquals(c.getLastName(), c3.getLastName());
        assertEquals(c.getPersonalID(), c3.getPersonalID());

    }

    @Test
    public void clientUpdateTest() {
        clientDao.create(c);
        c.setFirstName("Marcin");
        clientDao.update(c);
        Client c3 = clientDao.getById(c.getClientId());
        assertEquals(c.getFirstName(), c3.getFirstName());
        assertEquals(c.getLastName(), c3.getLastName());
        assertEquals(c.getPersonalID(), c3.getPersonalID());

    }


    @Test
    public void clientDeleteTest() {
        // mozna zrobic z metoda getall jak bedzie dzialac, ale troche fikolki trzebaby zrobic zeby sprawdzac jej dlugosc
        clientDao.create(c);
        assertEquals(clientDao.getById(c.getClientId()).getFirstName(), "Jan");
        clientDao.delete(c);
        assertEquals(clientDao.getById(c.getClientId()), null);
    }
    @Test
    public void clientCollectionEmptyCollectionTest() {
        clientDao.create(c);
        clientRepository.emptyCollection();
        assertEquals(clientDao.getById(c.getClientId()), null);
    }
}
