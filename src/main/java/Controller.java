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

        List<String> splitInput = splitByLine(text);
        if(splitInput == null) return;

        for(String s : splitInput) {
            //System.out.println(s);
            cards.add(inputToCard(s));
        }

        Collections.sort(cards);

        String fin = "";
        for(Card c : cards)
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

    private static List<String> splitByLine(String input) {
        List<String> fin = new ArrayList<>();

        final String delimiter = "!";
        if (input.contains(delimiter)) return null;

        String replaced =  input.replaceAll("\\r", "").replaceAll("\\n", delimiter);

        StringTokenizer strtok = new StringTokenizer(replaced, delimiter);

        while(strtok.hasMoreTokens())
            fin.add(strtok.nextToken());

        return fin;
    }

    private static Card inputToCard(String input) {
        Card fin = new Card();
        int delimiter = input.indexOf(" ");

        String num = input.substring(0, delimiter);
        if(num.substring(num.length() - 1).equals("x"))
            num = num.substring(0, num.length() - 1);

        String cardname = input.substring(1 + delimiter);

        try {
            fin.quantity = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException detected: " + e);
            return null;
        }

        fin.name = cardname;

        return fin;
    }
}
