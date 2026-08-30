import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Reads and writes the task list on disk.
 *
 * <p>{@code Storage} knows where the save file lives and how each task is
 * serialized line by line, but it delegates the actual text-to-task conversion
 * to {@link Parser} and all progress messages to {@link UI}.
 */
public class Storage {
    /** Location of the task file, relative to the directory where Hu9o is run. */
    private static final Path TASK_DATA_PATH = Path.of("./data/taskData.txt");

    private final Parser parser;
    private final UI ui;

    /**
     * Creates a storage helper.
     *
     * @param parser used to rebuild a task from each saved record
     * @param ui     used to show saving progress
     */
    public Storage(Parser parser, UI ui) {
        this.parser = parser;
        this.ui = ui;
    }

    /**
     * Loads every saved task record into the given task list.
     * The file stores one pipe-delimited task record per line.
     *
     * @param tasks the list to populate
     */
    public void loadTasks(TaskList tasks) {
        try (Stream<String> lines = Files.lines(TASK_DATA_PATH)) {
            lines.forEach(line -> {
                Task task = parser.parseTask(line);
                if (task != null) {
                    tasks.addTask(task);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves every task as a pipe-delimited record, one per line, so it can be
     * loaded on the next run.
     *
     * @param tasks the tasks to persist
     */
    public void dumpTasks(TaskList tasks) {
        StringBuilder result = new StringBuilder();
        for (Task task : tasks) {
            result.append(task.compressionString()).append("\n");
        }

        try {
            Files.writeString(TASK_DATA_PATH, result.toString());
            ui.showSaving();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
