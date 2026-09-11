package hu9o;

import java.util.Scanner;

import hu9o.parser.Parser;
import hu9o.storage.Storage;
import hu9o.task.TaskList;
import hu9o.ui.Ui;

/**
 * Entry point for the Hu9o chatbot, shared by the text CLI and the JavaFX GUI.
 *
 * <p>Hu9o wires together four collaborators and runs the read-evaluate-print
 * loop, but delegates every real responsibility:
 * <ul>
 *   <li>{@link Ui} &ndash; all output</li>
 *   <li>{@link Storage} &ndash; loading and saving tasks on disk</li>
 *   <li>{@link TaskList} &ndash; the in-memory list of tasks</li>
 *   <li>{@link Parser} &ndash; turning user and file text into actions</li>
 * </ul>
 *
 * <p>{@link #main(String[])} runs the console loop. The GUI instead builds Hu9o
 * with {@link #createForGui()} and feeds it one line at a time through
 * {@link #getResponse(String)}.
 */
public class Hu9o {
    private final Ui ui;
    private final TaskList tasks;
    private final Parser parser;
    private final Storage storage;

    /** Creates a console Hu9o that prints its output to the terminal. */
    public Hu9o() {
        this(new Ui());
    }

    /**
     * Creates a Hu9o whose collaborators all report through the given UI.
     *
     * @param ui the UI shared by every collaborator.
     */
    private Hu9o(Ui ui) {
        // Callers pass either new Ui() or Ui.createForGui(), never null.
        assert ui != null : "Hu9o needs a UI shared by every collaborator";
        this.ui = ui;
        this.tasks = new TaskList();
        this.parser = new Parser(tasks, ui);
        this.storage = new Storage(parser, ui);
    }

    /**
     * Creates a Hu9o for the JavaFX GUI: its UI captures output for the GUI to
     * read back, and the saved tasks are loaded straight away.
     *
     * @return a GUI-ready Hu9o.
     */
    public static Hu9o createForGui() {
        Hu9o hu9o = new Hu9o(Ui.createForGui());
        hu9o.storage.loadTasks(hu9o.tasks);
        return hu9o;
    }

    /** Greets the user, then reads and handles commands until {@code bye}. */
    public void run() {
        ui.showWelcome();
        storage.loadTasks(tasks);
        ui.showReady();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                ui.displayQuery();
                String command = scanner.nextLine();
                if (command.equalsIgnoreCase("bye")) {
                    storage.dumpTasks(tasks);
                    break;
                }
                parser.handleCommand(command);
            }
        }

        ui.showFarewell();
    }

    /**
     * Handles one line of GUI input and returns the reply to show in a dialog
     * box. A {@code bye} command saves the tasks and returns the farewell.
     *
     * @param input the text the user typed into the GUI.
     * @return Hu9o's reply.
     */
    public String getResponse(String input) {
        if (isExitCommand(input)) {
            storage.dumpTasks(tasks);
            ui.readCaptured();
            return "Bye. Hope to see you again soon! (wags tail)";
        }
        parser.handleCommand(input);
        return ui.readCaptured();
    }

    /**
     * Returns whether the given input is the command that closes Hu9o.
     *
     * @param input the text the user typed.
     * @return {@code true} if the input is {@code bye}, ignoring case and spacing.
     */
    public boolean isExitCommand(String input) {
        return input.trim().equalsIgnoreCase("bye");
    }

    /**
     * Returns whether the most recent {@link #getResponse(String)} produced an
     * error message, so the GUI can style that reply differently. Consumes the
     * flag, so each reply is classified exactly once.
     *
     * @return {@code true} if the last reply was an error message.
     */
    public boolean isLastResponseError() {
        return ui.consumeErrorShown();
    }

    /**
     * Returns the greeting shown in the dialog area when the GUI opens.
     *
     * @return the welcome message.
     */
    public String getWelcomeMessage() {
        return "Woof! I'm Hu9o!\n"
                + tasks.size() + " task(s) loaded. What can I do for you?";
    }

    /**
     * Launches the text CLI. The GUI has its own entry point in
     * {@code hu9o.gui.Launcher}.
     *
     * @param args command-line arguments (unused).
     */
    public static void main(String[] args) {
        new Hu9o().run();
    }
}
