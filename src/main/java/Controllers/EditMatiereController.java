package Controllers;

import Entities.Matiere;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditMatiereController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField coeffField;
    @FXML
    private TextField modeEvalField;

    private Stage dialogStage;
    private Matiere matiere;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;

        nomField.setText(matiere.getNom());
        coeffField.setText(String.valueOf(matiere.getCoeff()));
        modeEvalField.setText(matiere.getModeEval());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleSave() {
        // Vérification des champs vides
        String nom = nomField.getText();
        String coeffText = coeffField.getText();
        String modeEval = modeEvalField.getText();

        if (nom == null || nom.isEmpty() || modeEval == null || modeEval.isEmpty() || coeffText == null || coeffText.isEmpty()) {
            showErrorDialog("Champs obligatoires", "Veuillez remplir les champs obligatoires", "Tous les champs doivent être remplis.");
            return;
        }

        // Vérification du coefficient
        try {
            int coeff = Integer.parseInt(coeffText);
            if (coeff <= 0) {
                showErrorDialog("Coefficient invalide", "Le coefficient doit être un entier positif", "Veuillez entrer un coefficient valide.");
                return;
            }

            // Si toutes les vérifications passent, sauvegarde des valeurs
            matiere.setNom(nom);
            matiere.setCoeff(coeff);
            matiere.setModeEval(modeEval);

            okClicked = true;
            dialogStage.close();
        } catch (NumberFormatException e) {
            showErrorDialog("Coefficient invalide", "Le coefficient doit être un nombre entier", "Veuillez entrer un coefficient valide.");
        }
    }
    @FXML
    private void handleCancel() {

        dialogStage.close();
    }
    private void showErrorDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
