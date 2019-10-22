import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Auto {
    private static List<String> errorLog;

    public static void main(String[] args) {
        errorLog = new ArrayList<>();
        String text = null;

        try {
            text = Main.getClipboard();
        } catch (IOException e) {
            errorLog.add("IOException detected: " + e.toString());
        } catch (UnsupportedFlavorException e) {
            errorLog.add("UnsupportedFlavorException detected: " + e.toString());
        }

        if (text == null) {
            errorLog.add("getClipboardContents() returned null");
            printLog();
            return;
        }

        List<Card> cards = Main.stringToAlphabetizedCards(text);
        if (cards == null || cards.size() < 1) {
            errorLog.add("stringToAlphabetizedCards() returned no cards");
            printLog();
            return;
        }

        text = "";
        for (Card card : cards)
            text += (card + "\n");

        if (text.trim().length() < 1) {
            errorLog.add("Unable to parse any cards");
            printLog();
            return;
        }
        printLog(Main.copyToClipboard(text), text);
    }

    /**
     * Creates a logfile and outputs the contents of the error log to said file, unless the error log is empty
     *
     * @param noChangeMade whether or not the clipboard was updated
     * @param clipboard    the string copied to the clipboard
     */
    private static void printLog(boolean noChangeMade, String clipboard) {
        if (errorLog.size() < 1) return;
        try {
            String str = "";
            for (String logStr : errorLog)
                str += logStr + "\n";

            if (noChangeMade || clipboard == null)
                str += "NO CHANGES MADE TO CLIPBOARD";
            else
                str += "Current clipboard contents:\n" + clipboard;
            BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt"));
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            System.out.println("IOException detected: " + e);
        }
    }

    /**
     * If any error messages are stored in the log, output them to a new file called "log.txt".
     */
    private static void printLog() {
        printLog(true, null);
    }

}
