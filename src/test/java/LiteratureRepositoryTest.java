import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LiteratureRepositoryTest {
    @Test
    public void literatureTests(){
        Book b = new Book("Pan Tadeusz", "Epopeja", "Mickiewicz", 2, 2);
        LiteratureRepository.create(b);
        assertEquals(LiteratureRepository.getAll().get(0), b);
        Magazine m = new Magazine("Swiat nauki", "2001/10", 2);
        LiteratureRepository.create(m);
        assertThrows(NotFoundException.class, () -> {LiteratureRepository.getBookByAuthor("Sienkiewicz");});
    }
}
