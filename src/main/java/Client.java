import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Access(AccessType.FIELD)
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String personalID;
    @Version
    private long version;
    private int maxWeight;

    public int getCurrentWeight() {
        return currentWeight;
    }

    public void addCurrentWeight(int currentWeight) {
        this.currentWeight += currentWeight;
    }

    private int currentWeight;
    public Client(String firstName, String lastName, String personalID, int maxWeight) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalID = personalID;
        this.maxWeight = maxWeight;
    }

    public Client() {

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPersonalID() {
        return personalID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPersonalID(String personalID) {
        this.personalID = personalID;
    }
    public String getClientInfo() {
        return getFirstName() + " " + getLastName() + " "+ getPersonalID();
    }

    public Long getId() {
        return id;
    }
    public int getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(int maxWeight) {
        this.maxWeight = maxWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        return new EqualsBuilder().append(id, client.id).append(maxWeight, client.maxWeight).append(currentWeight, client.currentWeight).append(firstName, client.firstName).append(lastName, client.lastName).append(personalID, client.personalID).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(firstName).append(lastName).append(personalID).append(maxWeight).append(currentWeight).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("personalID", personalID)
                .append("maxWeight", maxWeight)
                .append("currentWeight", currentWeight)
                .toString();
    }
}
