package hu9o.task;

import java.util.Locale;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

public class DateTime {
    private static DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("dd/MM/yy h[mm] a")
            .toFormatter(Locale.ENGLISH);

    private DateTime() {
    }

    public static LocalDateTime parseString(String str) throws DateTimeParseException {
        return LocalDateTime.parse(str, formatter);
    }

    public static String dateToString(LocalDateTime obj) {
        String toStringFormat = obj.getMinute() > 0 ? "dd/MM/yy hmm a" : "dd/MM/yy h a";
        return obj.format(DateTimeFormatter.ofPattern(toStringFormat, Locale.US));
    }

    public static String dateToDisplay(LocalDateTime obj) {
        String toStringFormat = obj.getMinute() > 0 ? "dd MMM, hmm a" : "dd MMM, h a";
        return obj.format(DateTimeFormatter.ofPattern(toStringFormat, Locale.US));
    }

}
