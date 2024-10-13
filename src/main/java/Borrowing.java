import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.GregorianCalendar;

@Entity
@Access(AccessType.FIELD)
public class Borrowing {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Version
    private long version;
    private GregorianCalendar BorrowingBeginDate;
    private GregorianCalendar BorrowingEndDate;
    @ManyToOne
    @JoinColumn
    @NotNull
    private Client client;

    @ManyToOne
    @JoinColumn
    @NotNull
    private Literature literature;

    public Borrowing(GregorianCalendar beginDate, GregorianCalendar endDate, Client client, Literature literature) {
        this.BorrowingBeginDate = beginDate;
        this.BorrowingEndDate = endDate;
        this.client = client;
        this.literature = literature;
    }

    public Borrowing() {

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
