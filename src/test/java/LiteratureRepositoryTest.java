import org.junit.jupiter.api.Test;

public class LiteratureRepositoryTest {
    @Test
    public void persistTest(){
        Book b = new Book("Pan Tadeusz", "Epopeja", "Mickiewicz", 2, 2);
        LiteratureRepository.create(b);
        System.out.println(LiteratureRepository.getAll());
        Magazine m = new Magazine("Swiat nauki", "2001/10", 2);
        LiteratureRepository.create(m);
        System.out.println(LiteratureRepository.getAll());
    }
}
