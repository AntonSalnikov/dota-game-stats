package gg.bayes.challenge.util;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneId;


/**
 * Custom time provider used to fix Clock time for testing purposes.
 * It is an ugly solution caused by absence of DateTimeUtils, which was present in Joda Time.
 * Nice article about
 * @see  <a href="http://blog.freeside.co/2015/01/15/fixing-current-time-for-tests-with-java-8-s-date-time-api/">FIXING CURRENT TIME FOR TESTS WITH JAVA 8'S DATE/TIME API</a>
 *
 * Be aware that it is not thread safe and cound not be used for parallel test running.
 *
 * @author anton.salnikov
 */
public class OffsetDateTimeProvider {
    private static Clock clock = Clock.systemDefaultZone();

    private OffsetDateTimeProvider() {
    }

    public static OffsetDateTime now() {
        return OffsetDateTime.now(clock);
    }

    public static void useFixedClockAt(OffsetDateTime date, ZoneId zoneId) {
        clock = Clock.fixed(date.toInstant(), zoneId);
    }

    public static void cleanUpFixedTime() {
        clock = Clock.systemDefaultZone();
    }
}
