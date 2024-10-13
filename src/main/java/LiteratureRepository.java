import java.util.List;

public class LiteratureRepository {
    public static void create(Literature literature){Repository.create(literature);}
    public static List<Literature> getAll(){
        return Repository.getAll(Literature.class);
    }
    public static Literature getById(long id){
        return Repository.getBy(Literature.class, id, "id").get(0);
    }
    public static List<Literature> getByName(String name){
        return Repository.getBy(Literature.class, name, "name");
    }
    public static List<Literature> getByWeight(int weight){
        return Repository.getBy(Literature.class, weight, "weight");
    }
    public static List<Book> getBookByAuthor(String author){
        return Repository.getBy(Book.class, author, "author");
    }

}
