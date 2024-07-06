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
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/etudiantVoir.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));

        Parent root = null ;
        root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Welcome page");
        stage.setScene(scene);
        stage.show();
    }
}
