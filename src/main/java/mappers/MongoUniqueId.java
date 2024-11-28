package mappers;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.types.ObjectId;

import java.util.Objects;


public class MongoUniqueId {
    private ObjectId id;
    public MongoUniqueId(ObjectId id){
        this.id = id;
    }
    public ObjectId getId(){
        return id;
    }
    public MongoUniqueId(){}
    @Override
    public String toString() {
        return id.toString();
}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MongoUniqueId that = (MongoUniqueId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}