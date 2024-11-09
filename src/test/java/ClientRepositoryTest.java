import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientRepositoryTest {

    private Client c;
    private Client c2;

    @BeforeEach
    public void prepareForTests() {
        c = new Client("Jan", "Kowalski", "123", 10);
        c2 = new Client("Jan", "Kowalski", "456", 10);
    }

    @AfterAll
    public static void close() {


    }

    @Test
    public void clientCreateTest() {
        ClientRepository cr = new ClientRepository();
        cr.create(c);
        System.out.println(cr.readAll());

    }

//    @Test
//    public void clientUpdateTest() {
//        clientRepository.create(c);
//        c.setFirstName("Marcin");
//        clientRepository.update(c);
//        assertEquals(clientRepository.getById(c.getId()).getFirstName(), "Marcin");
//    }
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
