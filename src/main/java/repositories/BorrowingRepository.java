package repositories;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import mappers.MongoUniqueId;
import objects.Borrowing;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class BorrowingRepository extends AbstractMongoRepository implements IBorrowingRepository {
    private MongoCollection<Borrowing> borrowingCollection = initDbConnection().getCollection("borrowings", Borrowing.class);

    @Override
    public void create(Borrowing obj) {
        System.out.println(obj.getClient());
        Bson filter = Filters.eq("client", obj.getClient().getClientId().getId());
        Bson projections = Projections.exclude(obj.getClient().getClientId().getId().toString(), obj.getLiterature().getLiteratureId().getId().toString());
        AggregateIterable<Borrowing> aggregates = borrowingCollection.aggregate(List.of(
                Aggregates.match(filter),
                Aggregates.lookup("clients", obj.getClient().getClientId().getId().toString(), "_id", obj.getClient().toString()),
                Aggregates.unwind("$" + obj.getClient().toString()),
                Aggregates.lookup("literature", obj.getLiterature().getLiteratureId().getId().toString(), "_id", obj.getLiterature().toString()),
                Aggregates.unwind("$" + obj.getLiterature().toString()),
                Aggregates.project(projections)
        ));

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