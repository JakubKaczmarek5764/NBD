package repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import mappers.MongoUniqueId;
import objects.Borrowing;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class BorrowingRepository extends AbstractMongoRepository implements IBorrowingRepository {
    private MongoCollection<Borrowing> borrowingCollection = initDbConnection().getCollection("borrowings", Borrowing.class);

    @Override
    public void create(Borrowing obj) {
        borrowingCollection.insertOne(obj);
    }

    @Override
    public List<Borrowing> getAll() {
        return borrowingCollection.find().into(new ArrayList<Borrowing>());
    }

    @Override
    public Borrowing getById(MongoUniqueId id) {
        Bson filter = Filters.eq("_id", id);
        return borrowingCollection.find(filter).first();
    }

    @Override
    public void delete(Borrowing obj) {
        // mozliwe ze do wszystkich obj.getId() trzeba bedzie dodac jeszcze jedno getId() zeby wyciagac UUID,
        // ale to zobaczymy jak baza bedzie stala
        Bson filter = Filters.eq("_id", obj.getBorrowingId());
        borrowingCollection.deleteOne(filter);
    }

    @Override
    public void update(Borrowing obj) {
        Bson filter = Filters.eq("_id", obj.getBorrowingId());
        borrowingCollection.replaceOne(filter, obj);
    }

    @Override
    public void drop() {
        borrowingCollection.drop();
    }


}