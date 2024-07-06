package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Entities.User;
import Services.UserService;
import java.io.IOException;
import java.sql.SQLException;

public class UserProfileController {

    @FXML
    private TextField tfName;
    @FXML
    private TextField tfPrenom;
    @FXML
    private TextField tfEmail;
    @FXML
    private Button btnModify;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnChangePassword;

    private UserService userService = new UserService();
    private int currentUserId = getCurrentUserId(); // Supposez que cette méthode obtient l'ID de l'utilisateur connecté

    @FXML
    public void initialize()  {
        // Initialiser les champs avec les données de l'utilisateur
        User user;
        try {
            user = userService.getUserById(currentUserId);
            if (user != null) {
                tfName.setText(user.getNom());
                tfPrenom.setText(user.getPrenom());
                tfEmail.setText(user.getEmail());
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de récupérer les informations de l'utilisateur.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la récupération des informations de l'utilisateur.");
        }
    }

    @FXML
    private void handleModify() {
        tfName.setEditable(true);
        tfPrenom.setEditable(true);
        tfEmail.setEditable(true);

        btnModify.setVisible(false);
        btnSave.setVisible(true);
    }

    @FXML
    private void handleSave() {
        String updatedName = tfName.getText();
        String updatedPrenom = tfPrenom.getText();
        String updatedEmail = tfEmail.getText();

        User user = new User(currentUserId, updatedName, updatedPrenom, updatedEmail);

        try {
            boolean success = userService.update(user);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Les informations utilisateur ont été mises à jour.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour des informations utilisateur.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la mise à jour des informations utilisateur.");
        }

        tfName.setEditable(false);
        tfPrenom.setEditable(false);
        tfEmail.setEditable(false);

        btnModify.setVisible(true);
        btnSave.setVisible(false);
    }

    @FXML
    private void handleChangePassword() {
        try {
            // Charger l'interface de modification de mot de passe
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChangePassword.fxml"));
            Parent root = loader.load();

            // Optionnel: Passer l'utilisateur courant au contrôleur de l'interface de modification de mot de passe
            ChangePasswordController changePasswordController = loader.getController();
            changePasswordController.setUserId(currentUserId); // Passez l'ID utilisateur actuel

            // Afficher la nouvelle scène
            Stage stage = (Stage) btnChangePassword.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur d'entrée/sortie
        }
    }

    private int getCurrentUserId() {
        // Implémentez la logique pour obtenir l'ID de l'utilisateur actuel
        return 12;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
