import jakarta.persistence.*;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("book")
public class Book extends Literature{
    private String genre;
    private String author;
    private int tier;
    public Book(int maxBorrowingLength, String name, String genre, String author, int weight, int tier) {
        super(maxBorrowingLength, name, weight);
        this.genre = genre;
        this.author = author;
        this.tier = tier;

    }

    public Book() {

    }

    @Override
    String getLiteratureInfo() {
        return "id: "+ getLiteratureId() +" maxBorrowingLength: "+ getMaxBorrowingLength()+" name: "+getName()+" genre: "+getGenre()+" author: "+getAuthor();
    }

    @Override
    int getTotalWeight() {
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
