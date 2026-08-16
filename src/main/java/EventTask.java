/**
 * Represents a task that takes place during a specified time.
 */
public class EventTask extends Task {
    private final String fromSpecification;
    private final String toSpecification;

    /**
     * Creates an unfinished event task.
     *
     * @param description   the event's description
     * @param specification the event time, such as "Aug 6th 2pm to: 4pm"
     */
    public EventTask(String description, String fromSpecification, String toSpecification) {
        super(description, "E");
        this.fromSpecification = fromSpecification;
        this.toSpecification = toSpecification;
    }

    @Override
    protected String specificationSuffix() {
        return " (from: " + this.fromSpecification + " to: " + this.toSpecification + ")";
    }
}
