package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import Entities.User;
import Services.UserService;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    private User authenticatedUser;

    public void setUser(User user) {
        this.authenticatedUser = user;
        initializeUserProfile();
    }

    @FXML
    public void initialize() {
        // Cette méthode est appelée automatiquement après le chargement du FXML.
        // Nous n'avons pas encore les informations de l'utilisateur ici.
    }

    private void initializeUserProfile() {
        if (authenticatedUser != null) {
            tfName.setText(authenticatedUser.getNom());
            tfPrenom.setText(authenticatedUser.getPrenom());
            tfEmail.setText(authenticatedUser.getEmail());
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de récupérer les informations de l'utilisateur.");
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


        if (!updatedName.matches("[a-zA-Z]+") || !updatedPrenom.matches("[a-zA-Z]+")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom et le prénom doivent contenir uniquement des lettres alphabétiques.");
            return;
        }

        // Vérifier que l'adresse email se termine par "@esprit.tn"
        if (!updatedEmail.endsWith("@esprit.tn")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "L'adresse email doit se terminer par @esprit.tn.");
            return; // Arrêter la mise à jour
        }

        // Créer l'utilisateur mis à jour
        User updatedUser = new User(authenticatedUser.getId(), updatedName, updatedPrenom, updatedEmail);

        try {
            boolean success = userService.update(updatedUser);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Les informations utilisateur ont été mises à jour.");
                authenticatedUser = updatedUser; // Mettre à jour l'utilisateur authentifié
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de la mise à jour des informations utilisateur.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la mise à jour des informations utilisateur.");
        }

        // Désactiver l'édition et revenir à l'état initial des boutons
        tfName.setEditable(false);
        tfPrenom.setEditable(false);
        tfEmail.setEditable(false);

        btnModify.setVisible(true);
        btnSave.setVisible(false);
    }

    @FXML
    private void handleChangePassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChangePassword.fxml"));
            VBox root = loader.load();

            ChangePasswordController changePasswordController = loader.getController();
            // Passez l'utilisateur authentifié au contrôleur de changement de mot de passe
            changePasswordController.setUser(authenticatedUser);

            // Créez une nouvelle fenêtre pour afficher la modification de mot de passe
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Mot de Passe");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
