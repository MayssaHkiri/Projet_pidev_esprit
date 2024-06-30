package Controllers;
import Entities.Formation;
import Services.FormationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class ModifierFormation {

    @FXML
    private DatePicker dateFormationPicker;

    private Formation formation;
    private FormationService formationService = new FormationService();

    @FXML
    public void initialize() {
        // Rien de spécial à initialiser pour le DatePicker dans ce cas
    }

    public void setFormation(Formation formation) {
        this.formation = formation;

        // Pré-remplir le DatePicker avec la dateFormation existante
        if (formation.getDateFormation() != null) {
            // Convertir java.sql.Date en LocalDate pour l'affichage dans le DatePicker
            Instant instant = Instant.ofEpochMilli(formation.getDateFormation().getTime());
            LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
            dateFormationPicker.setValue(localDate);
        }
    }

    @FXML
    void modifierFormation(ActionEvent event) {
        // Récupérer la nouvelle date de formation depuis le DatePicker
        LocalDate newDate = dateFormationPicker.getValue();

        // Mettre à jour la date de formation dans l'objet formation
        if (formation != null) {
            // Convertir LocalDate en java.sql.Date pour l'enregistrement en base de données
            Instant instant = newDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            java.sql.Date sqlDate = java.sql.Date.from(instant);
            formation.setDateFormation(sqlDate);
        }

        try {
            // Appeler le service pour mettre à jour la formation dans la base de données
            if (formation != null) {
                formationService.modifierFormation(formation);
                afficherPopup("Succès", "Modification réussie", "La date de formation a été modifiée avec succès.");

                // Fermer la fenêtre après la modification
                Stage stage = (Stage) dateFormationPicker.getScene().getWindow();
                stage.close();
            } else {
                afficherPopup("Erreur", "Erreur de modification", "Impossible de trouver la formation à modifier.");
            }
        } catch (SQLException e) {
            afficherPopup("Erreur", "Erreur de modification", "Une erreur est survenue lors de la modification de la date de formation.");
            e.printStackTrace();
        }
    }

    private void afficherPopup(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void annulerModification(ActionEvent event) {
        // Fermer la fenêtre sans effectuer de modification
        Stage stage = (Stage) dateFormationPicker.getScene().getWindow();
        stage.close();
    }
}
