package hu9o.gui;

import hu9o.Hu9o;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for the main window.
 *
 * <p>It connects the scrolling dialog area, the text field, and the send button
 * to the {@link Hu9o} back end: each command becomes a pair of dialog boxes (the
 * user's line and Hu9o's reply), and a {@code bye} command closes the window
 * after a short delay so the farewell stays on screen.
 */
public class MainWindow extends AnchorPane {
    /** How long the farewell stays visible before the window closes. */
    private static final Duration EXIT_DELAY = Duration.seconds(1.5);

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Hu9o hu9o;

    private final Image userImage =
            new Image(this.getClass().getResourceAsStream("/images/DaUser.jpg"));
    private final Image hu9oImage =
            new Image(this.getClass().getResourceAsStream("/images/DaHu9o.jpg"));

    /** Pins the scroll pane to the newest message. Called by the FXML loader. */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the back end and shows its greeting.
     *
     * @param hu9o the chatbot that produces replies.
     */
    public void setHu9o(Hu9o hu9o) {
        // Main always injects a real back end straight after loading the FXML.
        assert hu9o != null : "MainWindow requires a Hu9o back end";
        this.hu9o = hu9o;
        dialogContainer.getChildren().add(
                DialogBox.getHu9oDialog(hu9o.getWelcomeMessage(), hu9oImage));
    }

    /**
     * Reads the text field, appends the user's line and Hu9o's reply as dialog
     * boxes, and clears the field. Error replies are styled to stand out, and a
     * {@code bye} command triggers a delayed window close.
     */
    @FXML
    private void handleUserInput() {
        // The send button and text field are only live after setHu9o() has run.
        assert hu9o != null : "setHu9o() must be called before handling input";
        String input = userInput.getText();
        if (input.isBlank()) {
            return;
        }

        String response = hu9o.getResponse(input);
        DialogBox userDialog = DialogBox.getUserDialog(input, userImage);
        DialogBox hu9oDialog = hu9o.isLastResponseError()
                ? DialogBox.getErrorDialog(response, hu9oImage)
                : DialogBox.getHu9oDialog(response, hu9oImage);
        dialogContainer.getChildren().addAll(userDialog, hu9oDialog);
        userInput.clear();

        if (hu9o.isExitCommand(input)) {
            exitAfterDelay();
        }
    }

    /** Disables further input and closes the window once {@link #EXIT_DELAY} passes. */
    private void exitAfterDelay() {
        userInput.setDisable(true);
        sendButton.setDisable(true);
        PauseTransition delay = new PauseTransition(EXIT_DELAY);
        delay.setOnFinished(event -> Platform.exit());
        delay.play();
    }
}
