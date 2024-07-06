package Controllers;

import Entities.Formation;
import Services.FormationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialBlob;
import java.time.format.DateTimeFormatter;

public class AjouterFormation {
    private ObservableList<Formation> formationList = FXCollections.observableArrayList();
    private FormationService formationService = new FormationService();
    private Stage stage;

    @FXML
    private TextField tfTitre;

    @FXML
    private TextField tfDescription;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ImageView imageView;

    private Blob selectedImageBlob;

    public void setStage(Stage stage) {
        this.stage = stage;
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
    void handleAjouterFormation(ActionEvent event) {
        String titre = tfTitre.getText();
        String description = tfDescription.getText();
        String dateFormation = null;
        if (datePicker.getValue() != null) {
            dateFormation = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }

        if (!isValidString(titre)) {
            showAlert("Erreur de saisie", "Titre invalide", "Le titre doit être une chaîne de caractères valide (lettres, espaces, et caractères accentués).");
            return;
        }

        if (!isValidString(description)) {
            showAlert("Erreur de saisie", "Description invalide", "La description doit être une chaîne de caractères valide (lettres, espaces, et caractères accentués).");
            return;
        }

        if (dateFormation == null) {
            showAlert("Erreur de saisie", "Date invalide", "Veuillez vérifier les données saisies.");
            return;
        }

        Formation formation = new Formation();
        formation.setTitre(titre);
        formation.setDescription(description);
        formation.setImageFormation(selectedImageBlob);
        formation.setDateFormation(dateFormation);

        try {
            boolean added = formationService.addFormation(formation);
            if (added) {
                showAlert("Succès", "Formation ajoutée", "La formation a été ajoutée avec succès.");
                clearFields();
                closeWindow();
            } else {
                showAlert("Erreur", "Erreur d'ajout", "Une erreur s'est produite lors de l'ajout de la formation.");
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur SQL", e.getMessage());
        }
    }

    private boolean isValidString(String str) {
        return str != null && str.trim().length() > 0 && str.matches("[a-zA-Zà-ÿÀ-ß\\s]+");
    }

    private void clearFields() {
        tfTitre.clear();
        tfDescription.clear();
        imageView.setImage(null);
        selectedImageBlob = null;
        datePicker.setValue(null);
    }

    private void closeWindow() {
        if (stage != null) {
            stage.close();
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
