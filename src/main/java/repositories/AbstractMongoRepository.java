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

    public void close(){
        mongoClient.close();
    }
}
