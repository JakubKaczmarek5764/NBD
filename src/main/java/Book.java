import jakarta.persistence.*;

@Entity
@Access(AccessType.FIELD)
@DiscriminatorValue("book")
public class Book extends Literature{
    private String genre;
    private String Author;

    public Book(int maxBorrowingLength, String name, String genre, String author) {
        super(maxBorrowingLength, name);
        this.genre = genre;
        Author = author;
    }

    public Book() {

    }

    @Override
    String getLiteratureInfo() {
        return "id: "+ getLiteratureId() +" maxBorrowingLength: "+ getMaxBorrowingLength()+" name: "+getName()+" genre: "+getGenre()+" author: "+getAuthor();
    }
    public String getGenre() {
        return genre;
    }

    public String getAuthor() {
        return Author;
    }
}
