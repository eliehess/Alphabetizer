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

        fin = mergeSort(fin);
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

    private static List<Card> merge(List<Card> leftList, List<Card> rightList) {
        if (leftList == null) return rightList;
        if (rightList == null) return leftList;

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
