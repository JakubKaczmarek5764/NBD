
import mappers.MongoUniqueId;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

//@Entity
//@Access(AccessType.FIELD)
//@DiscriminatorValue("magazine")
public class Magazine extends Literature {
    @BsonProperty("issue")
    private String issue;

    public Magazine(String name, String issue, int weight) {
        super(name, weight);
        this.issue = issue;
    }
    @BsonCreator
    public Magazine(
            @BsonProperty("id") MongoUniqueId id,
            @BsonProperty("name") String name,
            @BsonProperty("issue") String issue,
            @BsonProperty("weight") int weight
    ) {
        super(id, name, weight);
        this.issue = issue;
    }

    @Override
    String getLiteratureInfo() {
        return "id: " + getId() + " name: " + getName() + " issue: " + getIssue();
    }

    @Override
    int getTotalWeight() {
        return getWeight();
    }

    public String getIssue() {
        return issue;
    }

}
