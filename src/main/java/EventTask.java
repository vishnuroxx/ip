import java.time.format.DateTimeParseException;
import java.time.LocalDateTime;

/**
 * Represents a task that takes place during a specified time.
 */
public class EventTask extends Task {
    private final LocalDateTime fromSpecification;
    private final LocalDateTime toSpecification;

    /**
     * Creates an unfinished event task.
     *
     * @param description   the event's description
     * @param specification the event time, such as "Aug 6th 2pm to: 4pm"
     */
    public EventTask(String description, String fromSpecification, String toSpecification)
            throws DateTimeParseException {
        super(description, "E");
        this.fromSpecification = DateTime.parseString(fromSpecification);
        this.toSpecification = DateTime.parseString(toSpecification);
    }

    @Override
    public String compressionString() {
        return super.compressionString() + DateTime.dateToString(fromSpecification) + "|"
                + DateTime.dateToString(toSpecification) + "|";
    }

    @Override
    protected String specificationSuffix() {
        return " (from: " + DateTime.dateToDisplay(fromSpecification) + " to: "
                + DateTime.dateToDisplay(toSpecification)
                + ")";
    }
}
