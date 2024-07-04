// ListeMatiereQuizController.java
package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ListeMatiereQuizController {
    @FXML
    public void handleMatiereButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listeQuizEtudiant.fxml"));
            Parent root = loader.load();

            ListQuizEtudiantController controller = loader.getController();
            controller.setMatiere(((Button) event.getSource()).getId());

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}