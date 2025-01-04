package providers;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeConverter {
    public static Long toCassandraValue(ZonedDateTime zonedDateTime) {
        if (zonedDateTime == null) {
            return 0L;
        }
        return zonedDateTime.toInstant().toEpochMilli();
    }

    public static ZonedDateTime fromCassandraValue(Timestamp value) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(value.getTime()), ZoneId.systemDefault());
    }
}