package Controllers;

import Entities.Offre;
import Services.ServiceOffre;
import Utils.DataSource;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class AjouterOffre {

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
    private ChoiceBox<String> niveauEtude;

    @FXML
    private Button validerBtn;

    @FXML
    private TextField email;

    @FXML
    public ImageView imgPrev;

    private ServiceOffre serviceOffre = new ServiceOffre();

    @FXML
    public void initialize() {
        // Initialiser la date de publication à la date d'aujourd'hui
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        datePublication.setText(LocalDate.now().format(formatter));

        // Initialiser les items des niveaux d'études
        niveauEtude.getItems().addAll("Première année", "Deuxième année");
        niveauEtude.getSelectionModel().selectFirst();

        validerBtn.setDisable(true);

        // Vérifier la validité des champs à chaque changement
        titreOffre.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        descriptionOffre.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        dureeContrat.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        datePublication.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        nomEntreprise.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        email.textProperty().addListener((observable, oldValue, newValue) -> checkFields());
        dateLimiteOffre.valueProperty().addListener((observable, oldValue, newValue) -> checkFields());
    }

    private void checkFields() {
        boolean fieldsFilled = !titreOffre.getText().isEmpty()
                && !descriptionOffre.getText().isEmpty()
                && niveauEtude.getValue() != null
                && !dureeContrat.getText().isEmpty()
                && !datePublication.getText().isEmpty()
                && !nomEntreprise.getText().isEmpty()
                && !email.getText().isEmpty()
                && dateLimiteOffre.getValue() != null;

        validerBtn.setDisable(!fieldsFilled);
    }

    private boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
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

    @FXML
    void ajouterDB(ActionEvent event) {
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

        if (!isValidEmail(email.getText())) {
            errorMessage.append("L'adresse email doit être au format string@esprit.tn.\n");
        }

        if (!validateDate(datePublication.getText(), dateLimiteOffre.getValue())) {
            errorMessage.append("La date limite doit être après la date de publication.\n");
        }

        if (errorMessage.length() > 0) {
            afficherPopup("Erreur de saisie", "Validation échouée", errorMessage.toString());
            return;
        }

        Offre nouvelleOffre = new Offre(
                1, // À remplacer par la logique d'attribution de l'ID
                titreOffre.getText(),
                descriptionOffre.getText(),
                niveauEtude.getValue(),
                dureeContrat.getText(),
                datePublication.getText(),
                nomEntreprise.getText(),
                dateLimiteOffre.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                email.getText()
        );

        try {
            serviceOffre.ajouter(nouvelleOffre);
            afficherPopup("Succès", "Ajout réussi", "L'offre a été ajoutée avec succès.");
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
            afficherPopup("Erreur", "Erreur d'ajout", "Une erreur est survenue lors de l'ajout de l'offre.");
        }
    }

    private void clearFields() {
        titreOffre.clear();
        descriptionOffre.clear();
        //niveauEtude.getSelectionModel().clearSelection();
        dureeContrat.clear();
        nomEntreprise.clear();
        email.clear();
        dateLimiteOffre.getEditor().clear(); // Efface la date sélectionnée dans le DatePicker

        validerBtn.setDisable(true);
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

    public TextField getTitreOffreTextField() {
        return titreOffre;
    }
}
