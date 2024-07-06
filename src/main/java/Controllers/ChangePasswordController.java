package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import Services.UserService;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ChangePasswordController {

    @FXML
    private Button btnBack;


    @FXML
    private PasswordField pfOldPassword;

    @FXML
    private PasswordField pfNewPassword;

    @FXML
    private PasswordField pfConfirmPassword;

    private UserService userService;
    private int userId ; // The ID of the user whose password is being changed

    public ChangePasswordController() {
        userService = new UserService();
    }

    @FXML
    private void initialize() {
        // Any initialization code can go here
    }

    @FXML
    private void handleChangePassword() {
        String oldPassword = pfOldPassword.getText();
        String newPassword = pfNewPassword.getText();
        String confirmPassword = pfConfirmPassword.getText();

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Erreur", "Les nouveaux mots de passe ne correspondent pas.", Alert.AlertType.ERROR);
            return;
        }

        try {
            boolean success = userService.resetPassword(userId, oldPassword, newPassword);
            if (success) {
                showAlert("Succès", "Le mot de passe a été modifié avec succès.", Alert.AlertType.INFORMATION);
                // Clear the password fields after successful change
                pfOldPassword.clear();
                pfNewPassword.clear();
                pfConfirmPassword.clear();
            } else {
                showAlert("Erreur", "L'ancien mot de passe est incorrect ou une erreur s'est produite.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la modification du mot de passe.", Alert.AlertType.ERROR);
        }
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBack() {
        try {
            // Charger l'interface UserProfile.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserProfile.fxml"));
            Parent root = loader.load();

            // Afficher la nouvelle scène dans la fenêtre principale
            Stage stage = (Stage) btnBack.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur d'entrée/sortie si le chargement échoue
            showAlert("Erreur", "Impossible de charger l'interface UserProfile.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
