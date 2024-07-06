package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainTeacherController {
    @FXML
    private StackPane contentPane;

    @FXML
    private void loadGestionCours() {
        loadPage("CoursView.fxml");
    }

    @FXML
    private void loadGestionMatieres() {
        loadPage("MatiereView.fxml");
    }

    @FXML
    private void loadProfil() {
        loadPage("UserProfile.fxml");
    }

    public void loadConsulterOffres (ActionEvent actionEvent) {
        loadPage("");
    }

    public void loadGestionQuiz(ActionEvent actionEvent) {
        loadPage("");
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
