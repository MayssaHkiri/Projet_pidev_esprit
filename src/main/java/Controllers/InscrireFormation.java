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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs obligatoires.");
            alert.showAndWait();
        } else {
            // Créer un nouvel objet Inscription
            Inscription inscription = new Inscription(nomField.getText(), prenomField.getText(), emailField.getText(), telephoneField.getText());

            // Ajouter l'inscription à la base de données
            boolean success = inscriptionService.add(inscription);
            if (success) {
                // Afficher un message de succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Inscription réussie");
                alert.setHeaderText(null);
                alert.setContentText("Inscription avec succès !");
                alert.showAndWait();

                // Vider les champs après inscription réussie (facultatif)
                clearFields();

                // Fermer la fenêtre
                Stage stage = (Stage) nomField.getScene().getWindow();
                stage.close();
            } else {
                // Afficher un message d'erreur en cas d'échec de l'inscription
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("Une erreur est survenue lors de l'inscription. Veuillez réessayer.");
                alert.showAndWait();
            }
        }
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        telephoneField.clear();
    }
}
