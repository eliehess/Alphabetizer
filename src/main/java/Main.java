import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;

import java.net.URL;

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
        primaryStage.setTitle("Alphabetizer");
        primaryStage.setScene(new Scene(loadingRoot));
        primaryStage.show();
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
