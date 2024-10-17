import java.util.List;

public class LiteratureRepository implements ILiteratureRepository{
    public void create(Literature literature){Repository.create(literature);}
    public List<Literature> getAll(){
        return Repository.getAll(Literature.class);
    }
    public Literature getById(long id){
        return Repository.getByParam(Literature.class, id, "id").get(0);
    }
    public List<Literature> getByName(String name){
        return Repository.getByParam(Literature.class, name, "name");
    }
    public List<Literature> getByWeight(int weight){
        return Repository.getByParam(Literature.class, weight, "weight");
    }
    public List<Book> getBookByAuthor(String author){
        return Repository.getByParam(Book.class, author, "author");
    }
    @Override
    public void delete(Literature literature){
        Repository.delete(literature);
    }
    @Override
    public void update(Literature literature){
        Repository.update(literature);
    }
}
