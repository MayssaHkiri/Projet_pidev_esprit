package Controllers;

import Entities.Formation;
import Services.FormationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ConsulterFormation {
    @FXML
    private TableView<Formation> tableView;
    @FXML
    private TableColumn<Formation, String> titreFormationColumn;
    @FXML
    private TableColumn<Formation, String> descriptionFormationColumn;
    @FXML
    private TableColumn<Formation, String> dateFormationColumn;
    @FXML
    private TableColumn<Formation, Integer> nombreEtudiantsInscritsColumn; // Nouvelle colonne pour le nombre d'étudiants inscrits

    private ObservableList<Formation> formationsList;
    private FormationService serviceFormation = new FormationService();

    @FXML
    public void initialize() {
        formationsList = FXCollections.observableArrayList();
        try {
            loadFormationsFromDatabase();
            displayFormations();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Erreur lors du chargement des formations depuis la base de données.", e.getMessage());
        }
    }

    private void displayFormations() {
        titreFormationColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionFormationColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateFormationColumn.setCellValueFactory(new PropertyValueFactory<>("dateFormation"));
        // Assurez-vous que la PropertyValueFactory correspond à votre modèle de données

        // Bind la nouvelle colonne avec le nombre d'étudiants inscrits
        nombreEtudiantsInscritsColumn.setCellValueFactory(new PropertyValueFactory<>("nombreEtudiantsInscrits"));

        tableView.setItems(formationsList);

        // Charger le nombre d'étudiants inscrits pour chaque formation
        formationsList.forEach(formation -> {
            try {
                int nombreEtudiants = serviceFormation.getNumberOfStudentsEnrolled(formation.getId());
                formation.setNombreEtudiantsInscrits(nombreEtudiants);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Erreur lors du chargement du nombre d'étudiants inscrits pour la formation.", e.getMessage());
            }
        });
    }

    private void loadFormationsFromDatabase() throws SQLException {
        List<Formation> formations = serviceFormation.getAllFormations();
        formationsList.setAll(formations);
    }

    @FXML
    private void handleRetour(ActionEvent event) {
        closeWindow();
    }

    @FXML
    private void handleModifierFormation(ActionEvent event) {
        Formation selectedFormation = tableView.getSelectionModel().getSelectedItem();
        if (selectedFormation != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierFormation.fxml"));
                Parent root = loader.load();
                ModifierFormation controller = loader.getController();
                controller.setFormation(selectedFormation);
                controller.setFormationService(serviceFormation);

                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Modifier Formation");
                stage.show();

                stage.setOnHidden((WindowEvent we) -> {
                    try {
                        loadFormationsFromDatabase();
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Erreur lors du rechargement des formations depuis la base de données.", e.getMessage());
                    }
                });
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur d'ouverture", "Erreur lors de l'ouverture de la fenêtre de modification de formation.", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Aucune sélection", "Aucune formation sélectionnée.", "Veuillez sélectionner une formation à modifier.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.close();
    }
}
