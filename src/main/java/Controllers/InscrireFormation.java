package Controllers;

import Entities.Inscription;
import Services.InscriptionService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    @FXML
    private ImageView formationImageView;

    private int formationId; // ID de la formation à laquelle l'étudiant s'inscrit
    private Image formationImage; // Image de la formation sélectionnée

    private InscriptionService inscriptionService = new InscriptionService();

    public void setFormationId(int formationId) {
        this.formationId = formationId;
    }

    public void setFormationImage(Image image) {
        this.formationImage = image;
        formationImageView.setImage(image);
    }

    @FXML
    public void handleInscription() {
        StringBuilder errorMessage = new StringBuilder();

        // Vérifier si tous les champs obligatoires sont remplis
        if (nomField.getText().isEmpty() || prenomField.getText().isEmpty() ||
                emailField.getText().isEmpty() || telephoneField.getText().isEmpty()) {
            errorMessage.append("Veuillez remplir tous les champs obligatoires.\n");
        }

        // Valider chaque champ et ajouter des messages d'erreur spécifiques
        if (!isValidString(nomField.getText())) {
            errorMessage.append("Nom invalide.\n");
        }

        if (!isValidString(prenomField.getText())) {
            errorMessage.append("Prénom invalide.\n");
        }

        if (!isValidEmail(emailField.getText())) {
            errorMessage.append("L'adresse email doit être au format string@esprit.tn.\n");
        }

        if (!isValidTelephone(telephoneField.getText())) {
            errorMessage.append("Le numéro de téléphone doit contenir 8 chiffres.\n");
        }

        // Afficher les messages d'erreur s'il y en a
        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur", errorMessage.toString());
            return;
        }

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
        // Vérifier que l'email est au format string@esprit.tn
        if (email == null) {
            return false;
        }
        // Expression régulière pour le format spécifique
        String regex = "^[a-zA-Z]+@[esprit]+\\.[tn]+$";
        return email.matches(regex);
    }

    private boolean isValidTelephone(String telephone) {
        return telephone != null && telephone.matches("\\d{8}");
    }
}
