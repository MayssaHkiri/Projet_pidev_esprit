package Controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Entities.User;
import Services.UserService;

public class LoginInterface {

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    private UserService userService = new UserService();

    private static final String ESPRIT_EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@esprit\\.tn$";

    @FXML
    public void authentification(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (!isValidEspritEmail(email)) {
            showAlert("Invalid Email", "Please enter a valid Esprit email address (e.g., example@esprit.tn).");
            return;
        }

        try {
            User authenticatedUser = userService.authenticate(email, password);
            if (authenticatedUser != null) {
                if (authenticatedUser == UserService.DISACTIVE_USER) {
                    showAlert("Account Inactive", "Your account is inactive. Please contact support.");
                } else {
                    redirectUser(event, authenticatedUser);
                }
            } else {
                showAlert("Authentication Failed", "Invalid email or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while accessing the database.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while loading the interface.");
        }
    }

    private boolean isValidEspritEmail(String email) {
        Pattern pattern = Pattern.compile(ESPRIT_EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }

    private void redirectUser(ActionEvent event, User user) throws IOException {
        String role = user.getRole();
        String fxmlFile = "";

        switch (role) {
            case "admin":
                fxmlFile = "/mainAdmin.fxml";
                break;
            case "enseignant":
                fxmlFile = "/mainTeacher.fxml";
                break;
            case "etudiant":
                fxmlFile = "/mainStudent.fxml";
                break;
            default:
                showAlert("Authorization Error", "No interface found for role: " + role);
                return;
        }

        loadAndShowStage(event, fxmlFile, user);
    }

    private void loadAndShowStage(ActionEvent event, String fxmlFile, User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        Object controller = loader.getController();
        if (controller instanceof MainAdminController) {
            ((MainAdminController) controller).setUser(user);
        } else if (controller instanceof MainTeacherController) {
            ((MainTeacherController) controller).setUser(user);
        } else if (controller instanceof MainStudentController) {
            ((MainStudentController) controller).setUser(user);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void showForgetPasswordInterface(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ForgetPasswordInterface.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while loading the reset password interface.");
        }
    }

}
