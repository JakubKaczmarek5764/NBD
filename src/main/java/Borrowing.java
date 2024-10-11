import java.util.GregorianCalendar;

public class Borrowing {
    private long id;
    private GregorianCalendar BorrowingBeginDate;
    private GregorianCalendar BorrowingEndDate;
    private Client client;
    private Literature literature;

    public Borrowing(GregorianCalendar beginDate, GregorianCalendar endDate, Client client, Literature literature) {
        this.BorrowingBeginDate = beginDate;
        this.BorrowingEndDate = endDate;
        this.client = client;
        this.literature = literature;
    }

    public long getId() {
        return id;
    }

    public GregorianCalendar getBorrowingBeginDate() {
        return BorrowingBeginDate;
    }

    public GregorianCalendar getBorrowingEndDate() {
        return BorrowingEndDate;
    }

    public Client getClient() {
        return client;
    }

    public Literature getLiterature() {
        return literature;
    }

    public String getBorrowingInfo() {
        return getId() + getClient().getClientInfo()+getLiterature().getLiteratureInfo();
    }

    public void endBorrowing(GregorianCalendar endDate) {
        if(endDate.before(BorrowingBeginDate)) {
            BorrowingEndDate = BorrowingBeginDate;
        }
        else {
            BorrowingEndDate = endDate;
        }
        int BorrowingLengthInDays = (int)Math.ceil((double) (BorrowingEndDate.getTimeInMillis() -
                BorrowingBeginDate.getTimeInMillis()) / 86400000);
    }
}
