import jakarta.persistence.*;

@Entity
@Access(AccessType.FIELD)
public class Client {
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String personalID;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;



    private int maxWeight;
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
}
