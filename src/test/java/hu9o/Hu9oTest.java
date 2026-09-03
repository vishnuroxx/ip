package hu9o;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests the GUI-facing surface of {@link Hu9o}: that {@link Hu9o#getResponse}
 * returns the reply text as a string, that {@link Hu9o#isLastResponseError}
 * reflects the outcome of that reply, and that {@link Hu9o#isExitCommand}
 * recognises {@code bye} regardless of casing and surrounding spaces.
 *
 * <p>These tests avoid {@code bye}, since handling it writes the save file.
 */
public class Hu9oTest {

    @Test
    public void getResponse_addTodo_returnsConfirmationTextAndNoError() {
        Hu9o hu9o = Hu9o.createForGui();

        String response = hu9o.getResponse("todo read book");

        assertTrue(response.contains("Got it. I've added this task:"));
        assertTrue(response.contains("[T][ ] read book"));
        assertFalse(hu9o.isLastResponseError());
    }

    @Test
    public void getResponse_unknownCommand_flagsErrorOnNextQuery() {
        Hu9o hu9o = Hu9o.createForGui();

        String response = hu9o.getResponse("frobnicate");

        assertTrue(response.contains("Unknown command"));
        assertTrue(hu9o.isLastResponseError());
    }

    @Test
    public void isLastResponseError_errorThenSuccess_reportsPerCommand() {
        Hu9o hu9o = Hu9o.createForGui();

        hu9o.getResponse("mark 999");
        assertTrue(hu9o.isLastResponseError());

        hu9o.getResponse("todo buy milk");
        assertFalse(hu9o.isLastResponseError());
    }

    @Test
    public void isExitCommand_byeInAnyCasingOrSpacing_returnsTrue() {
        Hu9o hu9o = Hu9o.createForGui();

        assertTrue(hu9o.isExitCommand("bye"));
        assertTrue(hu9o.isExitCommand("  ByE  "));
    }

    @Test
    public void isExitCommand_otherCommands_returnsFalse() {
        Hu9o hu9o = Hu9o.createForGui();

        assertFalse(hu9o.isExitCommand("byebye"));
        assertFalse(hu9o.isExitCommand("list"));
    }
}
