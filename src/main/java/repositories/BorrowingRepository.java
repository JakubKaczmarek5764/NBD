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
//        Bson filter = Filters.eq("client", obj.getClient().getClientId().getId());
//        Bson projections = Projections.exclude(obj.getClient().getClientId().toString(), obj.getLiterature().getLiteratureId().toString());
//        AggregateIterable<Document> aggregates = borrowingCollection.aggregate(List.of(
//                Aggregates.match(filter),
//                Aggregates.lookup("clients", obj.getClient().getClientId().getId().toString(), "_id", "clientData"),
//                Aggregates.unwind("$" + "clientData"),
//                Aggregates.lookup("literature", obj.getLiterature().getLiteratureId().getId().toString(), "_id", "literatureData"),
//                Aggregates.unwind("$" + "literatureData"),
//                Aggregates.project(projections)
//        ));
//        ArrayList<Document> documents = aggregates.into(new ArrayList<>());
//        System.out.println(documents);
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