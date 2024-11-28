package objects;

import mappers.MongoUniqueId;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Objects;

//@Entity
//@Access(AccessType.FIELD)
//@DiscriminatorValue("magazine")

@BsonDiscriminator(key = "_clazz", value = "magazine")
public class Magazine extends Literature {
    @BsonProperty("issue")
    private String issue;

    public Magazine() {}
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Magazine magazine = (Magazine) o;
        return Objects.equals(issue, magazine.issue);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(issue);
        return result;
    }
}
