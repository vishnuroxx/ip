package hu9o;

import java.util.Scanner;

import hu9o.parser.Parser;
import hu9o.storage.Storage;
import hu9o.task.TaskList;
import hu9o.ui.Ui;

/**
 * Entry point for the Hu9o command-line chatbot.
 *
 * <p>Hu9o wires together four collaborators and runs the read-evaluate-print
 * loop, but delegates every real responsibility:
 * <ul>
 *   <li>{@link Ui} &ndash; all console output</li>
 *   <li>{@link Storage} &ndash; loading and saving tasks on disk</li>
 *   <li>{@link TaskList} &ndash; the in-memory list of tasks</li>
 *   <li>{@link Parser} &ndash; turning user and file text into actions</li>
 * </ul>
 */
public class Hu9o {
    private final Ui ui;
    private final TaskList tasks;
    private final Parser parser;
    private final Storage storage;

    /** Creates the collaborators and connects them to one another. */
    public Hu9o() {
        this.ui = new Ui();
        this.tasks = new TaskList();
        this.parser = new Parser(tasks, ui);
        this.storage = new Storage(parser, ui);
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
     * Launches Hu9o.
     *
     * @param args command-line arguments (unused).
     */
    public static void main(String[] args) {
        new Hu9o().run();
    }
}
