import mappers.MongoUniqueId;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Objects;

//@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "type")
//@Access(AccessType.FIELD)

public abstract class Literature {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(name = "id", nullable = false)
    // nie wiem jeszcze jak sie robi unique aby
    @BsonProperty("id")
    private MongoUniqueId id;

//    @Version
//    private long version;

    @BsonProperty("name")
    private String name;

    @BsonProperty("weight")
    private int weight;

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    private boolean isBorrowed;

    public Literature(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }
    @BsonCreator
    public Literature(
            @BsonProperty("id") MongoUniqueId id,
            @BsonProperty("name") String name,
            @BsonProperty("weight") int weight

    ) {
        this.id = id;
        this.name = name;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Literature that = (Literature) o;
        return weight == that.weight && isBorrowed == that.isBorrowed && Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, weight, isBorrowed);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("weight", weight)
                .append("isBorrowed", isBorrowed)
                .toString();
    }




    public MongoUniqueId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    abstract String getLiteratureInfo();

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    abstract int getTotalWeight();
}
