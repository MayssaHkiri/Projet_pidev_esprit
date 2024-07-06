package Controllers;

import Entities.Formation;
import Services.FormationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.sql.rowset.serial.SerialBlob;

public class ModifierFormation {

    @FXML
    private TextField titreField;
    @FXML
    private TextField descriptionField;
    @FXML
    private ImageView imageView;
    @FXML
    private DatePicker dateFormationPicker;

    private Formation formation;
    private FormationService formationService;

    private Blob selectedImageBlob;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public void setFormation(Formation formation) {
        this.formation = formation;

        titreField.setText(formation.getTitre());
        descriptionField.setText(formation.getDescription());

        if (formation.getImageFormation() != null) {
            try {
                // Convert Blob to Image
                byte[] imageBytes = formation.getImageFormation().getBytes(1, (int) formation.getImageFormation().length());
                Image image = new Image(new ByteArrayInputStream(imageBytes));
                imageView.setImage(image);
                selectedImageBlob = new SerialBlob(imageBytes); // Initialise selectedImageBlob
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (formation.getDateFormation() != null) {
            LocalDate localDate = LocalDate.parse(formation.getDateFormation(), DATE_FORMATTER);
            dateFormationPicker.setValue(localDate);
        }
    }

    public void setFormationService(FormationService formationService) {
        this.formationService = formationService;
    }

    @FXML
    void handleSelectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try (FileInputStream fis = new FileInputStream(selectedFile);
                 ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int readNum;
                while ((readNum = fis.read(buffer)) != -1) {
                    bos.write(buffer, 0, readNum);
                }
                byte[] imageBytes = bos.toByteArray();
                selectedImageBlob = new SerialBlob(imageBytes);

                // Afficher l'image dans l'ImageView
                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void modifierFormation() {
        String newTitre = titreField.getText();
        String newDescription = descriptionField.getText();
        LocalDate newDate = dateFormationPicker.getValue();
        String newDateString = null;

        if (newDate != null) {
            newDateString = DATE_FORMATTER.format(newDate);
        }

        if (newTitre == null || newTitre.trim().isEmpty() || !newTitre.matches("[a-zA-Z\\s]+")) {
            afficherPopup("Erreur de saisie", "Titre invalide", "Veuillez vérifier les données saisies.");
            return;
        }

        if (newDescription == null || newDescription.trim().isEmpty() || !newDescription.matches("[a-zA-Z\\s]+")) {
            afficherPopup("Erreur de saisie", "Description invalide", "Veuillez vérifier les données saisies.");
            return;
        }

        if (newDateString == null) {
            afficherPopup("Erreur de saisie", "Date invalide", "Veuillez vérifier les données saisies.");
            return;
        }

        if (formation != null) {
            formation.setTitre(newTitre);
            formation.setDescription(newDescription);
            formation.setDateFormation(newDateString);
            formation.setImageFormation(selectedImageBlob); // Update the image

            try {
                formationService.ModifierFormation(formation);
                afficherPopup("Succès", "Modification réussie", "La formation a été modifiée avec succès.");

                Stage stage = (Stage) dateFormationPicker.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                afficherPopup("Erreur", "Erreur de modification", "Une erreur est survenue lors de la modification de la formation.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    void annulerModification() {
        Stage stage = (Stage) dateFormationPicker.getScene().getWindow();
        stage.close();
    }

    private void afficherPopup(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
