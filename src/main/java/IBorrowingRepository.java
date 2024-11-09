import java.util.List;

public interface IBorrowingRepository extends Repository<Borrowing> {
    // update to chyba powinny byc jakies konkretne, np update endDate
    void updateClient(Borrowing borrowing, Client client);
}
