package mappers;

import objects.Book;
import objects.Literature;
import objects.Magazine;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class LiteratureCodec implements Codec<Literature> {
    private CodecRegistry registry;
    private Codec<MongoUniqueId> uniqueIdCodec;

    public LiteratureCodec(CodecRegistry codecRegistry) {
        this.registry = codecRegistry;
        this.uniqueIdCodec = registry.get(MongoUniqueId.class);
    }


    @Override
    public Literature decode(BsonReader bsonReader, DecoderContext decoderContext) {
        bsonReader.readStartDocument();
        MongoUniqueId id = null;
        Document document = new Document();
        while (bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String fieldName = bsonReader.readName();
            if (fieldName.equals("_id")) {
                id = uniqueIdCodec.decode(bsonReader, decoderContext);
                document.put(fieldName, id);
            } else {
                document.put(fieldName, readValue(bsonReader));

            }
        }
        Literature literature = null;
        bsonReader.readEndDocument();
        String clazz = document.getString("_clazz");
        if ("book".equalsIgnoreCase(clazz)) {
            literature = new Book(
                    id,
                    document.getString("name"),
                    document.getString("genre"),
                    document.getString("author"),
                    document.getInteger("weight"),
                    document.getInteger("tier"),
                    document.getInteger("isBorrowed")
            );
        } else if ("magazine".equalsIgnoreCase(clazz)) {
            literature = new Magazine(
                    id,
                    document.getString("name"),
                    document.getString("issue"),
                    document.getInteger("weight"),
                    document.getInteger("isBorrowed")
            );
        } else {
            throw new IllegalArgumentException("Unknown class " + clazz);
        }
        return literature;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Literature literature, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeString("_clazz", literature.getClass().getSimpleName());
        bsonWriter.writeString("name", literature.getName());
        bsonWriter.writeInt32("weight", literature.getWeight());
        bsonWriter.writeInt32("isBorrowed", literature.getIsBorrowed());
        if (literature instanceof Book) {
            bsonWriter.writeString("author", ((Book) literature).getAuthor());
            bsonWriter.writeInt32("tier", ((Book) literature).getTier());
            bsonWriter.writeString("genre", ((Book) literature).getGenre());
        } else if (literature instanceof Magazine) {
            bsonWriter.writeString("issue", ((Magazine) literature).getIssue());
        } else {
            throw new IllegalArgumentException("Unknown class " + literature.getClass());
        }

        bsonWriter.writeName("_id");
        uniqueIdCodec.encode(bsonWriter, literature.getLiteratureId(), encoderContext);
        bsonWriter.writeEndDocument();

    }

    @Override
    public Class<Literature> getEncoderClass() {
        return Literature.class;
    }

    private Object readValue(BsonReader bsonReader) {
        switch (bsonReader.getCurrentBsonType()) {
            case STRING:
                return bsonReader.readString();
            case INT32:
                return bsonReader.readInt32();
            default:
                throw new IllegalArgumentException("Unknown type " + bsonReader.getCurrentBsonType());
        }
    }
}
