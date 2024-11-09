package mappers;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

public class MongoUniqueId {
    private UUID id;
    public MongoUniqueId(UUID id){
        this.id = id;
    }
    public UUID getId(){
        return id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .toString();
    }
}
