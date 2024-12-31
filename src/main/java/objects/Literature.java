package objects;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import com.mongodb.lang.NonNull;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import mappers.MongoUniqueId;
import mappers.MongoUniqueIdAdapter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity(defaultKeyspace = "rent_a_literature")
@CqlName("literatures")
@PropertyStrategy(mutable = false)
public abstract class Literature implements Serializable {

    @PartitionKey
    @CqlName("literature_id")
    private UUID literatureId;

    @CqlName("name")
    private String name;

    @CqlName("weight")
    private int weight;

    // z tym borrowed trzeba pokminic, najlepiej chyba zrobic ta inkrementacje
    @CqlName("isBorrowed")
    private int isBorrowed;

    @CqlName("discriminator")
    private String discriminator;

    public Literature(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public Literature(UUID literatureId, String name, int weight, int isBorrowed, String discriminator) {
        this.literatureId = literatureId;
        this.name = name;
        this.weight = weight;
        this.isBorrowed = isBorrowed;
        this.discriminator = discriminator;
    }

    public Literature() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Literature that)) return false;

        return new EqualsBuilder().append(getWeight(), that.getWeight()).append(getIsBorrowed(), that.getIsBorrowed()).append(getLiteratureId(), that.getLiteratureId()).append(getName(), that.getName()).append(discriminator, that.discriminator).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getLiteratureId()).append(getName()).append(getWeight()).append(getIsBorrowed()).append(discriminator).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("literatureId", literatureId)
                .append("name", name)
                .append("weight", weight)
                .append("isBorrowed", isBorrowed)
                .append("discriminator", discriminator)
                .toString();
    }

    public UUID getLiteratureId() {
        return literatureId;
    }

    public void setLiteratureId(UUID literatureId) {
        this.literatureId = literatureId;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
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
