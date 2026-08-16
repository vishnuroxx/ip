/**
 * Represents a task that has a deadline.
 */
public class DeadlineTask extends Task {
    private final String specification;

    /**
     * Creates an unfinished deadline task.
     *
     * @param description the task's description
     * @param specification the deadline, such as "Sunday"
     */
    public DeadlineTask(String description, String specification) {
        super(description, "D");
        this.specification = specification;
    }

    @Override
    protected String specificationSuffix() {
        return " (by: " + specification + ")";
    }
}
