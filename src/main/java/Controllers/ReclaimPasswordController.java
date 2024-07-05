package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;

public class ReclaimPasswordController {

    @FXML
    private TextArea taDescription;

    @FXML
    private Button btnSubmit;

    @FXML
    private void handleSubmit() {
        String description = taDescription.getText();

        if (description.isEmpty()) {
            showAlert("Erreur", "Veuillez décrire votre problème.", Alert.AlertType.ERROR);
            return;
        }

        // Envoyer la réclamation (par exemple, à un service de support)
        boolean success = submitReclamation(description);

        if (success) {
            showAlert("Succès", "Votre réclamation a été soumise avec succès.", Alert.AlertType.INFORMATION);
            // Effacer le champ de texte après la soumission réussie
            taDescription.clear();
        } else {
            showAlert("Erreur", "Une erreur s'est produite lors de la soumission de votre réclamation.", Alert.AlertType.ERROR);
        }
    }

    private boolean submitReclamation(String description) {
        // Ajouter la logique pour envoyer la réclamation (par exemple, enregistrer dans une base de données ou envoyer un email)
        // Retourner true si la réclamation est soumise avec succès, sinon false
        return true;
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
