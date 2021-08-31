package top.snowphonix.toolsetusercenter.utils;

import org.jose4j.jwt.NumericDate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    private static final ZoneOffset ZONE_OFFSET = ZoneOffset.ofHours(8);

    public static long localDateTimeToTimestamp(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZONE_OFFSET).toEpochMilli();
    }

    public static LocalDateTime earliest(LocalDateTime first, LocalDateTime second) {
        if (first.isBefore(second)) {
            return first;
        }
        return second;
    }

    public static NumericDate localDateTimeToNumericDate(LocalDateTime localDateTime) {
        long timestamp = localDateTimeToTimestamp(localDateTime);
        return NumericDate.fromMilliseconds(timestamp);
    }

    public static LocalDateTime timestampToLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZONE_OFFSET);
    }

    public static LocalDateTime numericDateToLocalDateTime(NumericDate numericDate) {
        long timestamp = numericDate.getValueInMillis();
        return timestampToLocalDateTime(timestamp);
    }

    public static boolean localDateTimeSameSecond(LocalDateTime left, LocalDateTime right) {
        long leftSecond = localDateTimeToTimestamp(left) / 1000;
        long rightSecond = localDateTimeToTimestamp(right) / 1000;
        return leftSecond == rightSecond;
    }

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String localDAteTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(dateTimeFormatter);
    }
}
