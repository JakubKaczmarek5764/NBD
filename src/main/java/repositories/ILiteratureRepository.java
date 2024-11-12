package repositories;

import com.mongodb.client.MongoCollection;
import objects.Literature;

public interface ILiteratureRepository extends Repository<Literature> {
    public MongoCollection<Literature> getLiteratureCollection();
}
