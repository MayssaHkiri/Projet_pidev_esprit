package Controllers;

import Entities.Inscription;
import Services.InscriptionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InscrireFormation {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField telephoneField;

    private int formationId; // ID de la formation à laquelle l'étudiant s'inscrit

    private InscriptionService inscriptionService = new InscriptionService();

    public void setFormationId(int formationId) {
        this.formationId = formationId;
    }

    @FXML
    public void handleInscription(ActionEvent event) {
        // Vérifier si tous les champs obligatoires sont remplis
        if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() ||
                emailField.getText().isEmpty() || telephoneField.getText().isEmpty()) {
            // Afficher un message d'erreur si un champ obligatoire est vide
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs obligatoires.");
        } else if (!isValidString(nomField.getText()) || !isValidString(prenomField.getText()) || !isValidEmail(emailField.getText()) || !isValidTelephone(telephoneField.getText())) {
            // Afficher un message d'erreur si les données ne sont pas valides
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez vérifier les données saisies.");
        } else {
            // Créer un nouvel objet Inscription
            Inscription inscription = new Inscription(nomField.getText(), prenomField.getText(), emailField.getText(), telephoneField.getText());

            // Ajouter l'inscription à la base de données
            boolean success = inscriptionService.add(inscription);
            if (success) {
                // Afficher un message de succès
                showAlert(Alert.AlertType.INFORMATION, "Inscription réussie", "Inscription avec succès !");

                // Vider les champs après inscription réussie (facultatif)
                clearFields();

                // Fermer la fenêtre
                Stage stage = (Stage) nomField.getScene().getWindow();
                stage.close();
            } else {
                // Afficher un message d'erreur en cas d'échec de l'inscription
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'inscription. Veuillez réessayer.");
            }
        }
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        telephoneField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean isValidString(String str) {
        return str != null && str.matches("[a-zA-Z]+");
    }

    private boolean isValidEmail(String email) {
        // Vérifier que l'email est au format stringprenom@string.com
        if (email == null) {
            return false;
        }
        // Expression régulière pour le format spécifique
        String regex = "^[a-zA-Z]+[a-zA-Z]*@[a-zA-Z]+\\.com$";
        return email.matches(regex);
    }

    private boolean isValidTelephone(String telephone) {
        return telephone != null && telephone.matches("\\d{1,8}");
    }
}
