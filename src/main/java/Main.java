import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public final class Main {
    private static List<String> errorLog;
    private static String output = null;

    public static void main(String[] args) {
        errorLog = new ArrayList<>();
        String text = getClipboardContents();

        if (text == null) {
            errorLog.add("getClipboardContents() returned null");
            printLog();
            return;
        }

        text = alphabetize(text);

        if (text == null || text.trim().length() < 1) {
            errorLog.add("alphabetize() returned null");
            printLog();
            return;
        }

        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                new StringSelection(text), null);
        output = text;
        printLog();
    }

    /**
     * If any error messages are stored in the log, output them to a new file called "log.txt".
     */
    private static void printLog() {
        if (errorLog.size() < 1) return;
        try {
            String str = "";
            for (String logStr : errorLog)
                str += logStr + "\n";
            if (output == null)
                str += "NO CHANGES MADE TO CLIPBOARD";
            else
                str += "Current clipboard contents:\n" + output;
            BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt"));
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            System.out.println("IOException detected: " + e);
        }
    }

    /**
     * Returns the current system clipboard contents if it can be parsed to a Java String
     *
     * @return the current system clipboard contents
     */
    private static String getClipboardContents() {
        String fin = null;
        Transferable contents = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        if (contents == null) return null;

        if (contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                fin = (String) contents.getTransferData(DataFlavor.stringFlavor);
            } catch (IOException e) {
                errorLog.add("IOException detected: " + e.toString());
            } catch (UnsupportedFlavorException e) {
                errorLog.add("UnsupportedFlavorException detected: " + e.toString());
            }
        }
        return fin;
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
     * Parses a String into a List of Cards, then alphabetizes the list,  merges duplicate entries,
     * and converts it back into a String.
     *
     * @param input the String to be parsed and alphabetized.
     * @return an alphabetized String.
     */
    private static String alphabetize(String input) {
        List<Card> cards = new ArrayList<>();

        final String delimiter = "&";
        if (input.contains(delimiter)) {
            errorLog.add("ERROR: Input contains prohibited character " + delimiter);
            return null;
        }

        StringTokenizer tokens = new StringTokenizer(input.replaceAll("\\r", "")
                .replaceAll("\\n", delimiter), delimiter);

        int i = 0;
        while (tokens.hasMoreTokens()) {
            String str = tokens.nextToken();
            int quantity;
            String name;

            final int splitPoint = str.indexOf(" ");
            if (splitPoint <= 0) {
                errorLog.add("ERROR: line " + i + " (" + str + ") does not contain a space");
                continue;
            }

            String cardNum = str.substring(0, splitPoint);
            if (cardNum.substring(cardNum.length() - 1).equals("x"))
                cardNum = cardNum.substring(0, cardNum.length() - 1);

            try {
                quantity = Integer.parseInt(cardNum);
            } catch (NumberFormatException e) {
                errorLog.add("Line " + i + "does not contain a properly-formatted number");
                continue;
            }

            name = str.substring(1 + splitPoint);
            cards.add(new Card(name, quantity));
            i++;
        }

        cards = mergeSort(cards);
        String fin = "";
        for (Card card : cards)
            fin += (card + "\n");
        return fin;
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
        if (leftList == null || rightList == null) {
            errorLog.add("Attempting to merge null list");
            return null;
        } else if (leftList.size() == 0) return rightList;
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
