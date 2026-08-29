import java.util.Scanner;

/**
 * Entry point for the Hu9o command-line chatbot.
 *
 * <p>Hu9o wires together four collaborators and runs the read-evaluate-print
 * loop, but delegates every real responsibility:
 * <ul>
 *   <li>{@link UI} &ndash; all console output</li>
 *   <li>{@link Storage} &ndash; loading and saving tasks on disk</li>
 *   <li>{@link TaskList} &ndash; the in-memory list of tasks</li>
 *   <li>{@link Parser} &ndash; turning user and file text into actions</li>
 * </ul>
 */
public class Hu9o {
    private final UI ui;
    private final TaskList tasks;
    private final Parser parser;
    private final Storage storage;

    /** Creates the collaborators and connects them to one another. */
    public Hu9o() {
        this.ui = new UI();
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
            for (;;) {
                ui.displayQuery();
                String command = scanner.nextLine();
                if (command.equalsIgnoreCase("bye")) {
                    storage.dumpTasks(tasks);
                    break;
                }
                parser.answerHandler(command);
            }
        }

        ui.showFarewell();
    }

    public static void main(String[] args) {
        new Hu9o().run();
    }
}
