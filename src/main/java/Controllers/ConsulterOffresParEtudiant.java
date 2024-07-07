package Controllers;

import Entities.Offre;
import Entities.User;
import Services.ServiceOffre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ConsulterOffresParEtudiant {
    private User authenticatedUser;

    public void setUser(User user) {
        this.authenticatedUser = user;
    }
    private ObservableList<Offre> offresList;
    private ObservableList<Offre> filteredOffresList;
    private ServiceOffre serviceOffre = new ServiceOffre();

    @FXML
    private Pagination pagination;

    @FXML
    private VBox container; // Assurez-vous que votre FXML contient un VBox avec fx:id="container"

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    public void initialize() throws SQLException {
        offresList = FXCollections.observableArrayList();
        filteredOffresList = FXCollections.observableArrayList();
        loadOffresFromDatabase();

        // Initialiser la liste filtrée avec toutes les offres
        filteredOffresList.setAll(offresList);

        // Calculer le nombre de pages nécessaires pour afficher 3 offres par page
        updatePagination();
    }

    private void updatePagination() {
        int itemsPerPage = 3;
        int pageCount = (int) Math.ceil((double) filteredOffresList.size() / itemsPerPage);
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createPage);
    }

    private VBox createPage(int pageIndex) {
        VBox pageBox = new VBox(10);
        pageBox.setAlignment(Pos.CENTER);

        // Calculer l'index de début et de fin pour la page actuelle
        int itemsPerPage = 3;
        int startIndex = pageIndex * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, filteredOffresList.size());

        // Créer une HBox pour chaque ligne d'offres
        HBox hbox = new HBox(50); // Espacement horizontal entre les éléments
        hbox.setAlignment(Pos.CENTER); // Centrer le contenu
        hbox.setPadding(new Insets(10));

        // Parcourir les offres pour cette page
        for (int i = startIndex; i < endIndex; i++) {
            Offre offre = filteredOffresList.get(i);

            // Créer une VBox pour chaque offre
            VBox vbox = new VBox(10); // Espacement vertical entre les éléments
            vbox.setAlignment(Pos.CENTER); // Centrer le contenu

            // Créer une ImageView avec une image (à remplacer par votre propre chemin d'accès à l'image)
            ImageView imageView = new ImageView(new Image(String.valueOf(getClass().getResource("/offre_stage_logo.png"))));
            imageView.setFitWidth(100); // Largeur de l'image
            imageView.setFitHeight(100); // Hauteur de l'image

            // Créer un Label avec le nom de l'entreprise
            Label label = new Label(offre.getEntreprise());
            label.getStyleClass().add("label-box");

            // Créer un bouton "Voir plus"
            Button voirPlusButton = new Button("Voir");
            voirPlusButton.setOnAction(event -> handleVoirPlus(offre));

            // Ajouter les éléments à la VBox
            vbox.getChildren().addAll(imageView, label, voirPlusButton);

            // Ajouter la VBox à la HBox
            hbox.getChildren().add(vbox);
        }

        // Ajouter la HBox (ligne d'offres) à la pageBox
        pageBox.getChildren().add(hbox);

        return pageBox;
    }

    private void handleVoirPlus(Offre offre) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VoirPlusOffre.fxml"));
            Parent root = loader.load();
            VoirPlusController controller = loader.getController();
            controller.setEntreprise(offre.getEntreprise());
            controller.setTitreOffre(offre.getTitreOffre());
            controller.setDescriptionOffre(offre.getDescriptionOffre());
            controller.setNiveauEtude(offre.getNiveauEtude());
            controller.setDureeContrat(offre.getDureeContrat());
            controller.setDatePublication(offre.getDatePublication().toString());
            controller.setDateLimite(offre.getDateLimite().toString());
            controller.setEmail(offre.getEmail());

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
    public void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase().trim();
        List<Offre> filtered;

        if (searchTerm.isEmpty()) {
            filtered = offresList;
        } else {
            filtered = offresList.stream()
                    .filter(offre -> offre.getEntreprise().toLowerCase().contains(searchTerm))
                    .collect(Collectors.toList());
        }

        filteredOffresList.setAll(filtered);
        updatePagination();
    }
}
