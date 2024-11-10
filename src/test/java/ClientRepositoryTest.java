import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientRepositoryTest {
    private IClientRepository clientRepository = new ClientRepository();
    private Client c;
    private Client c2;

    @BeforeEach
    public void prepareForTests() {
        clientRepository.drop();
        c = new Client("Jan", "Kowalski", "123", 10);
        c2 = new Client("Jan", "Kowalski", "456", 10);
    }

    @AfterAll
    public static void close() {


    }

    @Test
    public void clientCreateTest() {
        clientRepository.create(c);
        System.out.println(clientRepository.getAll());

    }

    @Test
    public void clientUpdateTest() {
        clientRepository.create(c);
        c.setFirstName("Marcin");
        System.out.println(c);
        clientRepository.update(c);
        System.out.print(clientRepository.getAll());
//        assertEquals(clientRepository.getById(c.getId()).getFirstName(), "Marcin");
    }
//
//    @Test
//    public void clientGettersTests() {
//        clientRepository.create(c);
//        clientRepository.create(c2);
//        assertEquals(clientRepository.getByFirstName("Jan").getFirst(), c);
//        assertEquals(clientRepository.getByFirstName("Jan").size(), 2);
//        assertEquals(clientRepository.getByLastName("Kowalski").getFirst(), c);
//        assertEquals(clientRepository.getByLastName("Kowalski").size(), 2);
//        assertEquals(clientRepository.getByPersonalID("123"), c);
//        assertEquals(clientRepository.getAll().size(), 2);
//    }
//
//    @Test
//    public void clientDeleteTest() {
//        clientRepository.create(c);
//        clientRepository.create(c2);
//        assertEquals(clientRepository.getAll().size(), 2);
//        clientRepository.delete(c.getId());
//        assertEquals(clientRepository.getAll().size(), 1);
//    }
}
