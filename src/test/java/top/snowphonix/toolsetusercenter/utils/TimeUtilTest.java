package top.snowphonix.toolsetusercenter.utils;


import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TimeUtilTest {

    @Test
    void localDateTimeSame() {
        LocalDateTime now = LocalDateTime.now();
        assertEquals(now, TimeUtil.timestampToLocalDateTime(TimeUtil.localDateTimeToTimestamp(now)));
    }
}
