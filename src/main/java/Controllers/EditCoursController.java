package Controllers;

import Entities.Cours;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class EditCoursController {

    @FXML
    private TextField titreField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField enseignantIdField;
    @FXML
    private TextField pdfFileField;

    private Stage dialogStage;
    private Cours cours;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCours(Cours cours) {
        this.cours = cours;

        titreField.setText(cours.getTitre());
        descriptionField.setText(cours.getDescription());
        /*enseignantIdField.setText(String.valueOf(cours.getEnseignantId()));*/
        // Set the PDF file path
       // pdfFileField.setText(cours.getPdfFile().getPath());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleChooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir un Fichier PDF");
        File selectedFile = fileChooser.showOpenDialog(dialogStage);
        if (selectedFile != null) {
            pdfFileField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleSave() {
        cours.setTitre(titreField.getText());
        cours.setDescription(descriptionField.getText());
        /*cours.setEnseignantId(Integer.parseInt(enseignantIdField.getText()));*/
        // Set the PDF file path
      //  cours.setPdfFile(new File(pdfFileField.getText()));

        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
