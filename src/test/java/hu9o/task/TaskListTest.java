package hu9o.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link TaskList#findTasks(String)}: which tasks are returned, their
 * order, and case-insensitive matching.
 */
public class TaskListTest {

    private static TaskList listOf(String... descriptions) {
        TaskList tasks = new TaskList();
        for (String description : descriptions) {
            tasks.addTask(new ToDoTask(description));
        }
        return tasks;
    }

    @Test
    public void findTasks_keywordInSomeDescriptions_returnsOnlyThoseInListOrder() {
        TaskList tasks = listOf("read book", "wash car", "return book");

        TaskList matches = tasks.findTasks("book");

        assertEquals(2, matches.size());
        assertEquals("read book", matches.get(0).getTaskDescription());
        assertEquals("return book", matches.get(1).getTaskDescription());
    }

    @Test
    public void findTasks_keywordDiffersInCase_stillMatches() {
        TaskList tasks = listOf("Read Book");

        assertEquals(1, tasks.findTasks("book").size());
    }

    @Test
    public void findTasks_noDescriptionContainsKeyword_returnsEmptyList() {
        TaskList tasks = listOf("read book", "wash car");

        assertEquals(0, tasks.findTasks("plants").size());
    }
}
