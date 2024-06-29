package Controllers;

import Entities.Cours;
import Entities.Matiere;
import Services.ServiceCours;
import Services.ServiceMatiere;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

public class CoursViewController {

    @FXML
    private TableView<Cours> tableView;
    @FXML
    private TableColumn<Cours, String> titreColumn;
    @FXML
    private TableColumn<Cours, String> descriptionColumn;
    @FXML
    private TableColumn<Cours, Integer> enseignantIdColumn;
    @FXML
    private TableColumn<Cours, Integer> chapitreIdColumn;
    @FXML
    private TextField titreField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField enseignantIdField;
    @FXML
    private TextField chapitreIdField;
    @FXML
    private TableColumn<Cours, String> pdfColumn;
    @FXML
    private ComboBox<Matiere> matiereComboBox;
    private ServiceMatiere serviceMatiere;
    private ServiceCours serviceCours;
    private ObservableList<Cours> coursList;
    private ObservableList<Matiere> matiereList;
    private File pdfFile;

    @FXML
    public void initialize() {
        serviceCours = new ServiceCours();
        serviceMatiere = new ServiceMatiere();
        coursList = FXCollections.observableArrayList();
        matiereList = FXCollections.observableArrayList();
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        // enseignantIdColumn.setCellValueFactory(new PropertyValueFactory<>("enseignantId"));
        // chapitreIdColumn.setCellValueFactory(new PropertyValueFactory<>("idChapitre"));
        pdfColumn.setCellValueFactory(cellData -> {
            Blob pdfBlob = cellData.getValue().getPdfFile();
            return new SimpleStringProperty(pdfBlob != null ? "PDF Present" : "No PDF");
        });

        tableView.setItems(coursList);
        try {
            coursList.addAll(serviceCours.readAll());
            matiereList.addAll(serviceMatiere.readAll());
            matiereComboBox.setItems(matiereList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdd() {
        String titre = titreField.getText();
        String description = descriptionField.getText();
        Matiere selectedMatiere = matiereComboBox.getValue();
        // int enseignantId = Integer.parseInt(enseignantIdField.getText()); // Commenté
        // int chapitreId = Integer.parseInt(chapitreIdField.getText()); // Commenté
        Blob pdfBlob = null;
        if (pdfFile != null) {
            try (FileInputStream fis = new FileInputStream(pdfFile)) {
                pdfBlob = new javax.sql.rowset.serial.SerialBlob(fis.readAllBytes());
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }

        if (titre != null && !titre.isEmpty() && description != null && !description.isEmpty() && pdfBlob != null) {
            try {


                Cours cours = new Cours(titre, description, pdfBlob);
                serviceCours.ajouter(cours);
                coursList.add(cours);
                titreField.clear();
                descriptionField.clear();
                //enseignantIdField.clear();
                //chapitreIdField.clear();
                matiereComboBox.getSelectionModel().clearSelection();
                // Show success alert
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Ajout réussi");
                alert.setHeaderText(null);
                alert.setContentText("Le cours a été ajouté avec succès !");
                alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Ajout impossible");
            alert.setHeaderText(null);
            alert.setContentText("Vous n'avez pas rempli les champs obligatoire !");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleUpdate() {
        Cours selectedCours = tableView.getSelectionModel().getSelectedItem();
        Blob pdfBlob = selectedCours.getPdfFile();

        if (selectedCours != null) {
            Cours cours = showEditDialog(selectedCours);
            if (cours != null) {
                try {

                    try (FileInputStream fis = new FileInputStream(pdfFile)) {
                        pdfBlob = new javax.sql.rowset.serial.SerialBlob(fis.readAllBytes());
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }

                    selectedCours.setPdfFile(cours.getPdfFile());
                    serviceCours.update(selectedCours);
                    tableView.refresh();

                    // Show success alert
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Modification réussie");
                    alert.setHeaderText(null);
                    alert.setContentText("Le cours a été modifié avec succès !");
                    alert.showAndWait();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleDelete() {
        Cours selectedCours = tableView.getSelectionModel().getSelectedItem();
        if (selectedCours != null) {
            try {
                serviceCours.supprimer(selectedCours);
                //  System.out.println("+++++++"+ selectedCours.getId());
                coursList.remove(selectedCours);

                // Show success alert
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Suppression réussie");
                alert.setHeaderText(null);
                alert.setContentText("Le cours a été supprimé avec succès !");
                alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Cours showEditDialog(Cours cours) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/EditCoursView.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modifier Cours");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            EditCoursController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCours(cours);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private void handleChooseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        pdfFile = fileChooser.showOpenDialog(null);
        if (pdfFile != null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Fichier sélectionné");
            alert.setHeaderText(null);
            alert.setContentText("Le fichier PDF " + pdfFile.getName() + " a été sélectionné avec succès !");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDownloadPDF() {
        Cours selectedCours = tableView.getSelectionModel().getSelectedItem();
        if (selectedCours != null && selectedCours.getPdfFile() != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    Blob pdfBlob = selectedCours.getPdfFile();
                    byte[] buffer = pdfBlob.getBytes(1, (int) pdfBlob.length());

                    fos.write(buffer);

                    // Show success alert
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Téléchargement réussi");
                    alert.setHeaderText(null);
                    alert.setContentText("Le fichier PDF a été téléchargé avec succès !");
                    alert.showAndWait();
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Show error alert if no course is selected or no PDF is available
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur de téléchargement");
            alert.setHeaderText(null);
            alert.setContentText("Aucun fichier PDF à télécharger pour le cours sélectionné.");
            alert.showAndWait();
        }
    }
}

