import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LiteratureRepositoryTest {
    LiteratureRepository literatureRepository = new LiteratureRepository();
    @Test
    public void literatureTests(){
        Book b = new Book("Pan Tadeusz", "Epopeja", "Mickiewicz", 2, 2);
        literatureRepository.create(b);
        assertEquals(literatureRepository.getAll().getFirst(), b);
        Magazine m = new Magazine("Swiat nauki", "2001/10", 2);
        literatureRepository.create(m);
        assertEquals(literatureRepository.getBookByAuthor("Mickiewicz").getFirst(), b);
        assertEquals(literatureRepository.getByName("Swiat nauki").getFirst(), m);
        assertEquals(2, literatureRepository.getByWeight(2).size());

    }
}
