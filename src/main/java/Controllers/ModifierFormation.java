package Controllers;

import Entities.Formation;
import Services.FormationService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;

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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (formation.getDateFormation() != null) {
            LocalDate localDate = formation.getDateFormation().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate();
            dateFormationPicker.setValue(localDate);
        }
    }

    public void setFormationService(FormationService formationService) {
        this.formationService = formationService;
    }

    @FXML
    void modifierFormation() {
        String newTitre = titreField.getText();
        String newDescription = descriptionField.getText();
        LocalDate newDate = dateFormationPicker.getValue();

        if (formation != null) {
            formation.setTitre(newTitre);
            formation.setDescription(newDescription);
            java.sql.Date sqlDate = java.sql.Date.valueOf(newDate);
            formation.setDateFormation(sqlDate);

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
