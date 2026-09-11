package hu9o.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Parses and formats the date-times used by {@link DeadlineTask} and
 * {@link EventTask}.
 *
 * <p>
 * Input is accepted in {@code dd/MM/yy h[mm] a} form (for example
 * {@code 12/08/26 3 PM}). Two output forms are produced: a storage form that
 * round-trips through {@link #parseString(String)}, and a friendlier display
 * form.
 */
public class DateTime {
    /** Formatter used to read user- and file-supplied date-times. */
    private static final DateTimeFormatter INPUT_FORMAT = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("d/M/yy h[mm] a")
            .toFormatter(Locale.ENGLISH);

    private DateTime() {
    }

    /**
     * Parses a date-time written in {@code dd/MM/yy h[mm] a} form.
     *
     * @param text the date-time text to parse.
     * @return the parsed date-time.
     * @throws DateTimeParseException if the text is not in the expected form.
     */
    public static LocalDateTime parseString(String text) throws DateTimeParseException {
        return LocalDateTime.parse(text, INPUT_FORMAT);
    }

    /**
     * Formats a date-time for the save file, in a form that
     * {@link #parseString(String)} can read back.
     *
     * @param dateTime the date-time to format.
     * @return the storage-form text, such as {@code 12/08/26 3 PM}.
     */
    public static String formatForStorage(LocalDateTime dateTime) {
        // Only ever called on an already-parsed field of a Deadline/Event task.
        assert dateTime != null : "formatForStorage needs a date-time to format";
        String pattern = dateTime.getMinute() > 0 ? "dd/MM/yy hmm a" : "dd/MM/yy h a";
        return dateTime.format(DateTimeFormatter.ofPattern(pattern, Locale.US));
    }

    /**
     * Formats a date-time for display to the user.
     *
     * @param dateTime the date-time to format.
     * @return the display-form text, such as {@code 12 Aug, 3 PM}.
     */
    public static String formatForDisplay(LocalDateTime dateTime) {
        // Only ever called on an already-parsed field of a Deadline/Event task.
        assert dateTime != null : "formatForDisplay needs a date-time to format";
        String pattern = dateTime.getMinute() > 0 ? "dd MMM, hmm a" : "dd MMM, h a";
        return dateTime.format(DateTimeFormatter.ofPattern(pattern, Locale.US));
    }
}
