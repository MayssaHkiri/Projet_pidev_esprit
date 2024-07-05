package Controllers;

import java.io.IOException;
import java.sql.SQLException;

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

    @FXML
    public void authentification(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            User authenticatedUser = userService.authenticate(email, password);
            if (authenticatedUser != null) {
                redirectUser(event, authenticatedUser);
            } else {
                showAlert("Authentication Failed", "Invalid email or password.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while accessing the database.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void redirectUser(ActionEvent event, User user) throws IOException {
        String role = user.getRole();
        String fxmlFile = "";

        switch (role) {
            case "admin":
                fxmlFile = "/WelcomeAdmin.fxml" ;
                break;
            case "user":
                fxmlFile = "";
                break;
            case "etudiant" :
                fxmlFile = "" ;
                break ;
            default:
                showAlert("Authorization Error", "No interface found for role: " + role);
                return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        // Pass the authenticated user to the new controller
        AdminInterface welcomeAdminController = loader.getController();
        welcomeAdminController.setUser(user);

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
}
