package Controllers;

import Entities.Cours;
import Services.ServiceCours;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
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

    private ServiceCours serviceCours;
    private ObservableList<Cours> coursList;
    private File pdfFile;

    @FXML
    public void initialize() {
        serviceCours = new ServiceCours();
        coursList = FXCollections.observableArrayList();

        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        enseignantIdColumn.setCellValueFactory(new PropertyValueFactory<>("enseignantId"));
        chapitreIdColumn.setCellValueFactory(new PropertyValueFactory<>("idChapitre"));

        tableView.setItems(coursList);

        try {
            coursList.addAll(serviceCours.readAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdd() {
        String titre = titreField.getText();
        String description = descriptionField.getText();
        int enseignantId = Integer.parseInt(enseignantIdField.getText());
        int chapitreId = Integer.parseInt(chapitreIdField.getText());
        Blob pdfBlob = null;

        if (pdfFile != null) {
            try (FileInputStream fis = new FileInputStream(pdfFile)) {
                pdfBlob = new javax.sql.rowset.serial.SerialBlob(fis.readAllBytes());
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }

        Cours newCours = new Cours(titre, description, enseignantId, chapitreId, pdfBlob);

        try {
            serviceCours.ajouter(newCours);
            coursList.add(newCours);
            titreField.clear();
            descriptionField.clear();
            enseignantIdField.clear();
            chapitreIdField.clear();
            pdfFile = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        Cours selectedCours = tableView.getSelectionModel().getSelectedItem();
        if (selectedCours != null) {
            boolean okClicked = showEditDialog(selectedCours);
            if (okClicked) {
                try {
                    serviceCours.update(selectedCours);
                    tableView.refresh();
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
                coursList.remove(selectedCours);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean showEditDialog(Cours cours) {
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
            return false;
        }
    }

    @FXML
    private void handleChooseFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        pdfFile = fileChooser.showOpenDialog(null);
    }
}
