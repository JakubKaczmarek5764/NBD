import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Access(AccessType.FIELD)
public abstract class Literature {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private long id;
    private int maxBorrowingLength;
    private String name;
    @Column(unique = true)
    private String literatureId;

    public Literature(int maxBorrowingLength, String name) {
        this.maxBorrowingLength = maxBorrowingLength;
        this.name = name;
    }

    public Literature() {

    }

    public long getId() {
        return id;
    }

    public int getMaxBorrowingLength() {
        return maxBorrowingLength;
    }

    public void setMaxBorrowingLength(int maxBorrowingLength) {
        this.maxBorrowingLength = maxBorrowingLength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLiteratureId() {
        return literatureId;
    }

    public void setLiteratureId(String literatureId) {
        this.literatureId = literatureId;
    }

    abstract String getLiteratureInfo();
}
