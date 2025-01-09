package objects;

import com.datastax.oss.driver.api.mapper.annotations.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity(defaultKeyspace = "rent_a_literature")
@CqlName("borrowings")
@PropertyStrategy(mutable = false)  // to znaczy ze obiekt jest niemodyfikowalny, nw czy powinien byc
public class Borrowing {

    @PartitionKey
    @CqlName("borrowing_id")
    private UUID borrowingId;

    @ClusteringColumn
    @CqlName("begin_date")
    private ZonedDateTime beginDate;

      // chyba
    @CqlName("end_date")
    private ZonedDateTime endDate;

    @CqlName("client")
    private Client client;

    @CqlName("literature")
    private Literature literature;

    public Borrowing(ZonedDateTime beginDate, ZonedDateTime endDate, Client client, Literature literature) {
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.client = client;
        this.literature = literature;
    }

    public Borrowing(UUID borrowingId, ZonedDateTime beginDate, ZonedDateTime endDate, Client client, Literature literature) {
        this.borrowingId = borrowingId;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.client = client;
        this.literature = literature;
    }

    public void setEndDate(ZonedDateTime endDate) {
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

    public UUID getBorrowingId() {
        return borrowingId;
    }

    public ZonedDateTime getBeginDate() {
        return beginDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public Client getClient() {
        return client;
    }

    public Literature getLiterature() {
        return literature;
    }

    public String generateBorrowingInfo() {
        return getBorrowingId() + getClient().generateClientInfo() + getLiterature().generateLiteratureInfo();
    }

    public void endBorrowing(ZonedDateTime endDate) {
        if (endDate.isBefore(beginDate)) {
            this.endDate = beginDate;
        } else {
            this.endDate = endDate;
        }
    }
}
