package Controllers;

import Entities.Matiere;
import javafx.fxml.FXML;
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
        matiere.setNom(nomField.getText());
        matiere.setCoeff(Integer.parseInt(coeffField.getText()));
        matiere.setModeEval(modeEvalField.getText());

        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {

        dialogStage.close();
    }
}
