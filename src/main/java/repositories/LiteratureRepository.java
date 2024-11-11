package repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import mappers.MongoUniqueId;
import objects.Literature;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class LiteratureRepository extends AbstractMongoRepository implements ILiteratureRepository {

    private final MongoCollection<Literature> literatureCollection = initDbConnection().getCollection("literature", Literature.class);


    @Override
    public void create(Literature obj) {
        literatureCollection.insertOne(obj);
    }

    @Override
    public List<Literature> getAll() {
        return literatureCollection.find().into(new ArrayList<Literature>());
    }

    @Override
    public Literature getById(MongoUniqueId id) {
        Bson filter = Filters.eq("_id", id);
        return literatureCollection.find(filter).first();
    }

    @Override
    public void delete(Literature obj) {
        Bson filter = Filters.eq("_id", obj.getId());
        literatureCollection.deleteOne(filter);
    }

    @Override
    public void update(Literature obj) {
        Bson filter = Filters.eq("_id", obj.getId());
        literatureCollection.replaceOne(filter, obj);
    }

    @Override
    public void drop() {
        literatureCollection.drop();
    }
}
