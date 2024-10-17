import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("magazine")
public class Magazine extends Literature {
    private String issue;

    public Magazine(String name, String issue, int weight) {
        super(name, weight);
        this.issue = issue;
    }

    public Magazine() {

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
