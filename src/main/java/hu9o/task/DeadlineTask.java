package hu9o.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Represents a task that has a deadline.
 */
public class DeadlineTask extends Task {
    private final LocalDateTime specification;
    /**
     * Creates an unfinished deadline task.
     *
     * @param description   the task's description
     * @param specification the deadline, such as "Sunday"
     */

    public DeadlineTask(String description, String specification) throws DateTimeParseException {
        super(description, "D");
        this.specification = DateTime.parseString(specification);
    }

    @Override
    public String compressionString() {
        return super.compressionString() + DateTime.dateToString(specification) + "|";
    }

    @Override
    protected String specificationSuffix() {
        return " (by: " + DateTime.dateToDisplay(specification) + ")";
    }
}
