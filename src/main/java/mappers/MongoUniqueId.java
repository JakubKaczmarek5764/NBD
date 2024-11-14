package mappers;

import org.bson.types.ObjectId;


public class MongoUniqueId {
    private ObjectId id;

    public MongoUniqueId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getId() {
        return id;
    }

    public MongoUniqueId() {
    }

    @Override
    public String toString() {
        return id.toString();
    }
}