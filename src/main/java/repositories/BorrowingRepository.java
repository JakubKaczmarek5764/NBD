package repositories;

import com.mongodb.MongoCommandException;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import exceptions.WeightExceededException;
import mappers.MongoUniqueId;
import objects.Borrowing;
import objects.Client;
import objects.Literature;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class BorrowingRepository extends AbstractMongoRepository implements IBorrowingRepository {
    private final MongoCollection<Borrowing> borrowingCollection = getDatabase().getCollection("borrowing", Borrowing.class);


    @Override
    public void create(Borrowing obj) {
        ClientSession clientSession = mongoClient.startSession();
        try {
            clientSession.startTransaction();
            MongoCollection<Client> clientCollection = getDatabase().getCollection("clients", Client.class).withWriteConcern(WriteConcern.MAJORITY);
            Bson clientFilter = Filters.eq("_id", obj.getClient().getClientId());
            Client client = clientCollection.find(clientFilter).first();
            if (client != null && (client.getCurrentWeight() + obj.getLiterature().calculateTotalWeight()) > client.getMaxWeight()) {
                throw new WeightExceededException();
            }
            MongoCollection<Literature> literatureCollection = getDatabase().getCollection("literature", Literature.class);
            Bson filter = Filters.eq("_id", obj.getLiterature().getLiteratureId().getId());
            Bson update = Updates.inc("isBorrowed", 1);
            Bson clientUpdate = Updates.inc("currentWeight", obj.getLiterature().calculateTotalWeight());
            clientCollection.updateOne(clientFilter, clientUpdate);
            literatureCollection.updateOne(filter, update);
            borrowingCollection.insertOne(obj);
            clientSession.commitTransaction();

        } catch (MongoCommandException | WeightExceededException e) {
            clientSession.abortTransaction();
            throw new WeightExceededException();
        } finally {
            clientSession.close();
        }
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
    }

    @Override
    public List<Borrowing> getAll() {
        return borrowingCollection.find().into(new ArrayList<>());
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
    public void emptyCollection() {
        borrowingCollection.deleteMany(new Document());
    }

    public boolean collectionExists() {
        return this.collectionExists("borrowings");
    }

}