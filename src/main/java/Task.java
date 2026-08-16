/**
 * Represents one task in Hu9o's todo list.
 */
public class Task {
    private final String description;
    private boolean isDone = false;

    /**
     * Creates an unfinished task.
     *
     * @param description the task's description
     */
    public Task(String description) {
        this.description = description;
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

    @Override
    public String toString() {
        String status = isDone ? "X" : " ";
        return "[" + status + "] " + description;
    }
}
