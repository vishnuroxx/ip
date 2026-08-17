import errors.InvalidCommandException;
import errors.InvalidDeadlineFormatException;
import errors.InvalidEventFormatException;
import errors.InvalidMarkIndexException;
import errors.InvalidTodoFormatException;
import errors.InvalidUnmarkIndexException;
import errors.InvalidDeleteIndexException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple command-line chatbot that manages a list of tasks.
 */
public class Hu9o {
    private static final Pattern TODO_PATTERN = Pattern.compile("^todo\\s+(.+)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern DEADLINE_PATTERN = Pattern.compile("^deadline\\s+(.+?)\\s+/by\\s+(.+)$",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern EVENT_PATTERN = Pattern.compile("^event\\s+(.+?)\\s+/from\\s+(.+?)\\s+/to\\s+(.+)$",
            Pattern.CASE_INSENSITIVE);

    /** Stores all tasks entered during the current program run. */
    public static ArrayList<Task> tasks = new ArrayList<>();

    /* Stores all valid commands */
    enum CommandType {
        TODO, DEADLINE, EVENT, LIST, MARK, UNMARK, DELETE, BYE;
    }

    public static void main(String[] args) {
        String banner = " _   _           ___ \n"
                + "| | | | | | | | / _ \\   ___  \n"
                + "| |_| | | | | || (_) | / _ \\ \n"
                + "|  _  | | |_| | \\__,| | (_) |\n"
                + "|_| |_|  \\___/   /_/   \\___/ \n";
        System.out.println("_________________________________");
        System.out.print(banner);
        System.out.println("_________________________________\n");
        System.out.println("Woof! I'm Hu9o!");
        System.out.println("What can I do for you?\n");

        try (Scanner scanner = new Scanner(System.in)) {
            for (;;) {
                System.out.print("> ");
                String command = scanner.nextLine();
                if (command.equalsIgnoreCase("bye")) {
                    break;
                }
                answerHandler(command);
            }
        }
        System.out.println("_________________________________");
        System.out.println("\nBye. Hope to see you again soon! (wags tail)");
        System.out.println("_________________________________\n");
    }

    /** Adds a task and displays the updated task count. */
    private static void addTask(Task task) {
        tasks.add(task);
        System.out.println("Got it. I've added this task:\n\t" + task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Identifies the requested operation and performs it on the task list.
     * Each command validates its own input by throwing a specific Hu9oException;
     * all user-facing error messages are handled by the catch blocks below.
     *
     * @param str the complete command entered by the user
     */
    private static void answerHandler(String str) {
        String[] parts = str.trim().split("\\s+");
        System.out.println("_________________________________\n");

        try {
            CommandType command;
            try {
                command = CommandType.valueOf(parts[0].toUpperCase());
            } catch (IllegalArgumentException error) {
                throw new InvalidCommandException();
            }
            switch (command) {
                case LIST:
                    int index = 1;
                    for (Task task : tasks) {
                        System.out.println(index + ". " + task);
                        index++;
                    }
                    break;
                case MARK:
                    Task taskToMark = getTask(parts, true);
                    taskToMark.mark();
                    System.out.println("Nice! I've marked this task as done:\n\t" + taskToMark);
                    break;
                case UNMARK:
                    Task taskToUnmark = getTask(parts, false);
                    taskToUnmark.unmark();
                    System.out.println("Ok, I've marked this task as not done yet:\n\t" + taskToUnmark);
                    break;
                case TODO:
                    Matcher todoMatcher = TODO_PATTERN.matcher(str);
                    if (!todoMatcher.matches()) {
                        throw new InvalidTodoFormatException();
                    }
                    addTask(new ToDoTask(todoMatcher.group(1)));
                    break;
                case DEADLINE:
                    Matcher deadlineMatcher = DEADLINE_PATTERN.matcher(str);
                    if (!deadlineMatcher.matches()) {
                        throw new InvalidDeadlineFormatException();
                    }
                    addTask(new DeadlineTask(deadlineMatcher.group(1), deadlineMatcher.group(2)));
                    break;
                case EVENT:
                    Matcher eventMatcher = EVENT_PATTERN.matcher(str);
                    if (!eventMatcher.matches()) {
                        throw new InvalidEventFormatException();
                    }
                    addTask(new EventTask(eventMatcher.group(1), eventMatcher.group(2), eventMatcher.group(3)));
                    break;
                case DELETE:
                    deleteTask(parts);
                    break;
                default:
                    throw new InvalidCommandException();
            }
        } catch (InvalidMarkIndexException error) {
            System.out.println(error.getMessage());
        } catch (InvalidUnmarkIndexException error) {
            System.out.println(error.getMessage());
        } catch (InvalidTodoFormatException error) {
            System.out.println(error.getMessage());
        } catch (InvalidDeadlineFormatException error) {
            System.out.println(error.getMessage());
        } catch (InvalidEventFormatException error) {
            System.out.println(error.getMessage());
        } catch (InvalidCommandException error) {
            System.out.println(error.getMessage());
        } catch (InvalidDeleteIndexException error) {
            System.out.println(error.getMessage());
        }
        System.out.println("_________________________________\n");
    }

    /* Deletes a valid Task */
    private static void deleteTask(String[] parts) throws InvalidDeleteIndexException {
        boolean isInvalid = parts.length != 2 || !parts[1].matches("[1-9][0-9]{0,8}");
        int task_number = isInvalid ? 0 : Integer.parseInt(parts[1]);
        if (isInvalid || Integer.parseInt(parts[1]) > tasks.size())
            throw new InvalidDeleteIndexException();
        // Delete task
        Task deleted = tasks.remove(task_number - 1);
        System.out.println("Got it. Deleted the following task:\n\t" + deleted);
    }

    /** Gets a valid task for either the mark or unmark command. */
    private static Task getTask(String[] parts, boolean isMarkCommand)
            throws InvalidMarkIndexException, InvalidUnmarkIndexException {
        boolean isInvalid = parts.length != 2 || !parts[1].matches("[1-9][0-9]{0,8}");
        int taskNumber = isInvalid ? 0 : Integer.parseInt(parts[1]);
        if (isInvalid || taskNumber > tasks.size()) {
            if (isMarkCommand) {
                throw new InvalidMarkIndexException();
            }
            throw new InvalidUnmarkIndexException();
        }
        return tasks.get(taskNumber - 1);
    }

}
