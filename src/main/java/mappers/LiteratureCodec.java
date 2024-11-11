package mappers;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import objects.Literature;

public class LiteratureCodec implements Codec<Literature> {
    private CodecRegistry registry;
    private Codec<MongoUniqueId> uniqueIdCodec;

    public LiteratureCodec(CodecRegistry codecRegistry){
        this.registry = codecRegistry;
        this.uniqueIdCodec = registry.get(MongoUniqueId.class);
    }

    @Override
    public Literature decode(BsonReader bsonReader, DecoderContext decoderContext) {
        return null;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Literature literature, EncoderContext encoderContext) {

    }

    @Override
    public Class<Literature> getEncoderClass() {
        return null;
    }
}
