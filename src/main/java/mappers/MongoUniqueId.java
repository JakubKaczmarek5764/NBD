package mappers;

import java.util.UUID;


public class MongoUniqueId {
    private UUID id;

    public MongoUniqueId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public MongoUniqueId() {
    }

    @Override
    public String toString() {
        return id.toString();
    }
}