package Controllers;

import Entities.User;
import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginInterface {
    UserService userService = new UserService();
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button resetButton;

    @FXML
    private Button submitButton;

    @FXML
    void authentification(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();
        try {
            User authenticatedUser = userService.authenticate(email, password);
            if (authenticatedUser != null) {
                // Si l'authentification réussit, naviguez vers UsersInterface.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UsersInterface.fxml"));
                Parent root = loader.load();
                emailField.getScene().setRoot(root);
            } else {
                System.out.println("Échec de l'authentification");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void changerMotDePasse(ActionEvent event) {

    }

}
