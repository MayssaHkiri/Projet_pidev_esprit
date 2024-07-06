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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GererFormation {

    @FXML
    private TableView<Formation> tableView;
    @FXML
    private TableColumn<Formation, String> titreFormationColumn;
    @FXML
    private TableColumn<Formation, String> descriptionFormationColumn;
    @FXML
    private TableColumn<Formation, String> dateFormationColumn;
    @FXML
    private TextField searchField;

    private ObservableList<Formation> formationsList;
    private FormationService serviceFormation = new FormationService();
    private MainController mainController;

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

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void displayFormations() {
        titreFormationColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionFormationColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateFormationColumn.setCellValueFactory(new PropertyValueFactory<>("dateFormation"));

        tableView.setItems(formationsList);
    }

    private void loadFormationsFromDatabase() throws SQLException {
        List<Formation> formations = serviceFormation.getAllFormations();
        formationsList.setAll(formations);
    }

    @FXML
    private void handleAjouterFormation(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterFormation.fxml"));
            Parent root = loader.load();

            AjouterFormation controller = loader.getController();
            controller.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Ajouter Formation");
            stage.show();

            stage.setOnHidden((WindowEvent we) -> {
                try {
                    loadFormationsFromDatabase();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Erreur lors du rechargement des formations depuis la base de données.", e.getMessage());
                }
            });
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur d'ouverture", "Erreur lors de l'ouverture de la fenêtre d'ajout de formation.", e.getMessage());
        }
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

    @FXML
    private void handleSupprimerFormation(ActionEvent event) {
        Formation selectedFormation = tableView.getSelectionModel().getSelectedItem();
        if (selectedFormation != null) {
            try {
                if (serviceFormation.SupprimerFormation(selectedFormation.getId())) {
                    formationsList.remove(selectedFormation);
                    showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Formation supprimée avec succès.", "");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur lors de la suppression", "La formation n'a pas pu être supprimée.", "Veuillez réessayer plus tard.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Une erreur s'est produite lors de la suppression de la formation.", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Aucune sélection", "Aucune formation sélectionnée.", "Veuillez sélectionner une formation à supprimer.");
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String searchTerm = searchField.getText();
        if (searchTerm != null && !searchTerm.isEmpty()) {
            try {
                List<Formation> searchResults = serviceFormation.searchFormations(searchTerm);
                formationsList.setAll(searchResults);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de recherche", "Une erreur s'est produite lors de la recherche de formations.", e.getMessage());
            }
        } else {
            try {
                loadFormationsFromDatabase();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de chargement", "Erreur lors du rechargement des formations depuis la base de données.", e.getMessage());
            }
        }
    }

    @FXML
    private void handleLoadGestionCoursPage(ActionEvent event) {
        mainController.loadGestionCoursPage();
    }

    @FXML
    private void handleLoadGestionFormationsPage(ActionEvent event) {
        mainController.loadGestionFormationsPage();
    }

    @FXML
    private void handleLoadGestionOffresPage(ActionEvent event) {
        mainController.loadGestionOffresPage();
    }

    @FXML
    private void handleLoadGestionUtilisateursPage(ActionEvent event) {
        mainController.loadGestionUtilisateursPage();
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
}
