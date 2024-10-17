import java.util.List;

public interface ILiteratureRepository {
    void create(Literature literature);
    List<Literature> getAll();
    Literature getById(long id);
    List<Literature> getByName(String name);
    List<Literature> getByWeight(int weight);
    List<Book> getBookByAuthor(String author);
    void delete(Literature literature);
    void update(Literature literature);
}
