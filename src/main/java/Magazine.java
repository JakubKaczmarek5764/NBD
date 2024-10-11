public class Magazine extends Literature{
    private String issue;

    public Magazine(int maxBorrowingLength, String name, String issue) {
        super(maxBorrowingLength, name);
        this.issue = issue;
    }
    @Override
    String getLiteratureInfo() {
        return "id: "+ getId() +" maxBorrowingLength: "+ getMaxBorrowingLength()+" name: "+getName()+" issue: "+getIssue();
    }

    public String getIssue() {
        return issue;
    }
}
