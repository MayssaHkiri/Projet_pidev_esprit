package Controllers;

import Entities.Chapitre;
import Entities.Cours;
import Entities.Matiere;
import Entities.User;
import Services.ServiceChapitre;
import Services.ServiceCours;
import Services.ServiceMatiere;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CoursViewController implements Initializable {
    private User authenticatedUser;

    public void setUser(User user) {
        this.authenticatedUser = user;

    }
    @FXML
    private TableView<Cours> tableView;
    @FXML
    private TableColumn<Cours, String> titreColumn;
    @FXML
    private TableColumn<Cours, String> descriptionColumn;
    @FXML
    private TableColumn<Cours, String> pdfColumn;
    @FXML
    private TextField titreField;
    @FXML
    private TextField descriptionField;
    @FXML
    private ChoiceBox<Matiere> matiereChoiceBox;
    @FXML
    private ChoiceBox<Chapitre> chapitreChoiceBox;

    private ServiceMatiere serviceMatiere;
    private ServiceCours serviceCours;
    private ServiceChapitre serviceChapitre;

    private ObservableList<Cours> coursList;
    private ObservableList<Matiere> matiereList;
    private ObservableList<Chapitre> chapitreList;

    private File pdfFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceCours = new ServiceCours();
        serviceMatiere = new ServiceMatiere();
        serviceChapitre = new ServiceChapitre();

        coursList = FXCollections.observableArrayList();
        matiereList = FXCollections.observableArrayList();
        chapitreList = FXCollections.observableArrayList();

        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        pdfColumn.setCellValueFactory(cellData -> {
            Blob pdfBlob = cellData.getValue().getPdfFile();
            return new SimpleStringProperty(pdfBlob != null ? "PDF File" : "No PDF");
        });

        tableView.setItems(coursList);

        try {
            coursList.addAll(serviceCours.readAll());
            matiereList.addAll(serviceMatiere.readAll());
            matiereChoiceBox.setItems(matiereList);

            matiereChoiceBox.setOnAction(event -> {
                Matiere selectedMatiere = matiereChoiceBox.getSelectionModel().getSelectedItem();
                if (selectedMatiere != null) {
                    loadChapitresForMatiere(selectedMatiere.getId());
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadChapitresForMatiere(int matiereId) {
        try {
            chapitreList.clear();
            chapitreList.addAll(serviceChapitre.readByMatiere(matiereId));
            chapitreChoiceBox.setItems(chapitreList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getIdMatiere(){
                Matiere selectedMatiere = matiereChoiceBox.getValue();
                if (selectedMatiere != null) {
                    return selectedMatiere.getId();
                }
                return 0;
    }

    @FXML
    private void handleAdd() {
        String titre = titreField.getText();
        String description = descriptionField.getText();
        Matiere selectedMatiere = matiereChoiceBox.getValue();
        Chapitre selectedChapitre = chapitreChoiceBox.getValue();
        Blob pdfBlob = null;

        // Check if all required fields are filled
        if (titre == null || titre.isEmpty() || description == null || description.isEmpty() ||
                selectedMatiere == null || selectedChapitre == null || pdfFile == null) {
            showAlert(AlertType.ERROR, "Ajout impossible", null, "Vous n'avez pas rempli les champs obligatoires !");
            return;
        }

        // If all fields are filled, proceed with adding the course
        if (pdfFile != null) {
            try (FileInputStream fis = new FileInputStream(pdfFile)) {
                pdfBlob = new javax.sql.rowset.serial.SerialBlob(fis.readAllBytes());
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }

        if (pdfBlob != null) {
            try {
                Cours cours = new Cours(titre, description, authenticatedUser.getId(), selectedChapitre.getId(), pdfBlob);
                serviceCours.ajouter(cours);
                coursList.add(cours);
                titreField.clear();
                descriptionField.clear();
                chapitreChoiceBox.getSelectionModel().clearSelection();
                matiereChoiceBox.getSelectionModel().clearSelection();

                showAlert(AlertType.INFORMATION, "Ajout réussi", null, "Le cours a été ajouté avec succès !");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Erreur", "Exception SQL", e.getMessage());
            }
        }
    }


    private void showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleUpdate() {
        Cours selectedCours = tableView.getSelectionModel().getSelectedItem();

        if (selectedCours == null) {
            showAlert(AlertType.ERROR, "Erreur", null, "Veuillez sélectionner un cours à modifier !");
            return;
        }
        Cours updatedCours = showEditDialog(selectedCours);
        tableView.refresh();
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


    public void HandleAjoutChapitre(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/AjoutChapitreView.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter Chapitre");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            AjoutChapitreView controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}

