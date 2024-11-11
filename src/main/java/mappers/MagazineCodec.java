package mappers;

import objects.Literature;
import objects.Magazine;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class MagazineCodec extends LiteratureCodec<Magazine> {


    public MagazineCodec(CodecRegistry codecRegistry) {
        super(codecRegistry);
    }

    @Override
    public Magazine decode(BsonReader bsonReader, DecoderContext decoderContext) {
        bsonReader.readStartDocument();
        Literature basicInfo = decodeBasicInfo(bsonReader, decoderContext);
        String issue = bsonReader.readString("issue");
        bsonReader.readEndDocument();
        return new Magazine(basicInfo.getLiteratureId(), basicInfo.getName(), issue, basicInfo.getWeight(), basicInfo.isBorrowed());
    }

    @Override
    public void encode(BsonWriter bsonWriter, Magazine magazine, EncoderContext encoderContext) {
        encodeBasicInfo(bsonWriter, magazine, encoderContext);
        bsonWriter.writeString("issue", magazine.getIssue());
        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<Magazine> getEncoderClass() {
        return Magazine.class;
    }
}
