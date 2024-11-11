package mappers;

import objects.Book;
import objects.Magazine;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class LiteratureCodecProvider implements CodecProvider {

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry codecRegistry) {
        if (clazz.equals(Book.class)){
            return (Codec<T>) new BookCodec(codecRegistry);
        }
        else if (clazz.equals(Magazine.class)){
            return (Codec<T>) new MagazineCodec(codecRegistry);
        }
    }
}
