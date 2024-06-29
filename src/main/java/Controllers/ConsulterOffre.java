package Controllers;

import Entities.Offre;
import Services.ServiceOffre;
import Utils.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


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
        private VBox container;
    @FXML
    private Pagination pagination;

    private final int itemsPerPage = 9;
    private final int columns = 3;
    private final int totalItems = 45; // Par exemple, 45 labels

//this code was commented
        @FXML
        public void initialize() throws SQLException {

            int n = 9; // Par exemple, afficher 9 labels
            int columns = 3;

            HBox hBox = new HBox(10);
            for (int i = 0; i < n; i++) {
                if (i % columns == 0) {
                    hBox = new HBox(10); // Nouveau HBox pour chaque nouvelle ligne
                    //container.getChildren().add(hBox);
                }

                Label label = new Label("Label " + (i + 1));
                label.getStyleClass().add("label-box");
                hBox.getChildren().add(label);
            }
//fin
            offresList = FXCollections.observableArrayList();
            loadOffresFromDatabase();
            displayOffres();

//this code was commented

            titreOffreColumn.setCellValueFactory(new PropertyValueFactory<>("titreOffre"));
            descriptionOffreColumn.setCellValueFactory(new PropertyValueFactory<>("descriptionOffre"));
            niveauEtudeColumn.setCellValueFactory(new PropertyValueFactory<>("niveauEtude"));
            dureeContratColumn.setCellValueFactory(new PropertyValueFactory<>("dureeContrat"));
            datePublicationColumn.setCellValueFactory(new PropertyValueFactory<>("datePublication"));
            entrepriseColumn.setCellValueFactory(new PropertyValueFactory<>("entreprise"));
            dateLimiteColumn.setCellValueFactory(new PropertyValueFactory<>("dateLimite"));

            tableView.setItems(offresList);


//fin
        }

    private void displayOffres() {
        // Supprimer tous les enfants du conteneur principal
        //container.getChildren().clear();

        int entreprisesParLigne = 3; // Nombre d'entreprises par ligne

        for (int i = 0; i < offresList.size(); i++) {
            Offre offre = offresList.get(i);

            // Calculer l'index de colonne et de ligne
            int colonne = i % entreprisesParLigne;
            int ligne = i / entreprisesParLigne;

            // Créer un VBox pour chaque entreprise
            VBox vbox = new VBox(10); // Espacement vertical entre les éléments
            vbox.setAlignment(Pos.CENTER); // Centrer le contenu

            // Créer une ImageView avec une image (remplacez "path_vers_image" par votre chemin d'accès réel)
            ImageView imageView = new ImageView(new Image(String.valueOf(getClass().getResource("/offre_stage_logo.png"))));
            imageView.setFitWidth(50); // Largeur de l'image
            imageView.setFitHeight(50); // Hauteur de l'image

            // Créer un Label avec le nom de l'entreprise
            Label label = new Label(offre.getEntreprise());
            label.getStyleClass().add("label-box");

            // Créer un bouton "Voir plus"
            Button voirPlusButton = new Button("Voir");
            voirPlusButton.setOnAction(event -> handleVoirPlus(offre));
            //this code was commented
            /*voirPlusButton.setOnAction(event -> {
                // Action à effectuer lors du clic sur "Voir plus"
                // Implémentez ici la logique pour voir plus de détails sur l'offre
                System.out.println("Voir plus sur : " + offre.getEntreprise());
            });

             */
            //fin

            // Ajouter les éléments au VBox
            vbox.getChildren().addAll(imageView, label, voirPlusButton);

            // Ajouter le VBox à la colonne appropriée
            /*if (container.getChildren().size() <= ligne) {
                HBox hbox = new HBox(10); // Espacement horizontal entre les VBox
                hbox.setAlignment(Pos.CENTER_LEFT); // Aligner les éléments à gauche
                container.getChildren().add(hbox);
            }



            ((HBox) container.getChildren().get(ligne)).getChildren().add(vbox);
            */

            // Redimensionner la largeur du VBox pour s'adapter à la taille de l'écran
            //this code was commented
           /* vbox.setPrefWidth(container.getWidth() / entreprisesParLigne - 20); // Ajustement pour l'espacement
            //fin
            vbox.setSpacing(10);
            */



        }
    }
    private void handleVoirPlus(Offre offre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VoirPlusOffre.fxml"));
            Parent root = loader.load();
            VoirPlusController controller = loader.getController();
            //this code was commented
            controller.setEntreprise(offre.getEntreprise());
            //fin code
            controller.setEntreprise(offre.getEntreprise());
            controller.setTitreOffre(offre.getTitreOffre());
            controller.setDescriptionOffre(offre.getDescriptionOffre());
            controller.setNiveauEtude(offre.getNiveauEtude());
            controller.setDureeContrat(offre.getDureeContrat());
            controller.setDatePublication(offre.getDatePublication().toString());
            controller.setDateLimite(offre.getDateLimite().toString());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Voir Plus");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        private void loadOffresFromDatabase() throws SQLException {
            List<Offre> offres = serviceOffre.findAll();
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
        private void handleUpdate(ActionEvent event) throws IOException {
            Offre selectedOffre = tableView.getSelectionModel().getSelectedItem();
            if (selectedOffre != null) {
                // Charger la nouvelle scène pour modifier une offre
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierOffre.fxml"));
                Parent root = loader.load();
                Scene newScene = new Scene(root);

                // Récupérer le contrôleur de la nouvelle scène
                ModifierOffre controller = loader.getController();

                // Passer l'offre sélectionnée au contrôleur de modification
                controller.setOffre(selectedOffre);
                controller.setServiceOffre(serviceOffre);

                // Récupérer la scène actuelle à partir de n'importe quel nœud de la scène
                Scene scene = tableView.getScene();

                // Récupérer la fenêtre principale (Stage) à partir de la scène
                Stage stage = (Stage) scene.getWindow();

                // Remplacer la scène actuelle par la nouvelle scène
                stage.setScene(newScene);
                stage.setTitle("Modifier Offre");
                stage.show();
            } else {
                showAlert("Aucune sélection", "Aucune offre sélectionnée.", "Veuillez sélectionner une offre à modifier.");
            }
        }


    private void showAlert(String title, String header, String content) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.showAndWait();
        }
    }

