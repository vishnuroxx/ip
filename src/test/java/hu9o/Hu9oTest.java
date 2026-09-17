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

    @Test
    public void getResponse_addPerson_returnsConfirmationText() {
        Hu9o hu9o = Hu9o.createForGui();

        String response = hu9o.getResponse("person Hu9oTest Person Alpha /phone 91234567 /email alpha@example.com");

        assertTrue(response.contains("Got it. I've added this person:"));
        assertTrue(response.contains("Hu9oTest Person Alpha"));
        assertFalse(hu9o.isLastResponseError());
    }

    @Test
    public void getResponse_selectThenTodo_addsTaskToPersonsListOnly() {
        Hu9o hu9o = Hu9o.createForGui();
        hu9o.getResponse("person Hu9oTest Person Beta /phone 91234567 /email beta@example.com");
        hu9o.getResponse("select Hu9oTest Person Beta");

        String addResponse = hu9o.getResponse("todo Hu9oTest marker task beta");
        String personListResponse = hu9o.getResponse("list");
        hu9o.getResponse("deselect");
        String globalListResponse = hu9o.getResponse("list");

        assertTrue(addResponse.contains("Got it. I've added this task:"));
        assertTrue(personListResponse.contains("Hu9oTest marker task beta"));
        assertFalse(globalListResponse.contains("Hu9oTest marker task beta"));
    }

    @Test
    public void getResponse_selectUnknownPerson_flagsErrorOnNextQuery() {
        Hu9o hu9o = Hu9o.createForGui();

        String response = hu9o.getResponse("select Hu9oTest Nonexistent Person Gamma");

        assertTrue(response.contains("No person with that name"));
        assertTrue(hu9o.isLastResponseError());
    }

    @Test
    public void getResponse_linkTwoPeopleThenConnections_listsBothNames() {
        Hu9o hu9o = Hu9o.createForGui();
        hu9o.getResponse("person Hu9oTest Person Delta /phone 91234567 /email delta@example.com");
        hu9o.getResponse("person Hu9oTest Person Epsilon /phone 91234567 /email epsilon@example.com");
        hu9o.getResponse("link Hu9oTest Person Delta /with Hu9oTest Person Epsilon");

        String response = hu9o.getResponse("connections Hu9oTest Person Delta");

        assertTrue(response.contains("Hu9oTest Person Epsilon"));
        assertFalse(hu9o.isLastResponseError());
    }

    @Test
    public void getResponse_deletePersonWhoIsSelected_autoDeselects() {
        Hu9o hu9o = Hu9o.createForGui();
        hu9o.getResponse("person Hu9oTest Person Zeta /phone 91234567 /email zeta@example.com");
        hu9o.getResponse("select Hu9oTest Person Zeta");

        hu9o.getResponse("deleteperson 1");
        String response = hu9o.getResponse("deselect");

        assertTrue(response.contains("No one was selected"));
    }

    @Test
    public void getResponse_contactVerbWhilePersonSelected_stillRoutesToContactParser() {
        Hu9o hu9o = Hu9o.createForGui();
        hu9o.getResponse("person Hu9oTest Person Eta /phone 91234567 /email eta@example.com");
        hu9o.getResponse("select Hu9oTest Person Eta");

        String response = hu9o.getResponse("people");

        assertTrue(response.contains("Hu9oTest Person Eta"));
    }
}
