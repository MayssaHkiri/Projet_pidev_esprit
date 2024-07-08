package Controllers;
import Entities.Offre;
import Services.ServiceOffre;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ModifierOffre {

    @FXML
    private DatePicker dateLimiteOffre;

    @FXML
    private TextField datePublication;

    @FXML
    private TextField descriptionOffre;

    @FXML
    private TextField dureeContrat;

    @FXML
    private TextField nomEntreprise;

    @FXML
    private TextField titreOffre;

    @FXML
    private TextField emailEntreprise;


    @FXML
    private ChoiceBox<String> niveauEtude;

    private ServiceOffre serviceOffre = new ServiceOffre();
    private Offre offre;

    @FXML
    public void initialize() {
        niveauEtude.getItems().addAll("Première année", "Deuxième année");
        niveauEtude.getSelectionModel().selectFirst();
    }

    public void setServiceOffre(ServiceOffre serviceOffre) {
        this.serviceOffre = serviceOffre;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;
        // Populate fields with offre data
        titreOffre.setText(offre.getTitreOffre());
        descriptionOffre.setText(offre.getDescriptionOffre());
        niveauEtude.setValue(offre.getNiveauEtude());
        dureeContrat.setText(offre.getDureeContrat());
        datePublication.setText(offre.getDatePublication());
        nomEntreprise.setText(offre.getEntreprise());
        emailEntreprise.setText(offre.getEmail());

        // Convertir la date de limite de l'offre en LocalDate et l'assigner à dateLimiteOffre
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateLimite = LocalDate.parse(offre.getDateLimite(), formatter);
        dateLimiteOffre.setValue(dateLimite);
    }

    @FXML
    void modifierDB(ActionEvent event) {
        StringBuilder errorMessage = new StringBuilder();

        if (!isValidString(titreOffre.getText())) {
            errorMessage.append("Le titre doit être une chaîne de caractères valide (lettres, espaces, et caractères accentués).\n");
        }

        if (!isValidString(nomEntreprise.getText())) {
            errorMessage.append("Le nom d'entreprise doit être une chaîne de caractères valide (lettres, espaces, et caractères accentués).\n");
        }

        if (!isValidString(descriptionOffre.getText())) {
            errorMessage.append("La description doit être une chaîne de caractères valide (lettres, espaces, et caractères accentués).\n");
        }

        if (datePublication.getText() == null) {
            errorMessage.append("Veuillez vérifier les données saisies pour la date de publication.\n");
        }

        if (!isValidEmail(emailEntreprise.getText())) {
            errorMessage.append("L'adresse email doit être au format string@esprit.tn.\n");
        }

        if (!validateDate(datePublication.getText(), dateLimiteOffre.getValue())) {
            errorMessage.append("La date limite doit être après la date de publication.\n");
        }

        if (errorMessage.length() > 0) {
            afficherPopup("Erreur de saisie", "Validation échouée", errorMessage.toString());
            return;
        }

        // Update the offer object with the modified data from fields
        offre.setTitreOffre(titreOffre.getText());
        offre.setDescriptionOffre(descriptionOffre.getText());
        offre.setNiveauEtude(niveauEtude.getValue());
        offre.setDureeContrat(dureeContrat.getText());
        offre.setDatePublication(datePublication.getText());
        offre.setEntreprise(nomEntreprise.getText());
        offre.setEmail(emailEntreprise.getText());

        // Convertir la date limite de LocalDate à String au format dd/MM/yyyy
        String dateLimiteFormatted = dateLimiteOffre.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        offre.setDateLimite(dateLimiteFormatted);

        try {
            // Appeler le service pour mettre à jour l'offre dans la base de données
            serviceOffre.update(offre);
            afficherPopup("Succès", "Modification réussie", "L'offre a été modifiée avec succès.");

            // Fermer la fenêtre après la modification
            Stage stage = (Stage) titreOffre.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            afficherPopup("Erreur", "Erreur de modification", "Une erreur est survenue lors de la modification de l'offre.");
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

    public void handleCancel(ActionEvent actionEvent) {
        // Fermer la fenêtre
        Stage stage = (Stage) titreOffre.getScene().getWindow();
        stage.close();
    }
    private boolean validateDate(String publicationDate, LocalDate limiteDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate pubDate = LocalDate.parse(publicationDate, formatter);
        return limiteDate.isAfter(pubDate);
    }

    private boolean isValidString(String str) {
        return str != null && str.trim().length() > 0 && str.matches("[a-zA-Zà-ÿÀ-ß\\s]+");
    }

    private boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        String regex = "^[a-zA-Z]+@[esprit]+\\.[tn]+$";
        return email.matches(regex);
    }
}
