import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;

import java.io.IOException;
import java.net.URL;

public final class Main extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 550 + 24;
    private static Stage stage;
    private static URL scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setResizable(false);
        stage.setMaxHeight(HEIGHT);
        stage.setMinHeight(HEIGHT);
        stage.setMaxWidth(WIDTH);
        stage.setMinWidth(WIDTH);
        scene = getClass().getResource("/Main.fxml");
        loadMain();
        stage.show();
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    private void loadMain() throws IOException {
        Parent loadingRoot = FXMLLoader.load(scene);
        stage.setTitle("Alphabetizer");
        stage.setScene(new Scene(loadingRoot));
    }
}
