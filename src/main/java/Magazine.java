public class Magazine extends Literature{
    private String issue;

    public Magazine(double basePenalty, int maxBorrowingLength, String name, String issue) {
        super(basePenalty, maxBorrowingLength, name);
        this.issue = issue;
    }

    @Override
    String getLiteratureInfo() {
        return "id: "+ getId() +" maxBorrowingLength: "+ getMaxBorrowingLength()+" name: "+getName()+" issue: "+getIssue()+" penaltyPrice: "+ getPenaltyPrice();
    }

    @Override
    double getPenaltyPrice() {
        return 0;
    }

    public String getIssue() {
        return issue;
    }
}
