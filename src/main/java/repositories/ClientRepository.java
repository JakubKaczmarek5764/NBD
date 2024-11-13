package repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import mappers.MongoUniqueId;
import objects.Client;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class ClientRepository extends AbstractMongoRepository implements IClientRepository {
    private final MongoCollection<Client> clientCollection = initDbConnection().getCollection("clients", Client.class);


    @Override
    public void create(Client obj) {
        clientCollection.insertOne(obj);
    }

    @Override
    public List<Client> getAll() {
        return clientCollection.find().into(new ArrayList<>());
    }

    @Override
    public Client getById(MongoUniqueId id) {
        Bson filter = Filters.eq("_id", id);
        return clientCollection.find(filter).first();
    }

    @Override
    public void delete(Client obj) {
        Bson filter = Filters.eq("_id", obj.getClientId());
        clientCollection.deleteOne(filter);
    }

    @Override
    public void update(Client obj) {
        Bson filter = Filters.eq("_id", obj.getClientId());
        clientCollection.replaceOne(filter, obj);
    }

    @Override
    public void emptyCollection() {
        clientCollection.deleteMany(new Document());
    }
}
