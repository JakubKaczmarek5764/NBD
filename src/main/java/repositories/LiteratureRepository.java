package repositories;

import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationOptions;
import mappers.MongoUniqueId;
import objects.Literature;
import org.bson.BsonType;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class LiteratureRepository extends AbstractMongoRepository implements ILiteratureRepository {

    private MongoCollection<Literature> literatureCollection;

    public LiteratureRepository() {
        nbd = super.getDatabase();
        nbd.drop();
        if (!collectionExists()) {
//            System.out.println("Collection does not exist");

            ValidationOptions validationOptions = new ValidationOptions()
                    .validator(Document.parse("""
                            {
                                $jsonSchema:{
                                    "bsonType": "object",
                                    "required": ["isBorrowed"],
                                    "properties": {
                                    "isBorrowed": {
                                        "bsonType": "int",
                                        "minimum": 0,
                                        "maximum": 1,
                                        "description": "Must be 0 or 1"
                                        }
                                    }
                                }
                            }
                            """))
                    .validationAction(ValidationAction.ERROR);
            CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                    .validationOptions(validationOptions);
            nbd.createCollection("literature", createCollectionOptions);
        }
        literatureCollection = getDatabase().getCollection("literature", Literature.class).withWriteConcern(WriteConcern.MAJORITY);
    }

    public boolean collectionExists() {
        return this.collectionExists("literature");
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
    public void emptyCollection() {
        literatureCollection.deleteMany(new Document());
    }

    @Override
    public MongoCollection<Literature> getLiteratureCollection() {
        return literatureCollection;
    }
}
