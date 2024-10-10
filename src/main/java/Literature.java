public abstract class Literature {
    private long id;
    private double basePenalty;
    private int maxBorrowingLength;
    private String name;

    public Literature(double basePenalty, int maxBorrowingLength, String name) {
        this.basePenalty = basePenalty;
        this.maxBorrowingLength = maxBorrowingLength;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getBasePenalty() {
        return basePenalty;
    }

    public void setBasePenalty(double basePenalty) {
        this.basePenalty = basePenalty;
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


    abstract String getLiteratureInfo();

    abstract double getPenaltyPrice();
}
