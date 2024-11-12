package objects;

import com.mongodb.lang.NonNull;
import mappers.MongoUniqueId;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Objects;

//@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "type")
//@Access(AccessType.FIELD)

@BsonDiscriminator(key = "_clazz")
public abstract class Literature {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(name = "id", nullable = false)
    @BsonProperty("_id")
    @NonNull
    private MongoUniqueId literatureId;

//    @Version
//    private long version;

    @BsonProperty("name")
    private String name;

    @BsonProperty("weight")
    private int weight;


    // z tym borrowed trzeba pokminic, najlepiej chyba zrobic ta inkrementacje
    @BsonProperty("isBorrowed")
    private int isBorrowed;

//    public int isBorrowed() {
//        return isBorrowed;
//    }
//
//    public void setBorrowed(int borrowed) {
//        isBorrowed = borrowed;
//    }



    public Literature(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }
    @BsonCreator
    public Literature(
            @BsonProperty("_id") MongoUniqueId literatureId,
            @BsonProperty("name") String name,
            @BsonProperty("weight") int weight,
            @BsonProperty("isBorrowed") int isBorrowed
    ) {
        this.literatureId = literatureId;
        this.name = name;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Literature that = (Literature) o;
        return weight == that.weight && isBorrowed == that.isBorrowed && Objects.equals(literatureId, that.literatureId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(literatureId, name, weight, isBorrowed);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", literatureId)
                .append("name", name)
                .append("weight", weight)
                .append("isBorrowed", isBorrowed)
                .toString();
    }


    @NonNull
    public MongoUniqueId getLiteratureId() {
        return literatureId;
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

    public int getIsBorrowed() {
        return isBorrowed;
    }

    public void setIsBorrowed(int isBorrowed) {
        this.isBorrowed = isBorrowed;
    }
}
