package hu9o.parser;

import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hu9o.errors.Hu9oException;
import hu9o.errors.InvalidCommandException;
import hu9o.errors.InvalidDeadlineFormatException;
import hu9o.errors.InvalidEventFormatException;
import hu9o.errors.InvalidFindFormatException;
import hu9o.errors.InvalidTodoFormatException;
import hu9o.task.DeadlineTask;
import hu9o.task.EventTask;
import hu9o.task.Task;
import hu9o.task.TaskList;
import hu9o.task.ToDoTask;
import hu9o.ui.Ui;

/**
 * Turns text into actions for the rest of the program.
 *
 * <p>{@code Parser} handles two kinds of input: the pipe-delimited records read
 * from the save file ({@link #parseTask}), and the free-form commands typed by
 * the user ({@link #handleCommand}). It holds a {@link TaskList} to act on and a
 * {@link Ui} to report results through, so all console output stays in {@code Ui}.
 */
public class Parser {
    /** Matches a todo command and captures its description. */
    private static final Pattern TODO_PATTERN =
            Pattern.compile("^todo\\s+(.+)$", Pattern.CASE_INSENSITIVE);
    /** Matches a deadline command and captures its description and deadline. */
    private static final Pattern DEADLINE_PATTERN =
            Pattern.compile("^deadline\\s+(.+?)\\s+/by\\s+(.+)$", Pattern.CASE_INSENSITIVE);
    /** Matches an event command and captures its description, start, and end times. */
    private static final Pattern EVENT_PATTERN =
            Pattern.compile("^event\\s+(.+?)\\s+/from\\s+(.+?)\\s+/to\\s+(.+)$", Pattern.CASE_INSENSITIVE);
    /** Matches a find command and captures its search keyword. */
    private static final Pattern FIND_PATTERN =
            Pattern.compile("^find\\s+(.+)$", Pattern.CASE_INSENSITIVE);

    /** Every command name Hu9o understands. */
    private enum CommandType {
        TODO, DEADLINE, EVENT, LIST, FIND, MARK, UNMARK, DELETE, BYE
    }

    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates a parser that acts on the given task list and reports through the given UI.
     *
     * @param tasks the task list to read and modify.
     * @param ui    the UI used to display results and errors.
     */
    public Parser(TaskList tasks, Ui ui) {
        this.tasks = tasks;
        this.ui = ui;
    }

    /**
     * Recreates one task from its pipe-delimited persistence record.
     *
     * <p>Example record: {@code T|X|read book|}. Storage decides what to do with
     * the returned task; the parser only rebuilds it.
     *
     * @param record the compressed task record read from the save file.
     * @return the reconstructed task, or {@code null} if the record's type is unknown.
     */
    public Task parseTask(String record) {
        String[] parts = record.split("\\|");
        Task task = switch (parts[0]) {
            case "T" -> new ToDoTask(parts[2]);
            case "D" -> new DeadlineTask(parts[2], parts[3]);
            case "E" -> new EventTask(parts[2], parts[3], parts[4]);
            default -> null;
        };
        if (task == null) {
            return null;
        }
        if (parts[1].equals("X")) {
            task.mark();
        }
        return task;
    }

    /**
     * Identifies the requested operation and performs it on the task list.
     *
     * <p>Each command validates its own input by throwing a specific
     * {@link Hu9oException}; every user-facing error message is produced by the
     * single catch block below, which relies on all of those exceptions sharing
     * the {@code Hu9oException} base type.
     *
     * @param command the complete command entered by the user.
     */
    public void handleCommand(String command) {
        String[] parts = command.trim().split("\\s+");
        ui.showBlockStart();
        try {
            switch (parseCommand(parts[0])) {
                case LIST:
                    ui.showTaskList(tasks);
                    break;
                case FIND: {
                    Matcher matcher = FIND_PATTERN.matcher(command);
                    if (!matcher.matches()) {
                        throw new InvalidFindFormatException();
                    }
                    ui.showMatchingTasks(tasks.findTasks(matcher.group(1)));
                    break;
                }
                case MARK: {
                    Task task = tasks.getTask(parseIndex(parts), true);
                    task.mark();
                    ui.showTaskMarked(task);
                    break;
                }
                case UNMARK: {
                    Task task = tasks.getTask(parseIndex(parts), false);
                    task.unmark();
                    ui.showTaskUnmarked(task);
                    break;
                }
                case TODO: {
                    Matcher matcher = TODO_PATTERN.matcher(command);
                    if (!matcher.matches()) {
                        throw new InvalidTodoFormatException();
                    }
                    addAndConfirm(new ToDoTask(matcher.group(1)));
                    break;
                }
                case DEADLINE: {
                    Matcher matcher = DEADLINE_PATTERN.matcher(command);
                    if (!matcher.matches()) {
                        throw new InvalidDeadlineFormatException();
                    }
                    addAndConfirm(new DeadlineTask(matcher.group(1), matcher.group(2)));
                    break;
                }
                case EVENT: {
                    Matcher matcher = EVENT_PATTERN.matcher(command);
                    if (!matcher.matches()) {
                        throw new InvalidEventFormatException();
                    }
                    addAndConfirm(new EventTask(matcher.group(1), matcher.group(2), matcher.group(3)));
                    break;
                }
                case DELETE:
                    ui.showTaskDeleted(tasks.deleteTask(parseIndex(parts)));
                    break;
                default:
                    throw new InvalidCommandException();
            }
        } catch (Hu9oException exception) {
            ui.showError(exception.getMessage());
        } catch (DateTimeParseException exception) {
            ui.showDateError();
        }
        ui.showBlockEnd();
    }

    /** Adds a task to the list and asks the UI to confirm it. */
    private void addAndConfirm(Task task) {
        tasks.addTask(task);
        ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Converts the first word of a command into a {@link CommandType}.
     *
     * @param word the command name typed by the user.
     * @return the matching command type.
     * @throws InvalidCommandException if the word is not a known command.
     */
    private CommandType parseCommand(String word) throws InvalidCommandException {
        try {
            return CommandType.valueOf(word.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new InvalidCommandException();
        }
    }

    /**
     * Reads the task number from a two-word command such as {@code mark 3}.
     *
     * @param parts the whitespace-split command.
     * @return the task number, or {@code 0} if the command is malformed (the
     *         task list treats 0 as out of range).
     */
    private int parseIndex(String[] parts) {
        boolean isValid = parts.length == 2 && parts[1].matches("[1-9][0-9]{0,8}");
        return isValid ? Integer.parseInt(parts[1]) : 0;
    }
}
