package StartApplication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            // Charger le fichier FXML principal
            Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));

            // Configurer la scène
            Scene scene = new Scene(root);

            // Configurer la fenêtre principale (Stage)
            stage.setScene(scene);
            stage.setTitle("Consulter les formations"); // Titre de la fenêtre
            stage.show();
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'interface utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}