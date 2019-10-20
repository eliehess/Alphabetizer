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

        final String splitDelim = "!";
        if (text.contains(splitDelim)) return;

        String replaced = text.replaceAll("\\r", "").replaceAll("\\n", splitDelim);

        StringTokenizer strtok = new StringTokenizer(replaced, splitDelim);

        while (strtok.hasMoreTokens()) {
            String str = strtok.nextToken();
            Card card = new Card();

            final int cardDelim = str.indexOf(" ");

            String cardNum = str.substring(0, cardDelim);
            if (cardNum.substring(cardNum.length() - 1).equals("x"))
                cardNum = cardNum.substring(0, cardNum.length() - 1);

            try {
                card.quantity = Integer.parseInt(cardNum);
            } catch (NumberFormatException e) {
                System.out.println("NumberFormatException detected: " + e);
                return;
            }

            card.name = str.substring(1 + cardDelim);
            cards.add(card);
        }

        Collections.sort(cards);

        String fin = "";
        for (Card c : cards)
            fin += (c.toString() + '\n');

        postTextArea.setText(fin);
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
            return name.compareTo(c.name);
        }
    }
}
