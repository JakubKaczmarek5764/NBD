package repositories;

import com.mongodb.MongoCommandException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ValidationOptions;
import exceptions.WeightExceededException;
import mappers.MongoUniqueId;
import objects.Borrowing;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class BorrowingRepository extends AbstractMongoRepository implements IBorrowingRepository {
    private MongoCollection<Borrowing> borrowingCollection;
    public BorrowingRepository() {
        for (String name : getDatabase().listCollectionNames()){
            if (name.equals("borrowings")){
                getDatabase().getCollection("borrowings").drop();
            }
        }

        getDatabase().createCollection("borrowings", new CreateCollectionOptions().validationOptions(new ValidationOptions().validator(
                Document.parse("""
                        {
                          $jsonSchema: {
                             bsonType: "object"
                             properties: {
                                has_rent: {
                                   bsonType: "int",
                                   minimum: 0,
                                   maximum: 1,
                                   description: "has_rent can have only value of 0 or 1"
                                },
                             }
                          }
                        }                           \s
                    """)
        )));
        borrowingCollection = getDatabase().getCollection("borrowings", Borrowing.class);

    }
    @Override
    public void create(Borrowing obj) {
        ClientSession clientSession = mongoClient.startSession();
        try {
            clientSession.startTransaction();
            // tu chyba te twoje

            clientSession.commitTransaction();
        } catch (MongoCommandException | WeightExceededException e) {
            clientSession.abortTransaction();
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
        borrowingCollection.insertOne(obj);
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