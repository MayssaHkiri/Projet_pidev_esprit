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
    public void start(Stage stage) {
       //FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/ConsulterOffre.fxml"));
       FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("/etudiantVoir.fxml"));



        try {
/*
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Ajouter Offre");
            stage.show();
*/

           Parent root2 = fxmlLoader2.load();
            Scene scene2 = new Scene(root2);

            stage.setScene(scene2);
            stage.setTitle("Consulter Offre");
            stage.show();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
