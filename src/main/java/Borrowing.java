
import com.mongodb.lang.NonNull;
import mappers.MongoUniqueId;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.GregorianCalendar;

//@Entity
//@Access(AccessType.FIELD)
public class Borrowing {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @BsonProperty("_id")
    @NonNull
    private MongoUniqueId borrowingId;
//    @Version
//    private long version;

    @BsonProperty("beginDate")
    private GregorianCalendar beginDate;

    @BsonProperty("endDate")
    private GregorianCalendar endDate;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn
//    @NotNull
    @BsonProperty("client")
    private Client client;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn
//    @NotNull
    @BsonProperty("literature")
    private Literature literature;

    public Borrowing(GregorianCalendar beginDate, GregorianCalendar endDate, Client client, Literature literature) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.client = client;
        this.literature = literature;
    }

    @BsonCreator
    public Borrowing(
            @BsonProperty("_id") MongoUniqueId borrowingId,
            @BsonProperty("beginDate") GregorianCalendar beginDate,
            @BsonProperty("endDate") GregorianCalendar endDate,
            @BsonProperty("client") Client client,
            @BsonProperty("literature") Literature literature
    ) {
        this.borrowingId = borrowingId;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.client = client;
        this.literature = literature;
    }


    public void setEndDate(GregorianCalendar endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", borrowingId)
                .append("BorrowingBeginDate", beginDate)
                .append("BorrowingEndDate", endDate)
                .append("client", client)
                .append("literature", literature)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Borrowing borrowing = (Borrowing) o;

        return new EqualsBuilder().append(borrowingId, borrowing.borrowingId).append(beginDate, borrowing.beginDate).append(endDate, borrowing.endDate).append(client, borrowing.client).append(literature, borrowing.literature).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(borrowingId).append(beginDate).append(endDate).append(client).append(literature).toHashCode();
    }

    public MongoUniqueId getBorrowingId() {
        return borrowingId;
    }

    public GregorianCalendar getBeginDate() {
        return beginDate;
    }

    public GregorianCalendar getEndDate() {
        return endDate;
    }

    public Client getClient() {
        return client;
    }

    public Literature getLiterature() {
        return literature;
    }

    public String getBorrowingInfo() {
        return getBorrowingId() + getClient().getClientInfo() + getLiterature().getLiteratureInfo();
    }

    public void endBorrowing(GregorianCalendar endDate) {
        if (endDate.before(beginDate)) {
            this.endDate = beginDate;
        } else {
            this.endDate = endDate;
        }
    }
}
