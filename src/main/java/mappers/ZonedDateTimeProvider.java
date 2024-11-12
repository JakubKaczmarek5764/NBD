package mappers;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import java.time.ZonedDateTime;

public class ZonedDateTimeProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry codecRegistry) {
        if (clazz == ZonedDateTime.class) {
            return (Codec<T>) new ZonedDateTimeCodec(codecRegistry);

        }
        return null;
    }
}
