
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
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
    public List<Borrowing> readAll() {
        return borrowingCollection.find().into(new ArrayList<Borrowing>());
    }

    @Override
    public void delete(Borrowing obj) {
        Bson filter = Filters.eq("_id", obj.getId());
        borrowingCollection.deleteOne(filter);
    }

    @Override
    public void updateClient(Borrowing borrowing, Client client) {
        Bson filter = Filters.eq("_id", borrowing.getId());
        Bson update = Updates.set("client", client);
        borrowingCollection.updateOne(filter, update);
    }

}