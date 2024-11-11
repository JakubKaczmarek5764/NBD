package mappers;

import objects.Book;
import objects.Literature;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class BookCodec extends LiteratureCodec<Book>{

    public BookCodec(CodecRegistry codecRegistry) {
        super(codecRegistry);
    }

    @Override
    public Book decode(BsonReader bsonReader, DecoderContext decoderContext) {
        // w decode i encode czuje ze trzeba pamietac o takiej samej kolejnosci parametrow
        bsonReader.readStartDocument();
        Literature basicInfo = decodeBasicInfo(bsonReader, decoderContext);
        String genre = bsonReader.readString("genre");
        String author = bsonReader.readString("author");
        int tier = bsonReader.readInt32("tier");
        bsonReader.readEndDocument();
        return new Book(basicInfo.getId(), basicInfo.getName(), genre, author, basicInfo.getWeight(), tier, basicInfo.isBorrowed());
    }

    @Override
    public void encode(BsonWriter bsonWriter, Book book, EncoderContext encoderContext) {
        encodeBasicInfo(bsonWriter, book, encoderContext);
        bsonWriter.writeString("genre");
        bsonWriter.writeString("author");
        bsonWriter.writeString("tier");
        bsonWriter.writeEndDocument();
    }


    @Override
    public Class<Book> getEncoderClass() {
        return Book.class;
    }
}
