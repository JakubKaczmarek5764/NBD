import java.util.List;

public interface ILiteratureRepository extends Repository<Literature> {
    void updateAuthor(Literature literature, String author);
}
