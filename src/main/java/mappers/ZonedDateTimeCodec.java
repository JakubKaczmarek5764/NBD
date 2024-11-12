package mappers;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;


import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ZonedDateTimeCodec implements Codec<ZonedDateTime> {
    private CodecRegistry codecRegistry;

    public ZonedDateTimeCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public ZonedDateTime decode(BsonReader bsonReader, DecoderContext decoderContext) {
        bsonReader.readStartDocument();
        long time = bsonReader.readDateTime("time");
        String zone = bsonReader.readString("zone");
        bsonReader.readEndDocument();
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.of(zone));
    }

    @Override
    public void encode(BsonWriter bsonWriter, ZonedDateTime zonedDateTime, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeDateTime("time", zonedDateTime.toInstant().toEpochMilli());
        bsonWriter.writeString("zone", zonedDateTime.getZone().getId());
        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<ZonedDateTime> getEncoderClass() {
        return ZonedDateTime.class;
    }
}
