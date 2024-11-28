package objects;

import com.mongodb.lang.NonNull;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import mappers.MongoUniqueId;
import mappers.MongoUniqueIdAdapter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.util.Objects;

//@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "type")
//@Access(AccessType.FIELD)

@BsonDiscriminator(key = "_clazz")
public abstract class Literature implements Serializable {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(name = "id", nullable = false)
    @JsonbTypeAdapter(MongoUniqueIdAdapter.class)
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


    public void setLiteratureId(@NonNull MongoUniqueId literatureId) {
        this.literatureId = literatureId;
    }

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
        this.isBorrowed = isBorrowed;
    }

    public Literature() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Literature that = (Literature) o;
        return weight == that.weight && isBorrowed == that.isBorrowed && literatureId.equals(that.literatureId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = literatureId.hashCode();
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + weight;
        result = 31 * result + isBorrowed;
        return result;
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

    public abstract int getTotalWeight();

    public int getIsBorrowed() {
        return isBorrowed;
    }

    public void setIsBorrowed(int isBorrowed) {
        this.isBorrowed = isBorrowed;
    }
}
