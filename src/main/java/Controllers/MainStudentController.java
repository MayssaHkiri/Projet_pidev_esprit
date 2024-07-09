package Controllers;

import Entities.User;
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
    private User authenticatedUser;

    public void setUser(User user) {
        this.authenticatedUser = user;
    }

    @FXML
    private StackPane contentPane;

    @FXML
    private void loadConsulterFormations() {
        loadPage("ConsulterFormation.fxml", authenticatedUser);
    }

    @FXML
    private void loadConsulterCours() {
        loadPage("EtudiantCoursView.fxml", authenticatedUser);
    }

    @FXML
    private void loadProfil() {
        loadPage("UserProfile.fxml", authenticatedUser);
    }

    @FXML
    private void loadPasserQuiz() {
        loadPage("", authenticatedUser);
    }

    public void loadConsulterOffres(ActionEvent actionEvent) {
        loadPage("etudiantVoir.fxml", authenticatedUser);
    }

    private void loadPage(String fxmlFile, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlFile));
            Pane pane = loader.load();

            Object controller = loader.getController();
            if (controller instanceof UserProfileController) {
                ((UserProfileController) controller).setUser(user);
            }
            if (controller instanceof EtudiantCoursViewController) {
                ((EtudiantCoursViewController) controller).setUser(user);
            }
            if (controller instanceof EtudiantCoursViewController) {
                ((EtudiantCoursViewController) controller).setUser(user);
            }
            if (controller instanceof ConsulterOffresParEtudiant ) {
                ((ConsulterOffresParEtudiant) controller).setUser(user) ;
            }


            contentPane.getChildren().setAll(pane);
        } catch (IOException e) {
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

    public void logoutAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        // Charge l'interface de connexion (LoginInterface.fxml)
        Parent root = FXMLLoader.load(getClass().getResource("/LoginInterface.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
