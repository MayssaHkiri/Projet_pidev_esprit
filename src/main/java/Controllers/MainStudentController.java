package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainStudentController {
    @FXML
    private StackPane contentPane;

    @FXML
    private void loadConsulterFormations() {
        loadPage("");
    }

    @FXML
    private void loadConsulterCours() {
        loadPage("");
    }

    @FXML
    private void loadProfil() {
        loadPage("UserProfile.fxml");
    }

    @FXML
    private void loadPasserQuiz() {
        loadPage("");
    }

    public void loadConsulterOffres (ActionEvent actionEvent) {
        loadPage("etudiantVoir.fxml");
    }

    private void loadPage(String fxmlFile) {
        try {
            Pane pane = FXMLLoader.load(getClass().getResource("/" + fxmlFile));
            contentPane.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
