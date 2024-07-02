package Controllers;

import Entities.Formation;
import Services.FormationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import javax.sql.rowset.serial.SerialBlob;

public class AjouterFormation {
    ObservableList<Formation> formationList = FXCollections.observableArrayList();
    private FormationService formationService = new FormationService();

    @FXML
    private ComboBox<String> btNiveau;

    @FXML
    private TextField tfDescription;

    @FXML
    private TextField tfTitre;

    @FXML
    private ImageView imageView;

    @FXML
    private DatePicker datePicker; // Ajout du DatePicker pour choisir la dateFormation

    private Blob selectedImageBlob;

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
        String niveau = btNiveau.getValue(); // Récupérer le niveau sélectionné dans la ComboBox
        String dateFormation = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); // Récupérer la date sélectionnée et la formater

        Formation formation = new Formation();
        formation.setTitre(titre);
        formation.setDescription(description);
        formation.setImageFormation(selectedImageBlob); // Ajouter l'image en tant que Blob à la formation
        formation.setDateFormation(dateFormation); // Ajouter la dateFormation en String à la formation

        try {
            boolean added = formationService.addFormation(formation); // Appel à la méthode du service pour ajouter la formation
            if (added) {
                System.out.println("Formation ajoutée avec succès");
                formationList.add(formation); // Ajouter à la liste locale (simulation)
                clearFields(); // Vider les champs après ajout
            } else {
                System.err.println("Erreur lors de l'ajout de la formation");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        tfTitre.clear();
        tfDescription.clear();
        btNiveau.setValue(null); // Clear the ComboBox selection
        imageView.setImage(null); // Clear the ImageView
        selectedImageBlob = null; // Reset the selected image blob
        datePicker.setValue(null); // Clear the DatePicker selection
    }
}
