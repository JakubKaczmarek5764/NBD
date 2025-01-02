package objects;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

@Entity(defaultKeyspace = "rent_a_literature")
@CqlName("clients")
@PropertyStrategy(mutable = false)
public class Client {

    @PartitionKey
    private UUID clientId;

    @CqlName("first_name")
    private String firstName;

    @CqlName("last_name")
    private String lastName;

    @CqlName("personal_id")
    private String personalID;

    @CqlName("max_weight")
    private int maxWeight;

    @CqlName("current_weight")
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

    public Client(
            UUID clientId,
            String firstName,
            String lastName,
            String personalID,
            int maxWeight,
            int currentWeight
    ) {
        this.clientId = clientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalID = personalID;
        this.maxWeight = maxWeight;
        this.currentWeight = currentWeight;
    }
    public Client(int currentWeight, java.lang.String firstName, java.lang.String lastName, java.lang.String personalID, int maxWeight) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalID = personalID;
        this.maxWeight = maxWeight;
        this.currentWeight = currentWeight;
    }
    public Client() {
    }

    public UUID getClientId() {
        return clientId;
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

    public String generateClientInfo() {
        return getFirstName() + " " + getLastName() + " " + getPersonalID();
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
