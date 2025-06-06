package Utilities;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class EpochTimeToDateTime {
    public static String epochTimeToDateTime(long epochTimeInMillis, String timeZoneId) {
        // Convert epoch time in milliseconds to LocalDateTime in the specified timezone
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochTimeInMillis), ZoneId.of(timeZoneId));

        // Define the desired date-time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // Format the LocalDateTime using the defined format
        return dateTime.format(formatter);
    }

}
