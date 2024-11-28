package objects;

import mappers.MongoUniqueId;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

//@Entity
//@Access(AccessType.FIELD)
//@DiscriminatorValue("book")

@BsonDiscriminator(key = "_clazz", value = "book")
public class Book extends Literature {
    @BsonProperty("genre")
    private String genre;

    @BsonProperty("author")
    private String author;

    @BsonProperty("tier")
    private int tier;

    public Book() {
    }

    public Book(String name, String genre, String author, int weight, int tier) {
        super(name, weight);
        this.genre = genre;
        this.author = author;
        this.tier = tier;

    }

    @BsonCreator
    public Book(@BsonProperty("_id") MongoUniqueId id,
                @BsonProperty("name") String name,
                @BsonProperty("genre") String genre,
                @BsonProperty("author") String author,
                @BsonProperty("weight") int weight,
                @BsonProperty("tier") int tier,
                @BsonProperty("isBorrowed") int isBorrowed
    ) {
        super(id, name, weight, isBorrowed);
        this.genre = genre;
        this.author = author;
        this.tier = tier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return new EqualsBuilder().appendSuper(super.equals(o)).append(tier, book.tier).append(genre, book.genre).append(author, book.author).isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString())
                .append("genre", genre)
                .append("author", author)
                .append("tier", tier)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(genre).append(author).append(tier).toHashCode();
    }

    @Override
    String getLiteratureInfo() {
        return "id: " + getLiteratureId() + " name: " + getName() + " genre: " + getGenre() + " author: " + getAuthor();
    }

    @Override
    public int getTotalWeight() {
        return getWeight() * getTier();
    }

    public String getGenre() {
        return genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getTier() {
        return tier;
    }

    public void setTier(int tier) {
        this.tier = tier;
    }

}
