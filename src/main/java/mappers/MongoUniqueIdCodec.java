package mappers;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.UUID;

public class MongoUniqueIdCodec implements Codec<MongoUniqueId> {

    private Codec<UUID> uniqueIdCodec;

    public MongoUniqueIdCodec(CodecRegistry codecRegistry) {
        this.uniqueIdCodec = codecRegistry.get(UUID.class);
    }

    @Override
    public MongoUniqueId decode(BsonReader bsonReader, DecoderContext decoderContext) {
        UUID uniqueId = uniqueIdCodec.decode(bsonReader, decoderContext);
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
