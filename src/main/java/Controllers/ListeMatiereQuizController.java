// ListeMatiereQuizController.java
package Controllers;

import Utils.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    public void handleAccueil(ActionEvent actionEvent) {
        Stage mainStudentStage = StageManager.getInstance().getMainStudentStage();
        if (mainStudentStage != null && mainStudentStage.isShowing()) {
            ((Node) actionEvent.getSource()).getScene().getWindow().hide();
        } else {
            ((Node) actionEvent.getSource()).getScene().getWindow().hide();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainStudent.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                StageManager.getInstance().setMainStudentStage(stage);
            } catch (IOException e) {
                System.out.println("Error while loading mainTeacher.fxml: " + e.getMessage());
            }
        }
    }
}