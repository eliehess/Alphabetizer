import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.application.Application;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public final class Main extends Application {
    private static final int WIDTH = 750;
    private static final int HEIGHT = 516 + 24;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL scene = getClass().getResource("/Main.fxml");
        Parent loadingRoot = FXMLLoader.load(scene);

        primaryStage.setResizable(false);
        primaryStage.setMaxHeight(HEIGHT);
        primaryStage.setMinHeight(HEIGHT);
        primaryStage.setMaxWidth(WIDTH);
        primaryStage.setMinWidth(WIDTH);

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("Icon_GUI.png")));
        primaryStage.setTitle("Alphabetizer");
        primaryStage.setScene(new Scene(loadingRoot));
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    static String getClipboard() throws IOException, UnsupportedFlavorException {
        String fin = null;
        Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (contents == null) return null;

        if (contents.isDataFlavorSupported(DataFlavor.stringFlavor))
            fin = (String) contents.getTransferData(DataFlavor.stringFlavor);
        return fin;
    }

    static boolean copyToClipboard(String text) {
        if (text == null || text.trim().length() < 1) return false;

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new StringSelection(text), null);
        return true;
    }

    /**
     * Parses a String into a List of Cards, then alphabetizes the list and merges duplicate entries.
     *
     * @param input the String to be parsed and alphabetized.
     * @return an alphabetized List.
     */
    static List<Card> stringToAlphabetizedCards(String input) {
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
    private static java.util.List<Card> merge(java.util.List<Card> leftList, java.util.List<Card> rightList) {
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
