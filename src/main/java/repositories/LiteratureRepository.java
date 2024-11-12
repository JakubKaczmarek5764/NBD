package repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ValidationOptions;
import mappers.MongoUniqueId;
import objects.Literature;
import org.bson.BsonType;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class LiteratureRepository extends AbstractMongoRepository implements ILiteratureRepository {

    private MongoCollection<Literature> literatureCollection;

    public LiteratureRepository() {
        if (!collectionExists()) {
            Bson isBorrowedType = Filters.type("isBorrowed", BsonType.INT32);
            Bson isBorrowedMax = Filters.lte("isBorrowed", 1);
            Bson isBorrowedMin = Filters.gte("isBorrowed", 0);
            Bson isBorrowed = Filters.and(isBorrowedType, isBorrowedMin, isBorrowedMax);
            ValidationOptions validationOptions = new ValidationOptions()
                    .validator(isBorrowed);
            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                    .validationOptions(validationOptions);
            nbd.createCollection("literature", createCollectionOptions);
        }
        literatureCollection = initDbConnection().getCollection("literature", Literature.class);
    }
    public boolean collectionExists() {
        MongoDatabase db = initDbConnection();
        for (String name : db.listCollectionNames()) {
            if (name.equals("literature")) {
                return true;
            }
        }
        return false;
    }

    private void createValidationOptions() {
        Bson isBorrowedType = Filters.type("isBorrowed", BsonType.INT32);
        Bson isBorrowedMax = Filters.lte("isBorrowed", 1);
        Bson isBorrowedMin = Filters.gte("isBorrowed", 0);
        Bson isBorrowed = Filters.and(isBorrowedType, isBorrowedMin, isBorrowedMax);
        ValidationOptions validationOptions = new ValidationOptions()
                .validator(isBorrowed);
        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                .validationOptions(validationOptions);
    }
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
        Bson filter = Filters.eq("_id", obj.getLiteratureId());
        literatureCollection.deleteOne(filter);
    }

    @Override
    public void update(Literature obj) {
        Bson filter = Filters.eq("_id", obj.getLiteratureId());
        literatureCollection.replaceOne(filter, obj);
    }

    @Override
    public void drop() {
        literatureCollection.drop();
    }

    @Override
    public MongoCollection<Literature> getLiteratureCollection() {
        return literatureCollection;
    }
}
