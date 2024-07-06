package Controllers;

import Utils.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainTeacherController {
    @FXML
    private StackPane contentPane;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
        StageManager.getInstance().setMainTeacherStage(stage);
    }

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
        loadPage("gestionQuiz.fxml");
    }
    private void loadPage(String fxmlFile) {
        try {
            Pane pane = FXMLLoader.load(getClass().getResource("/" + fxmlFile));
            contentPane.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleGestionQuiz(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionQuiz.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
