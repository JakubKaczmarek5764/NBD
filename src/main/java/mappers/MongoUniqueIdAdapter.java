package mappers;

import jakarta.json.bind.adapter.JsonbAdapter;
import org.bson.types.ObjectId;

public class MongoUniqueIdAdapter implements JsonbAdapter<MongoUniqueId, String> {


    @Override
    public String adaptToJson(MongoUniqueId id) throws Exception {
        return id != null ? id.getId().toString() : null;
    }

    @Override
    public MongoUniqueId adaptFromJson(String s) throws Exception {
        return s != null ? new MongoUniqueId( new ObjectId(s)) : null;
    }
}
