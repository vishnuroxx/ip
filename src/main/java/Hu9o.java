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

import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * A simple command-line chatbot that manages a list of tasks.
 */
public class Hu9o {
    /** Location of the task file, relative to the directory where Hu9o is run. */
    private static final Path TASK_DATA_PATH = Path.of("./data/taskData.txt");

    /** Matches a todo command and captures its description. */
    private static final Pattern TODO_PATTERN = Pattern.compile("^todo\\s+(.+)$", Pattern.CASE_INSENSITIVE);
    /** Matches a deadline command and captures its description and deadline. */
    private static final Pattern DEADLINE_PATTERN = Pattern.compile("^deadline\\s+(.+?)\\s+/by\\s+(.+)$",
            Pattern.CASE_INSENSITIVE);
    /** Matches an event command and captures its description, start, and end times. */
    private static final Pattern EVENT_PATTERN = Pattern.compile("^event\\s+(.+?)\\s+/from\\s+(.+?)\\s+/to\\s+(.+)$",
            Pattern.CASE_INSENSITIVE);

    /** Stores all tasks entered during the current program run. */
    public static ArrayList<Task> tasks = new ArrayList<>();

    /* Stores all valid commands */
    enum CommandType {
        TODO, DEADLINE, EVENT, LIST, MARK, UNMARK, DELETE, BYE;
    }

    /**
     * Recreates and stores one task from its pipe-delimited persistence record.
     *
     * @param str the compressed task record read from the task data file
     */
    private static void parseTask(String str) {
        /* T|Cross|description */
        String[] parts = str.split("\\|");
        String command = parts[0];
        Task task = null;
        switch (command) {
            case "T":
                task = new ToDoTask(parts[2]);
                break;
            case "D":
                task = new DeadlineTask(parts[2], parts[3]);
                break;
            case "E":
                task = new EventTask(parts[2], parts[3], parts[4]);
                break;
            default:
                break;
        }
        if (task == null)
            return;

        if (parts[1].equals("X"))
            task.mark();

        tasks.add(task);

    }

    /**
     * Loads all saved task records into the current task list.
     * The file stores one pipe-delimited task record per line.
     *
     * @return the loaded task list
     */
    private static ArrayList<Task> loadTasks() {
        try {
            Stream<String> lines = Files.lines(TASK_DATA_PATH);
            lines.forEach(x -> parseTask(x));
            lines.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Saves every task in the supplied list as a pipe-delimited record.
     * Each record is written on its own line so it can be loaded on the next run.
     *
     * @param tasks the tasks to persist
     */
    private static void dumpTasks(ArrayList<Task> tasks) {
        String result = "";
        for (Task task : tasks) {
            result += task.compressionString() + "\n";
        }

        try {
            Files.writeString(TASK_DATA_PATH, result);
            System.out.println("\n Saving data...");
            Thread.sleep(500);
            System.out.println(" Successfully saved data");
            Thread.sleep(200);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try {
            String banner = " _   _           ___ \n"
                    + "| | | | | | | | / _ \\   ___  \n"
                    + "| |_| | | | | || (_) | / _ \\ \n"
                    + "|  _  | | |_| | \\__,| | (_) |\n"
                    + "|_| |_|  \\___/   /_/   \\___/ \n";
            System.out.println("_________________________________");
            System.out.print(banner);
            System.out.println("_________________________________\n");
            System.out.println("Give me a second....Loading tasks...");
            Thread.sleep(1000);
            loadTasks();
            System.out.println("Successful! Use list to view the tasks.");
            Thread.sleep(500);
            System.out.println("Woof! I'm Hu9o!");
            Thread.sleep(500);
            System.out.println("What can I do for you?\n");

            try (Scanner scanner = new Scanner(System.in)) {
                for (;;) {
                    System.out.print("> ");
                    String command = scanner.nextLine();
                    if (command.equalsIgnoreCase("bye")) {
                        dumpTasks(tasks);
                        break;
                    }
                    answerHandler(command);
                }
            }
            System.out.println("_________________________________");
            System.out.println("\nBye. Hope to see you again soon! (wags tail)");
            System.out.println("_________________________________\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
