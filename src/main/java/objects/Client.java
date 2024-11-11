package objects;

import mappers.MongoUniqueId;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

//@Entity
//@Access(AccessType.FIELD)
public class Client {
    // adapter?
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @BsonProperty("_id")
    private MongoUniqueId clientId;

    @BsonProperty("firstName")
    private String firstName;

    @BsonProperty("lastName")
    private String lastName;

//    @Column(unique = true)
    @BsonProperty("personalID")
    private String personalID;

//    @Version
//    private long version;

    @BsonProperty("maxWeight")
    private int maxWeight;

    @BsonProperty("currentWeight")
    private int currentWeight;

    public int getCurrentWeight() {
        return currentWeight;
    }

    public void addCurrentWeight(int currentWeight) {
        this.currentWeight += currentWeight;
    }

    public void subtractCurrentWeight(int currentWeight) {
        this.currentWeight -= currentWeight;
    }

    public Client(String firstName, String lastName, String personalID, int maxWeight) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalID = personalID;
        this.maxWeight = maxWeight;
    }

    @BsonCreator
    public Client(
            @BsonProperty("_id") MongoUniqueId clientId,
            @BsonProperty("firstName") String firstName,
            @BsonProperty("lastName") String lastName,
            @BsonProperty("personalID") String personalID,
            @BsonProperty("maxWeight") int maxWeight
    ) {
        this.clientId = clientId;
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
        return getFirstName() + " " + getLastName() + " " + getPersonalID();
    }

    public MongoUniqueId getClientId() {
        return clientId;
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

        return new EqualsBuilder().append(clientId, client.clientId).append(maxWeight, client.maxWeight).append(currentWeight, client.currentWeight).append(firstName, client.firstName).append(lastName, client.lastName).append(personalID, client.personalID).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(clientId).append(firstName).append(lastName).append(personalID).append(maxWeight).append(currentWeight).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", clientId)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("personalID", personalID)
                .append("maxWeight", maxWeight)
                .append("currentWeight", currentWeight)
                .toString();
    }
}
