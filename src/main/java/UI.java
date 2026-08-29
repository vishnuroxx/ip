/**
 * Owns every line Hu9o prints to the console.
 *
 * <p>Keeping all output in one class means the logic classes ({@link Parser},
 * {@link Storage}, {@link TaskList}) never touch {@code System.out} directly, so
 * the exact wording, spacing, and conversational pauses live in a single place.
 * {@code UI} handles output; {@link Parser} handles input.
 */
public class UI {
    /** Horizontal rule printed around each block of output. */
    private static final String SEPARATOR = "_________________________________";

    /** ASCII-art logo shown at start-up. */
    private static final String BANNER = " _   _           ___ \n"
            + "| | | | | | | | / _ \\   ___  \n"
            + "| |_| | | | | || (_) | / _ \\ \n"
            + "|  _  | | |_| | \\__,| | (_) |\n"
            + "|_| |_|  \\___/   /_/   \\___/ \n";

    /**
     * Sleeps quietly so the start-up and shut-down messages feel conversational.
     * The interrupt flag is restored rather than crashing the program, since a
     * missed pause is harmless.
     *
     * @param millis how long to pause, in milliseconds
     */
    private void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /** Prints the banner and the "loading" message shown before tasks are read. */
    public void showWelcome() {
        System.out.println(SEPARATOR);
        System.out.print(BANNER);
        System.out.println(SEPARATOR + "\n");
        System.out.println("Give me a second....Loading tasks...");
        pause(1000);
    }

    /** Prints the greeting shown once tasks have finished loading. */
    public void showReady() {
        System.out.println("Successful! Use list to view the tasks.");
        pause(500);
        System.out.println("Woof! I'm Hu9o!");
        pause(500);
        System.out.println("What can I do for you?\n");
    }

    /** Prints the prompt that asks the user for the next command. */
    public void displayQuery() {
        System.out.print("> ");
    }

    /** Opens a block of command output with a separator line. */
    public void showBlockStart() {
        System.out.println(SEPARATOR + "\n");
    }

    /** Closes a block of command output with a separator line. */
    public void showBlockEnd() {
        System.out.println(SEPARATOR + "\n");
    }

    /** Prints the progress messages shown while tasks are written to disk. */
    public void showSaving() {
        System.out.println("\n Saving data...");
        pause(500);
        System.out.println(" Successfully saved data");
        pause(200);
    }

    /** Prints the farewell shown after the user types {@code bye}. */
    public void showFarewell() {
        System.out.println(SEPARATOR);
        System.out.println("\nBye. Hope to see you again soon! (wags tail)");
        System.out.println(SEPARATOR + "\n");
    }

    /**
     * Prints the whole task list, one numbered task per line.
     *
     * @param tasks the tasks to display
     */
    public void showTaskList(TaskList tasks) {
        int index = 1;
        for (Task task : tasks) {
            System.out.println(index + ". " + task);
            index++;
        }
    }

    /**
     * Confirms that a task was added and reports the new task count.
     *
     * @param task      the task that was added
     * @param taskCount the number of tasks now in the list
     */
    public void showTaskAdded(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:\n\t" + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Confirms that a task was marked as done.
     *
     * @param task the task that was marked
     */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:\n\t" + task);
    }

    /**
     * Confirms that a task was marked as not done.
     *
     * @param task the task that was unmarked
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("Ok, I've marked this task as not done yet:\n\t" + task);
    }

    /**
     * Confirms that a task was deleted.
     *
     * @param task the task that was removed
     */
    public void showTaskDeleted(Task task) {
        System.out.println("Got it. Deleted the following task:\n\t" + task);
    }

    /**
     * Prints a recoverable error message from a {@code Hu9oException}.
     *
     * @param message the user-facing explanation of what went wrong
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /** Prints the hint shown when a date could not be parsed. */
    public void showDateError() {
        System.out.println("Invalid Date...Use day/month/year 12 hr time\n\ti.e 12/8/26 330 pm");
    }
}
