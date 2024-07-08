package Controllers;

import Entities.Formation;
import Entities.User;
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
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GererFormation {
    private User authenticatedUser;

    public void setUser(User user) {
        this.authenticatedUser = user;
        System.out.println(user.getNom());
        System.out.println(user.getEmail());
    }

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
    private void handleAjouterFormation(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterFormation.fxml"));
            Parent root = loader.load();

            AjouterFormation controller = loader.getController();
            controller.setStage(stage);
            controller.setUser(authenticatedUser); // Passer l'utilisateur authentifié

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
    private void handleTelechargerExcel(ActionEvent event) {
        try {
            // Générer le fichier Excel avec les données nécessaires
            File excelFile = generateExcelFile();

            if (excelFile != null) {
                // Ouvrir le fichier Excel avec l'application par défaut pour le téléchargement
                Desktop.getDesktop().open(excelFile);

                showAlert(Alert.AlertType.INFORMATION,
                        "Success", "Téléchargement réussi",
                        "Fichier Excel téléchargé avec succès à l'emplacement : " + excelFile.getAbsolutePath());
            } else {
                showAlert(Alert.AlertType.ERROR,
                        "Erreur", "Erreur lors du téléchargement du fichier Excel.",
                        "Le fichier Excel n'a pas pu être généré.");
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR,
                    "Erreur", "Erreur lors du téléchargement du fichier Excel.",
                    e.getMessage());
        }
    }

    private File generateExcelFile() throws IOException {
        // Création du Workbook
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Formations");

        // Création de l'en-tête
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Titre de la formation");
        headerRow.createCell(1).setCellValue("Description de la formation");
        headerRow.createCell(2).setCellValue("Date de la formation");
        headerRow.createCell(3).setCellValue("Nombre d'étudiants inscrits");

        // Remplissage des données
        int rowNum = 1;
        for (Formation formation : formationsList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(formation.getTitre());
            row.createCell(1).setCellValue(formation.getDescription());
            row.createCell(2).setCellValue(formation.getDateFormation());
            // Récupération du nombre d'étudiants inscrits à cette formation
            int nombreEtudiantsInscrits = formation.getNombreEtudiantsInscrits();
            row.createCell(3).setCellValue(nombreEtudiantsInscrits);
        }

        // Écriture du contenu dans le fichier
        File file = new File("Formations.xlsx");
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'écriture dans le fichier Excel.", e.getMessage());
            file = null;
        }

        workbook.close(); // Fermeture du Workbook
        return file;
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
