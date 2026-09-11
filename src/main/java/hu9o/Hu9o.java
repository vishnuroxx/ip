package hu9o;

import java.util.Scanner;

import hu9o.contact.ContactParser;
import hu9o.contact.ContactStorage;
import hu9o.contact.Person;
import hu9o.contact.PersonNetwork;
import hu9o.parser.Parser;
import hu9o.storage.Storage;
import hu9o.task.TaskList;
import hu9o.ui.Ui;

/**
 * Entry point for the Hu9o chatbot, shared by the text CLI and the JavaFX GUI.
 *
 * <p>Hu9o wires together the task-tracking collaborators and runs the
 * read-evaluate-print loop, but delegates every real responsibility:
 * <ul>
 *   <li>{@link Ui} &ndash; all output</li>
 *   <li>{@link Storage} &ndash; loading and saving tasks on disk</li>
 *   <li>{@link TaskList} &ndash; the in-memory list of tasks</li>
 *   <li>{@link Parser} &ndash; turning user and file text into actions</li>
 * </ul>
 *
 * <p>It also wires up a separate, self-contained contact-network feature
 * ({@link PersonNetwork}, {@link ContactParser}, {@link ContactStorage}) and
 * routes each command to whichever side should handle it in
 * {@link #dispatchCommand(String)}. A contact verb always goes to the
 * contact parser; any other command goes to the task {@link Parser} &ndash;
 * against the global {@link #tasks} list normally, or against whichever
 * person is currently {@code select}ed, if anyone is.
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
    private final PersonNetwork people;
    private final ContactParser contactParser;
    private final ContactStorage contactStorage;

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
        this.people = new PersonNetwork();
        this.contactParser = new ContactParser(people, ui);
        this.contactStorage = new ContactStorage(parser);
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
        hu9o.contactStorage.loadAll(hu9o.people);
        return hu9o;
    }

    /** Greets the user, then reads and handles commands until {@code bye}. */
    public void run() {
        ui.showWelcome();
        storage.loadTasks(tasks);
        contactStorage.loadAll(people);
        ui.showReady();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                ui.displayQuery();
                String command = scanner.nextLine();
                if (command.equalsIgnoreCase("bye")) {
                    saveAll();
                    break;
                }
                dispatchCommand(command);
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
            saveAll();
            ui.readCaptured();
            return "Bye. Hope to see you again soon! (wags tail)";
        }
        dispatchCommand(input);
        return ui.readCaptured();
    }

    /**
     * Routes one command line to whichever parser should handle it: a
     * contact verb always goes to {@link #contactParser}; anything else goes
     * to a task {@link Parser} &ndash; against the currently selected
     * person's own list if someone is selected, or against the global
     * {@link #tasks} list otherwise. A throwaway {@code Parser} is
     * constructed per call when a person is selected, since {@code Parser}
     * carries no state beyond the {@link TaskList}/{@link Ui} references it
     * is given.
     *
     * @param command the complete command entered by the user.
     */
    private void dispatchCommand(String command) {
        String firstWord = command.trim().split("\\s+")[0];
        if (contactParser.isContactCommand(firstWord)) {
            contactParser.handleCommand(command);
            return;
        }
        Person selected = contactParser.getSelectedPerson();
        Parser activeParser = selected == null ? parser : new Parser(selected.getTasks(), ui);
        activeParser.handleCommand(command);
    }

    /** Saves the global task list and the whole contact network before exit. */
    private void saveAll() {
        storage.dumpTasks(tasks);
        contactStorage.dumpAll(people);
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
