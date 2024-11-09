import java.util.List;


interface Repository<T> {

    void create(T obj);
    List<T> readAll();
    void delete(T obj);
}
