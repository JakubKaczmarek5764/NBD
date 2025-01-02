package providers;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeConverter {
    public static Long toCassandraValue(ZonedDateTime zonedDateTime) {
        return zonedDateTime.toInstant().toEpochMilli();
    }

    public static ZonedDateTime fromCassandraValue(Long value) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault());
    }
}