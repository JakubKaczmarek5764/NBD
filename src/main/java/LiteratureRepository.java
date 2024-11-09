import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class LiteratureRepository extends AbstractMongoRepository implements ILiteratureRepository {

    private final MongoCollection<Literature> literatureCollection = initDbConnection().getCollection("literature", Literature.class);

    @Override
    public void updateAuthor(Literature literature, String author) {
        Bson filter = Filters.eq("_id", literature.getId());
        Bson update = Updates.set("author", author);
        literatureCollection.updateOne(filter, update);
    }

    @Override
    public void create(Literature obj) {
        literatureCollection.insertOne(obj);
    }

    @Override
    public List<Literature> readAll() {
        return literatureCollection.find().into(new ArrayList<Literature>());
    }

    @Override
    public void delete(Literature obj) {
        Bson filter = Filters.eq("_id", obj.getId());
        literatureCollection.deleteOne(filter);
    }
}
