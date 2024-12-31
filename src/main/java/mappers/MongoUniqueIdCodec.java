package mappers;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;


public class MongoUniqueIdCodec implements Codec<MongoUniqueId> {

    private Codec<ObjectId> uniqueIdCodec;

    public MongoUniqueIdCodec(CodecRegistry codecRegistry) {
        this.uniqueIdCodec = codecRegistry.get(ObjectId.class);
    }

    @Override
    public MongoUniqueId decode(BsonReader bsonReader, DecoderContext decoderContext) {
        ObjectId uniqueId = uniqueIdCodec.decode(bsonReader, decoderContext);
        return new MongoUniqueId(uniqueId);
    }

    @Override
    public void encode(BsonWriter bsonWriter, MongoUniqueId mongoUniqueId, EncoderContext encoderContext) {
        uniqueIdCodec.encode(bsonWriter, mongoUniqueId.getId(), encoderContext);
    }

    @Override
    public Class<MongoUniqueId> getEncoderClass() {
        return MongoUniqueId.class;
    }
}
