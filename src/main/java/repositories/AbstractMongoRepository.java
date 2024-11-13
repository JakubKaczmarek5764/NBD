package repositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ValidationOptions;
import mappers.LiteratureCodecProvider;
import mappers.UniqueIdCodecProvider;
import mappers.ZonedDateTimeProvider;
import org.bson.BsonType;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import java.util.List;

public abstract class AbstractMongoRepository implements AutoCloseable {
    private static final ConnectionString connectionString = new ConnectionString(
            "mongodb://mongodb1:27017,mongodb2:27018,mongodb3:27019/?replicaSet=replica_set_single");

    private final MongoCredential credential = MongoCredential.createCredential(
            "admin", "admin", "adminpassword".toCharArray());

    private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
            PojoCodecProvider.builder()
                .automatic(true)
                .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                .build());


    protected MongoClient mongoClient;
    protected MongoDatabase nbd;

    // to bylo void, ale chyba lepiej jak zwraca
    public MongoDatabase initDbConnection() {
        if (mongoClient != null && nbd != null) {
            return nbd;
        }
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        CodecRegistries.fromProviders(new UniqueIdCodecProvider()), // tbc
                        CodecRegistries.fromProviders(new LiteratureCodecProvider()),
                        CodecRegistries.fromProviders(new ZonedDateTimeProvider()),
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                ))
                .build();
        mongoClient = MongoClients.create(settings);
        nbd = mongoClient.getDatabase("nbd");
        return nbd;
    }
    public Document getValidationOptions( String collectionName) {
        // Define the command to retrieve collection options, including validation rules
        MongoDatabase database = this.nbd;
        Document command = new Document("listCollections", 1)
                .append("filter", new Document("name", collectionName));

        // Execute the command and retrieve the result
        Document result = database.runCommand(command);

        // Extract the collection options
        Document collectionOptions = result.get("cursor", Document.class)
                .getList("firstBatch", Document.class).get(0)
                .get("options", Document.class);

        // Retrieve validation options specifically
        Document validationOptions = new Document();
        if (collectionOptions != null) {
            validationOptions.put("validator", collectionOptions.get("validator"));
            validationOptions.put("validationLevel", collectionOptions.get("validationLevel"));
            validationOptions.put("validationAction", collectionOptions.get("validationAction"));
        }

        return validationOptions;
    }
    public void close(){
        mongoClient.close();
    }
}
