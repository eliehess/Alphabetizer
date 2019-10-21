import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class Controller {

    @FXML
    private TextArea postTextArea;
    @FXML
    private TextArea preTextArea;

    @FXML
    private void alphabetize() {
        String text = preTextArea.getText();
        List<Card> cards = new ArrayList<>();

        final String delimiter = "!";
        if (text.contains(delimiter)) return;

        StringTokenizer tokens = new StringTokenizer(text.replaceAll("\\r", "")
                .replaceAll("\\n", delimiter), delimiter);

        while (tokens.hasMoreTokens()) {
            String str = tokens.nextToken();
            Card card = new Card();

            final int splitPoint = str.indexOf(" ");

            String cardNum = str.substring(0, splitPoint);
            if (cardNum.substring(cardNum.length() - 1).equals("x"))
                cardNum = cardNum.substring(0, cardNum.length() - 1);

            try {
                card.quantity = Integer.parseInt(cardNum);
            } catch (NumberFormatException e) {
                System.out.println("NumberFormatException detected: " + e);
                return;
            }

            card.name = str.substring(1 + splitPoint);
            cards.add(card);
        }

        Collections.sort(cards);

        postTextArea.setText("");
        cards.forEach(card -> postTextArea.appendText(card + "\n"));
    }

    private static class Card implements Comparable<Card> {
        String name;
        int quantity;

        @Override
        public String toString() {
            return quantity + " " + name;
        }

        @Override
        public int compareTo(Card c) {
            return name.compareTo(c.name) == 0 ? quantity - c.quantity : name.compareTo(c.name);
        }
    }
}
