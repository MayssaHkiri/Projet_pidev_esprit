package StartApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConsulterFormation.fxml"));

        Parent root;
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Welcome page");
        stage.setScene(scene);
        stage.show();

    }
}