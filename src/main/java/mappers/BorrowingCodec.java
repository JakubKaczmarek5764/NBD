package mappers;

import objects.Borrowing;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class BorrowingCodec implements Codec<Borrowing> {

    @Override
    public Borrowing decode(BsonReader bsonReader, DecoderContext decoderContext) {
        return null;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Borrowing borrowing, EncoderContext encoderContext) {

    }

    @Override
    public Class<Borrowing> getEncoderClass() {
        return null;
    }
}
