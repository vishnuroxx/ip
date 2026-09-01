package hu9o.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link EventTask}'s two non-trivial methods, {@code compressionString}
 * (save-file record) and {@code specificationSuffix} (the "(from: ... to: ...)"
 * text), plus the date parsing done in its constructor.
 */
public class EventTaskTest {

    @Test
    public void compressionString_onTheHourTimes_appendsBothDatesInStorageFormat() {
        EventTask event = new EventTask("project meeting", "12/08/26 2 PM", "12/08/26 4 PM");

        assertEquals("E| |project meeting|12/08/26 2 PM|12/08/26 4 PM|", event.compressionString());
    }

    @Test
    public void specificationSuffix_spanningTwoMonths_rendersFromAndToInDisplayFormat() {
        EventTask event = new EventTask("project meeting", "12/08/26 2 PM", "06/09/26 4 PM");

        assertEquals(" (from: 12 Aug, 2 PM to: 06 Sep, 4 PM)", event.specificationSuffix());
    }

    @Test
    public void constructor_unparseableTime_throwsDateTimeParseException() {
        assertThrows(DateTimeParseException.class, () ->
                new EventTask("meeting", "next tuesday", "later"));
    }
}
