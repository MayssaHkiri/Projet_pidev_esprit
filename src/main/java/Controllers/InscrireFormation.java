package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class InscrireFormation {

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField telephoneField;

    private int id; // ID de la formation à laquelle l'étudiant s'inscrit

    public static void showStage(int id) {
    }

    public void setId(int id) {
        this.id = id;
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
            // Afficher un message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inscription réussie");
            alert.setHeaderText(null);
            alert.setContentText("Inscription avec succès !");
            alert.showAndWait();

            // Vider les champs après inscription réussie (facultatif)
            clearFields();
        }
    }

    private void clearFields() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        telephoneField.clear();
    }
}
