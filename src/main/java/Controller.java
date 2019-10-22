import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

public class Controller {

    public Button alphabetizeButton;
    @FXML
    private TextArea postTextArea;
    @FXML
    private TextArea preTextArea;

    /**
     * Alphabetizes the contents of the pre-alphabetized TextArea and
     * sets the post-alphabetized TextArea's contents to the result.
     */
    @FXML
    private void alphabetize() {
        List<Card> cards = Main.stringToAlphabetizedCards(preTextArea.getText());

        if (cards == null) return;

        postTextArea.setText("");
        cards.forEach(card -> postTextArea.appendText(card + "\n"));
    }

    /**
     * Copies the system clipboard contents to the pre-alphabetized TextArea.
     */
    @FXML
    private void pasteFromClipboard() {
        try {
            String clipboard = Main.getClipboard();
            if (clipboard != null)
                preTextArea.setText(clipboard);
        } catch (UnsupportedFlavorException | IOException e) {
            System.out.println("Exception detected: " + e);
        }
    }

    /**
     * Copies the alphabetized text to the system clipboard.
     */
    @FXML
    private void copyToClipboard() {
        Main.copyToClipboard(postTextArea.getText());
    }

    /**
     * Pastes from clipboard, alphabetizes, and copies to clipboard.
     */
    @FXML
    private void clipboardPivot() {
        pasteFromClipboard();
        alphabetize();
        copyToClipboard();
    }

    /**
     * Clears both the preTextArea and postTextArea.
     */
    @FXML
    private void clear() {
        preTextArea.setText("");
        postTextArea.setText("");
    }

}
