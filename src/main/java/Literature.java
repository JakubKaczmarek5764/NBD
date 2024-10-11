public abstract class Literature {
    private long id;
    private int maxBorrowingLength;
    private String name;

    public Literature(int maxBorrowingLength, String name) {
        this.maxBorrowingLength = maxBorrowingLength;
        this.name = name;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
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
}
