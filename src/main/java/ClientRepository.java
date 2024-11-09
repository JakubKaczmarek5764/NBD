import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class ClientRepository extends AbstractMongoRepository implements IClientRepository {
    private final MongoCollection<Client> clientCollection = initDbConnection().getCollection("clients", Client.class);

    @Override
    public void updateFirstName(Client client, String firstName) {
        Bson filter = Filters.eq("_id", client.getId());
        Bson update = Updates.set("firstName", firstName);
        clientCollection.updateOne(filter, update);
    }

    @Override
    public void create(Client obj) {
        clientCollection.insertOne(obj);
    }

    @Override
    public List<Client> readAll() {
        return clientCollection.find().into(new ArrayList<>());
    }

    @Override
    public void delete(Client obj) {
        Bson filter = Filters.eq("_id", obj.getId());
        clientCollection.deleteOne(filter);
    }
}
