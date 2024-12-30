package objects;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;

import java.util.Objects;
import java.util.UUID;

@Entity(defaultKeyspace = "rent_a_literature")
@CqlName("literatures")
public class Magazine extends Literature {
    @CqlName("issue")
    private String issue;

    public Magazine() {
    }

    public Magazine(UUID literatureId, String name, int weight, int isBorrowed, String discriminator, String issue) {
        super(literatureId, name, weight, isBorrowed, discriminator);
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
