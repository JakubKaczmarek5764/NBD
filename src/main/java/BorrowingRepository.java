import java.util.List;

public class BorrowingRepository {
    public static void create(Borrowing borrowing){
        // tu chyba logika biznesowa
        Repository.create(borrowing);
    }
    public static List<Borrowing> getAll(){
        return Repository.getAll(Borrowing.class);
    }
//    public static List<Borrowing> getByClientId(long id){
//        return Repository.getBy(Borrowing.class, id, "id");
//    }
//    public static List<Borrowing> getByLiteratureId(long id){
//        return Repository.getBy(Borrowing.class, id, "")
//    }
}
