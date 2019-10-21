import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class Controller {

    public Button alphabetizeButton;
    @FXML
    private TextArea postTextArea;
    @FXML
    private TextArea preTextArea;

    @FXML
    private void alphabetize() {
        List<Card> cards = stringToAlphabetizedCards(preTextArea.getText());

        if (cards == null) return;

        postTextArea.setText("");
        cards.forEach(card -> postTextArea.appendText(card + "\n"));
    }

    @FXML
    private void pasteFromClipboard() {
        preTextArea.setText(getClipboardContents());
    }

    @FXML
    private void copyToClipboard() {
        sendToClipboard(postTextArea.getText());
    }

    @FXML
    private void clipboardPivot() {
        pasteFromClipboard();
        alphabetize();
        copyToClipboard();
    }

    @FXML
    private void clear() {
        preTextArea.setText("");
        postTextArea.setText("");
    }

    private static class Card implements Comparable<Card> {
        String name;
        int quantity;

        @Override
        public String toString() {
            return quantity + " " + name;
        }

        @Override
        public int compareTo(Card otherCard) {
            int compareName = name.compareTo(otherCard.name);
            return compareName == 0 ? quantity - otherCard.quantity : compareName;
        }
    }

    private static List<Card> stringToAlphabetizedCards(String input) {
        List<Card> fin = new ArrayList<>();

        final String delimiter = "&";
        if (input.contains(delimiter)) return null;

        StringTokenizer tokens = new StringTokenizer(input.replaceAll("\\r", "")
                .replaceAll("\\n", delimiter), delimiter);

        while (tokens.hasMoreTokens()) {
            String str = tokens.nextToken();
            Card card = new Card();

            final int splitPoint = str.indexOf(" ");
            if (splitPoint <= 0)
                continue;

            String cardNum = str.substring(0, splitPoint);
            if (cardNum.substring(cardNum.length() - 1).equals("x"))
                cardNum = cardNum.substring(0, cardNum.length() - 1);

            try {
                card.quantity = Integer.parseInt(cardNum);
            } catch (NumberFormatException e) {
                System.out.println("NumberFormatException detected: " + e);
                continue;
            }

            card.name = str.substring(1 + splitPoint);
            fin.add(card);
        }

        Collections.sort(fin);
        for (int i = 0; i < fin.size() - 1; i++) {
            Card curCard = fin.get(i);
            Card nextCard = fin.get(i + 1);
            if (curCard.name.equals(nextCard.name)) {
                curCard.quantity += nextCard.quantity;
                fin.remove(i + 1);
            }
        }
        return fin;
    }

    private static String getClipboardContents() {
        String fin = "";
        Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (contents == null) return null;

        if (contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                fin = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException e) {
                System.out.println("Exception detected: " + e);
            }
        }
        return fin;
    }

    private static void sendToClipboard(String str) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(str), null);
    }
}
