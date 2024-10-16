import java.util.List;

public interface IRepository<T> {
    void create();
    List<T> getAll();

}
