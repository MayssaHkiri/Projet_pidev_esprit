package Controllers;

import Entities.User;
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
    private User authenticatedUser;

    @FXML
    private StackPane contentPane;

    public void setUser(User user) {
        this.authenticatedUser = user;
    }

    @FXML
    private void loadGestionCours(ActionEvent event) {
        loadPage("CoursView.fxml", authenticatedUser);
    }

    @FXML
    private void loadGestionMatieres(ActionEvent event) {
        loadPage("MatiereView.fxml", authenticatedUser);
    }

    @FXML
    private void loadProfil(ActionEvent event) {
        loadPage("UserProfile.fxml", authenticatedUser);
    }

    @FXML
    private void loadGestionQuiz(ActionEvent event) {
        loadPage("gestionQuiz.fxml", authenticatedUser);
    }

    private void loadPage(String fxmlFile, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlFile));
            Pane pane = loader.load();

            Object controller = loader.getController();
            if (controller instanceof UserProfileController) {
                ((UserProfileController) controller).setUser(user);
            }
            if (controller instanceof CoursViewController) {
                ((CoursViewController) controller).setUser(user);
            }
            if (controller instanceof MatiereViewController) {
                ((MatiereViewController) controller).setUser(user);
            }

            contentPane.getChildren().setAll(pane);
        } catch (IOException e) {
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
