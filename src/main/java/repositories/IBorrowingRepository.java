package repositories;

import objects.Borrowing;

public interface IBorrowingRepository extends Repository<Borrowing> {
    void endBorrowing(Borrowing obj);
}
