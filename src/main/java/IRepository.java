import java.util.List;

public interface IRepository<T> {
    <T> void create(T obj);
    List<T> getAll();
}
