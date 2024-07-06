package Controllers;

import Utils.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainStudentController {
    @FXML
    private StackPane contentPane;
    private Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
        StageManager.getInstance().setMainStudentStage(stage);
    }

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
    public void handlePasserQuiz(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/listeMatiereQuiz.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
