/**
 * Represents a task in Hu9o's todo list.
 */
public abstract class Task {
    private final String description;
    private final String taskType;
    private boolean isDone = false;

    /**
     * Creates an unfinished task of the specified type.
     *
     * @param description the task's description
     * @param taskType the one-letter task type shown in the task output
     */
    protected Task(String description, String taskType) {
        this.description = description;
        this.taskType = taskType;
    }

    /**
     * Marks this task as done.
     */
    public void mark() {
        isDone = true;
    }

    /**
     * Marks this task as unfinished.
     */
    public void unmark() {
        isDone = false;
    }

    /**
     * Provides the type-specific text shown after the task description.
     *
     * @return the formatted task specification, including its parentheses
     */
    protected abstract String specificationSuffix();

    @Override
    public String toString() {
        String status = isDone ? "X" : " ";
        return "[" + taskType + "][" + status + "] " + description + specificationSuffix();
    }
}
