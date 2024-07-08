package Controllers;

import Entities.Quiz;
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
        loadPage("CoursView.fxml", authenticatedUser, null);
    }

    @FXML
    private void loadGestionMatieres(ActionEvent event) {
        loadPage("MatiereView.fxml", authenticatedUser, null);
    }

    @FXML
    private void loadProfil(ActionEvent event) {
        loadPage("UserProfile.fxml", authenticatedUser, null);
    }

    @FXML
    private void loadGestionQuiz(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionQuiz.fxml"));
            Pane pane = loader.load();

            HomeQuizController homeQuizController = loader.getController();
            homeQuizController.setMainTeacherController(this); // Pass MainTeacherController to HomeQuizController

            contentPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPage(String fxmlFile, User user, Quiz quiz) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlFile));
            Pane pane = loader.load();

            Object controller = loader.getController();
            if (controller instanceof UserProfileController) {
                ((UserProfileController) controller).setUser(user);
            } else if (controller instanceof CoursViewController) {
                ((CoursViewController) controller).setUser(user);
            } else if (controller instanceof MatiereViewController) {
                ((MatiereViewController) controller).setUser(user);
            } else if (controller instanceof AfficherQuizController) {
                ((AfficherQuizController) controller).setQuiz(quiz);
            } else if (controller instanceof ModifierQuizController) {
                ((ModifierQuizController) controller).setQuiz(quiz);
            }

            contentPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
