package mappers;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import java.lang.reflect.Type;
import java.util.List;

public class UniqueIdCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry codecRegistry) {
        if (clazz == MongoUniqueId.class){
            return (Codec<T>) new MongoUniqueIdCodec(codecRegistry);
        }
        return null;
    }

    // ta funkcja do dogadania
    @Override
    public <T> Codec<T> get(Class<T> clazz, List<Type> typeArguments, CodecRegistry codecRegistry) {
        if (clazz == MongoUniqueId.class){
            return (Codec<T>) new MongoUniqueIdCodec(codecRegistry);
        }
        return null;
    }
}
