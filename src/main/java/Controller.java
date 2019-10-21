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
import java.util.List;
import java.util.StringTokenizer;

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
        List<Card> cards = stringToAlphabetizedCards(preTextArea.getText());

        if (cards == null) return;

        postTextArea.setText("");
        cards.forEach(card -> postTextArea.appendText(card + "\n"));
    }

    /**
     * Copies the system clipboard contents to the pre-alphabetized TextArea.
     */
    @FXML
    private void pasteFromClipboard() {
        Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (contents == null) return;

        if (contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                preTextArea.setText((String) contents.getTransferData(DataFlavor.stringFlavor));
            } catch (UnsupportedFlavorException | IOException e) {
                System.out.println("Exception detected: " + e);
            }
        }
    }

    /**
     * Copies the alphabetized text to the system clipboard.
     */
    @FXML
    private void copyToClipboard() {
        String text = postTextArea.getText();
        if (text == null || text.trim().length() < 1) return;
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new StringSelection(text), null);
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

    private static class Card implements Comparable<Card> {
        final String name;
        final int quantity;

        Card(String name, int quantity) {
            this.name = name;
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return quantity + " " + name;
        }

        @Override
        public int compareTo(Card otherCard) {
            return name.compareTo(otherCard.name);
        }
    }

    /**
     * Parses a String into a List of Cards, then alphabetizes the list and merges duplicate entries.
     *
     * @param input the String to be parsed and alphabetized.
     * @return an alphabetized List.
     */
    private static List<Card> stringToAlphabetizedCards(String input) {
        List<Card> fin = new ArrayList<>();

        final String delimiter = "&";
        if (input.contains(delimiter)) return null;

        StringTokenizer tokens = new StringTokenizer(input.replaceAll("\\r", "")
                .replaceAll("\\n", delimiter), delimiter);

        while (tokens.hasMoreTokens()) {
            String str = tokens.nextToken();
            int quantity;
            String name;

            final int splitPoint = str.indexOf(" ");
            if (splitPoint <= 0)
                continue;

            String cardNum = str.substring(0, splitPoint);
            if (cardNum.substring(cardNum.length() - 1).equals("x"))
                cardNum = cardNum.substring(0, cardNum.length() - 1);

            try {
                quantity = Integer.parseInt(cardNum);
            } catch (NumberFormatException e) {
                System.out.println("NumberFormatException detected: " + e);
                continue;
            }

            name = str.substring(1 + splitPoint);
            fin.add(new Card(name, quantity));
        }

        return mergeSort(fin);
    }

    /**
     * Sorts a list of Cards using a mergesort algorithm.
     *
     * @param cards the list of Cards to be sorted.
     * @return the sorted list.
     */
    private static List<Card> mergeSort(List<Card> cards) {
        if (cards.size() < 2) return cards;

        int leftSize = cards.size() / 2;
        int rightSize = cards.size() - leftSize;
        List<Card> leftList = new ArrayList<>();
        List<Card> rightList = new ArrayList<>();

        for (int i = 0; i < leftSize; i++)
            leftList.add(cards.get(i));

        for (int i = 0; i < rightSize; i++)
            rightList.add(cards.get(i + leftSize));

        return merge(mergeSort(leftList), mergeSort(rightList));
    }

    /**
     * Merges two sorted list of Cards. Multiple Cards with the same name are combined into a single entry.
     * Returns null if either input list is null. If one input list is empty, the other is returned.
     *
     * @param leftList  one list of Cards to be merged.
     * @param rightList another list of Cards to be merged.
     * @return the merged list.
     */
    private static List<Card> merge(List<Card> leftList, List<Card> rightList) {
        if (leftList == null || rightList == null) return null;
        else if (leftList.size() == 0) return rightList;
        else if (rightList.size() == 0) return leftList;

        List<Card> fin = new ArrayList<>();
        int leftCounter = 0;
        int rightCounter = 0;
        int leftSize = leftList.size();
        int rightSize = rightList.size();
        for (int i = 0; i < (leftSize + rightSize); i++) {
            if (rightCounter >= rightSize && leftCounter >= leftSize) {
                break;
            } else if (rightCounter >= rightSize) {
                fin.add(leftList.get(leftCounter));
                leftCounter++;
            } else if (leftCounter >= leftSize) {
                fin.add(rightList.get(rightCounter));
                rightCounter++;
            } else {
                Card leftElement = leftList.get(leftCounter);
                Card rightElement = rightList.get(rightCounter);
                int compare = leftElement.compareTo(rightElement);
                if (compare == 0) {
                    fin.add(new Card(leftElement.name, leftElement.quantity + rightElement.quantity));
                    leftCounter++;
                    rightCounter++;
                } else if (compare < 0) {
                    fin.add(leftElement);
                    leftCounter++;
                } else {
                    fin.add(rightElement);
                    rightCounter++;
                }
            }
        }
        return fin;
    }
}
