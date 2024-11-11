package mappers;

import objects.Magazine;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import objects.Literature;

public abstract class LiteratureCodec<T extends Literature> implements Codec<T> {
    private CodecRegistry registry;
    private Codec<MongoUniqueId> uniqueIdCodec;

    public LiteratureCodec(CodecRegistry codecRegistry){
        this.registry = codecRegistry;
        this.uniqueIdCodec = registry.get(MongoUniqueId.class);
    }

    public Literature decodeBasicInfo(BsonReader bsonReader, DecoderContext decoderContext) {
        MongoUniqueId id = uniqueIdCodec.decode(bsonReader, decoderContext);
        String name = bsonReader.readString("name");
        int weight = bsonReader.readInt32("weight");
        int isBorrowed = bsonReader.readInt32("isBorrowed");
        // mogloby tu byc book, potrzebujemy tylko przekazac dane do innego codeca
        return new Magazine(id, name, "", weight, isBorrowed);
    }

    public void encodeBasicInfo(BsonWriter bsonWriter, Literature literature, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        uniqueIdCodec.encode(bsonWriter, literature.getId(), encoderContext);
        bsonWriter.writeString("name", literature.getName());
        bsonWriter.writeInt32("weight", literature.getWeight());
        bsonWriter.writeInt32("isBorrowed", literature.isBorrowed());
    }
}
