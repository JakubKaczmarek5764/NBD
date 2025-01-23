package repositories;

import com.mongodb.ClientSessionOptions;
import com.mongodb.MongoCommandException;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import exceptions.WeightExceededException;
import kafka.Producer;
import mappers.MongoUniqueId;
import objects.Borrowing;
import objects.Client;
import objects.Literature;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class BorrowingRepositoryConsumer extends AbstractMongoRepositoryConsumer implements IBorrowingRepository {
    private final MongoCollection<Borrowing> borrowingCollection = getDatabase().getCollection("borrowing", Borrowing.class);
    private final ClientSessionOptions sessionOptions = ClientSessionOptions.builder()
            .defaultTransactionOptions(TransactionOptions.builder()
                    .writeConcern(WriteConcern.MAJORITY)
                    .build())
            .build();

    public BorrowingRepositoryConsumer() {    }
    @Override
    public void create(Borrowing obj) {
        ClientSession clientSession = mongoClient.startSession(sessionOptions);
        try {
            clientSession.startTransaction();
            MongoCollection<Client> clientCollection = getDatabase().getCollection("clients", Client.class);
            Bson clientFilter = Filters.eq("_id", obj.getClient().getClientId());
            Client client = clientCollection.find(clientFilter).first();
            if (client != null && (client.getCurrentWeight() + obj.getLiterature().getTotalWeight()) > client.getMaxWeight()) {
                throw new WeightExceededException();
            }
            MongoCollection<Literature> literatureCollection = getDatabase().getCollection("literature", Literature.class);
            Bson filter = Filters.eq("_id", obj.getLiterature().getLiteratureId().getId());
            Bson update = Updates.inc("isBorrowed", 1);
            Bson clientUpdate = Updates.inc("currentWeight", obj.getLiterature().getTotalWeight());
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

    @Override
    public void endBorrowing(Borrowing obj) {
        ClientSession clientSession = mongoClient.startSession(sessionOptions);
        try {
            clientSession.startTransaction();
            MongoCollection<Literature> literatureCollection = getDatabase().getCollection("literature", Literature.class);
            MongoCollection<Client> clientCollection = getDatabase().getCollection("clients", Client.class);
            literatureCollection.updateOne(clientSession, Filters.eq("_id",
                    obj.getLiterature().getLiteratureId()), Updates.inc("isBorrowed", -1));
            clientCollection.updateOne(clientSession, Filters.eq("_id", obj.getClient().getClientId()), Updates.inc("currentWeight", -obj.getLiterature().getTotalWeight()));
            borrowingCollection.updateOne(clientSession, Filters.eq("_id", obj.getBorrowingId()), Updates.set("endDate", ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS)));
            clientSession.commitTransaction();
        } catch (Exception e) {
            clientSession.abortTransaction();
        } finally {
            clientSession.close();
        }
    }

}