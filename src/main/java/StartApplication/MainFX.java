package StartApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Charger le fichier FXML principal
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AjouterFormation.fxml"));
        Parent root = fxmlLoader.load();

        // Configurer la scène
        Scene scene = new Scene(root);

        // Configurer la fenêtre principale (Stage)
        stage.setScene(scene);
        stage.setTitle("Ajouter Formations");
        stage.show();
    }
}
