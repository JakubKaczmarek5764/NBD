public class Book extends Literature{
    private String genre;
    private String Author;

    public Book(double basePenalty, int maxBorrowingLength, String name, String genre, String author) {
        super(basePenalty, maxBorrowingLength, name);
        this.genre = genre;
        Author = author;
    }

    @Override
    String getLiteratureInfo() {
        return "id: "+ getId() +" maxBorrowingLength: "+ getMaxBorrowingLength()+" name: "+getName()+" genre: "+getGenre()+" author: "+getAuthor()+" penaltyPrice: "+ getPenaltyPrice();
    }

    @Override
    double getPenaltyPrice() {
        return getBasePenalty()*2.0;
    }

    public String getGenre() {
        return genre;
    }

    public String getAuthor() {
        return Author;
    }
}
