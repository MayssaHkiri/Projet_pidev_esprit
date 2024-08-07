package Controllers;

import Entities.Chapitre;
import Entities.Cours;
import Services.ServiceCours;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

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
    private File pdfFile;


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

    public Cours isOkClicked() {
        return cours;
    }

    @FXML
    private void handleChooseFile(ActionEvent actionEvent) {
        Blob pdfBlob = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        pdfFile = fileChooser.showOpenDialog(null);
        if (pdfFile != null) {
            try (FileInputStream fis = new FileInputStream(pdfFile)) {
                pdfBlob = new javax.sql.rowset.serial.SerialBlob(fis.readAllBytes());
                cours.setPdfFile(pdfBlob);

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
        if (pdfFile != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Fichier sélectionné");
            alert.setHeaderText(null);
            alert.setContentText("Le fichier PDF " + pdfFile.getName() + " a été sélectionné avec succès !");
            alert.showAndWait();
        }

    }
   /* @FXML
    private ChoiceBox<Chapitre> chapitreChoiceBox;*/

    @FXML
    private void handleSave() throws SQLException {
        cours.setTitre(titreField.getText());

        cours.setDescription(descriptionField.getText());
        //Chapitre selectedChapitre = chapitreChoiceBox.getValue();
        //int id=selectedChapitre.getId();

        if (pdfFile != null) {
            try (FileInputStream fis = new FileInputStream(pdfFile)) {
                Blob pdfBlob = new javax.sql.rowset.serial.SerialBlob(fis.readAllBytes());
                cours.setPdfFile(pdfBlob);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }

        ServiceCours serviceCours=new ServiceCours();
        Cours newCours=serviceCours.findbyId(cours.getId());
        System.out.println("cours="+cours);
        //cours.setIdChapitre(cour.getIdChapitre());
        serviceCours.UpdateCours(cours);

        okClicked = true;
        dialogStage.close();



    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
