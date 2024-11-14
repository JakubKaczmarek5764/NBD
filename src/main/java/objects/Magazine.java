package objects;

import mappers.MongoUniqueId;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

//@Entity
//@Access(AccessType.FIELD)
//@DiscriminatorValue("magazine")

@BsonDiscriminator(key = "_clazz", value = "magazine")
public class Magazine extends Literature {
    @BsonProperty("issue")
    private String issue;

    public Magazine(String name, String issue, int weight) {
        super(name, weight);
        this.issue = issue;
    }

    @BsonCreator
    public Magazine(
            @BsonProperty("_id") MongoUniqueId literatureId,
            @BsonProperty("name") String name,
            @BsonProperty("issue") String issue,
            @BsonProperty("weight") int weight,
            @BsonProperty("isBorrowed") int isBorrowed
    ) {
        super(literatureId, name, weight, isBorrowed);
        this.issue = issue;
    }

    @Override
    String getLiteratureInfo() {
        return "id: " + getLiteratureId() + " name: " + getName() + " issue: " + getIssue();
    }

    @Override
    public int getTotalWeight() {
        return getWeight();
    }

    public String getIssue() {
        return issue;
    }

}
