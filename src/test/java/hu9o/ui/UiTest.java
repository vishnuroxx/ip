package hu9o.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import hu9o.task.TaskList;
import hu9o.task.ToDoTask;

/**
 * Tests the two {@link Ui} methods that build their output from arguments
 * ({@code showTaskAdded} and {@code showTaskList}) rather than printing a fixed
 * literal. Each test redirects {@code System.out} into a buffer so the printed
 * text can be asserted on.
 */
public class UiTest {
    private final PrintStream realOut = System.out;
    private ByteArrayOutputStream buffer;

    /** Strips carriage returns so assertions can be written with {@code \n} only. */
    private String captured() {
        return buffer.toString().replace("\r\n", "\n");
    }

    @BeforeEach
    public void redirectStdOut() {
        buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));
    }

    @AfterEach
    public void restoreStdOut() {
        System.setOut(realOut);
    }

    @Test
    public void showTaskAdded_todoTask_printsConfirmationAndCount() {
        new Ui().showTaskAdded(new ToDoTask("read book"), 3);

        assertEquals(
                "Got it. I've added this task:\n"
                        + "\t[T][ ] read book\n"
                        + "Now you have 3 tasks in the list.\n",
                captured());
    }

    @Test
    public void showTaskList_multipleTasks_printsOneNumberedLineEach() {
        TaskList tasks = new TaskList();
        tasks.addTask(new ToDoTask("read book"));
        tasks.addTask(new ToDoTask("buy milk"));

        new Ui().showTaskList(tasks);

        assertEquals(
                "1. [T][ ] read book\n"
                        + "2. [T][ ] buy milk\n",
                captured());
    }

    @Test
    public void showTaskList_emptyList_printsNothing() {
        new Ui().showTaskList(new TaskList());

        assertEquals("", captured());
    }

    @Test
    public void showMatchingTasks_withMatches_printsHeaderThenNumberedList() {
        TaskList matches = new TaskList();
        matches.addTask(new ToDoTask("read book"));
        matches.addTask(new ToDoTask("return book"));

        new Ui().showMatchingTasks(matches);

        assertEquals(
                "Here are the matching tasks in your list:\n"
                        + "1. [T][ ] read book\n"
                        + "2. [T][ ] return book\n",
                captured());
    }

    @Test
    public void showMatchingTasks_noMatches_printsNotice() {
        new Ui().showMatchingTasks(new TaskList());

        assertEquals("No matching tasks found.\n", captured());
    }
}
