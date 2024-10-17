import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.validator.internal.util.stereotypes.Lazy;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @NotNull
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("version", version)
                .append("BorrowingBeginDate", BorrowingBeginDate)
                .append("BorrowingEndDate", BorrowingEndDate)
                .append("client", client)
                .append("literature", literature)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Borrowing borrowing = (Borrowing) o;

        return new EqualsBuilder().append(id, borrowing.id).append(BorrowingBeginDate, borrowing.BorrowingBeginDate).append(BorrowingEndDate, borrowing.BorrowingEndDate).append(client, borrowing.client).append(literature, borrowing.literature).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(BorrowingBeginDate).append(BorrowingEndDate).append(client).append(literature).toHashCode();
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
    }
}
