package Controllers;

import Entities.Offre;
import Services.ServiceOffre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class ConsulterOffre {

        @FXML
        private TableView<Offre> tableView;
        @FXML
        private TableColumn<Offre, String> titreOffreColumn;
        @FXML
        private TableColumn<Offre, String> descriptionOffreColumn;
        @FXML
        private TableColumn<Offre, String> niveauEtudeColumn;
        @FXML
        private TableColumn<Offre, String> dureeContratColumn;
        @FXML
        private TableColumn<Offre, String> datePublicationColumn;
        @FXML
        private TableColumn<Offre, String> entrepriseColumn;
        @FXML
        private TableColumn<Offre, String> dateLimiteColumn;

        private ObservableList<Offre> offresList;

        private  ServiceOffre serviceOffre=new ServiceOffre();


        @FXML
        public void initialize() throws SQLException {
            offresList = FXCollections.observableArrayList();
            loadOffresFromDatabase();

            titreOffreColumn.setCellValueFactory(new PropertyValueFactory<>("titreOffre"));
            descriptionOffreColumn.setCellValueFactory(new PropertyValueFactory<>("descriptionOffre"));
            niveauEtudeColumn.setCellValueFactory(new PropertyValueFactory<>("niveauEtude"));
            dureeContratColumn.setCellValueFactory(new PropertyValueFactory<>("dureeContrat"));
            datePublicationColumn.setCellValueFactory(new PropertyValueFactory<>("datePublication"));
            entrepriseColumn.setCellValueFactory(new PropertyValueFactory<>("entreprise"));
            dateLimiteColumn.setCellValueFactory(new PropertyValueFactory<>("dateLimite"));

            tableView.setItems(offresList);
        }

        private void loadOffresFromDatabase() throws SQLException {
            List<Offre> offres = serviceOffre.findAll();
            //System.out.println(offres);
            offresList.setAll(offres);
        }
    @FXML
    private TextField titreOffre;


        @FXML
        private void handleAdd(ActionEvent event) throws IOException {
            // Récupérer la scène actuelle à partir de n'importe quel nœud de la scène
            Scene scene = tableView.getScene();

            // Récupérer la fenêtre principale (Stage) à partir de la scène
            Stage stage = (Stage) scene.getWindow();

            // Logique pour ajouter une nouvelle offre (à implémenter)
            // Ajouter la nouvelle offre à la base de données puis à la liste `offresList`
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterOffre.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);

            // Récupérer le contrôleur de la nouvelle scène
            AjouterOffre controller = loader.getController();

            // Récupérer le TextField titreOffre depuis le contrôleur de la nouvelle scène
            TextField titreOffreTextField = controller.getTitreOffreTextField();

            // Utiliser le TextField titreOffreTextField comme nécessaire

            // Remplacer la scène actuelle par la nouvelle scène
            stage.setScene(newScene);
            stage.setTitle("Ajouter Offre");
            stage.show();
        }


    @FXML
    private void handleDelete(ActionEvent event) {
        Offre selectedOffre = tableView.getSelectionModel().getSelectedItem();
        if (selectedOffre != null) {
            try {
                serviceOffre.supprimer(selectedOffre);
                offresList.remove(selectedOffre);
                showAlert("Suppression réussie", "Offre supprimée avec succès.", "");
            } catch (SQLException e) {
                showAlert("Erreur lors de la suppression", "Une erreur s'est produite lors de la suppression de l'offre.", e.getMessage());
            }
        } else {
            showAlert("Aucune sélection", "Aucune offre sélectionnée.", "Veuillez sélectionner une offre à supprimer.");
        }


    }

        @FXML
        private void handleUpdate(MouseEvent event) {

        }


    private void showAlert(String title, String header, String content) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.showAndWait();
        }
    }

