public class Client {
    private String firstName;
    private String lastName;
    private String personalID;
    private double penalty;

    public Client(String firstName, String lastName, String personalID) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalID = personalID;
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

    public double getPenalty() {
        return penalty;
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

    public void setPenalty(double penalty) {
        this.penalty = penalty;
    }

    public String getClientInfo() {
        return getFirstName() + " " + getLastName() + " "+ getPersonalID() + " " + getPenalty();
    }

    void addPenalty(int days, double penaltyPerDay) {
        setPenalty(getPenalty()+days*penaltyPerDay);
    }
}
