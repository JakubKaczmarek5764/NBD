package mappers;

import objects.Literature;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class LiteratureCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry codecRegistry) {
        if (clazz == Literature.class) {
            return (Codec<T>) new LiteratureCodec(codecRegistry);
        }
        return null;
    }
}
