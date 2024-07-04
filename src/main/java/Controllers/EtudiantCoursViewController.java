package Controllers;

import Entities.Cours;
import Services.ServiceCours;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EtudiantCoursViewController {
    private ObservableList<Cours> coursList;
    private List<Cours> allCoursList; // Liste complète des cours
    private ServiceCours serviceCours = new ServiceCours();

    @FXML
    private Pagination pagination;
    @FXML
    private Label noResultsLabel;
    @FXML
    private VBox container; // Assurez-vous que votre FXML contient un VBox avec fx:id="container"

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    public void initialize() throws SQLException {
        coursList = FXCollections.observableArrayList();
        allCoursList = new ArrayList<>(); // Initialiser la liste complète

        loadCoursFromDatabase();

        // Calculer le nombre de pages nécessaires pour afficher 3 cours par page
        int itemsPerPage = 3;
        int pageCount = (int) Math.ceil((double) coursList.size() / itemsPerPage);

        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createPage);
    }

    private VBox createPage(int pageIndex) {
        VBox pageBox = new VBox(10);
        pageBox.setAlignment(Pos.CENTER);

        // Calculer l'index de début et de fin pour la page actuelle
        int itemsPerPage = 3;
        int startIndex = pageIndex * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, coursList.size());

        // Créer une HBox pour chaque ligne de cours
        HBox hbox = new HBox(50); // Espacement horizontal entre les éléments
        hbox.setAlignment(Pos.CENTER); // Centrer le contenu
        hbox.setPadding(new Insets(10));

        // Parcourir les cours pour cette page
        for (int i = startIndex; i < endIndex; i++) {
            Cours cours = coursList.get(i);

            // Créer une VBox pour chaque cours
            VBox vbox = new VBox(10); // Espacement vertical entre les éléments
            vbox.setAlignment(Pos.CENTER); // Centrer le contenu

            // Créer une ImageView avec une image (à remplacer par votre propre chemin d'accès à l'image)
            ImageView imageView = new ImageView(new Image(String.valueOf(getClass().getResource("/logoPdf.jpg"))));
            imageView.setFitWidth(100); // Largeur de l'image
            imageView.setFitHeight(100); // Hauteur de l'image

            // Créer un Label avec le titre du cours
            Label label = new Label(cours.getTitre());
            label.getStyleClass().add("label-box");

            // Créer un bouton "Télécharger"
            Button telechargerButton = new Button("Télécharger");
            telechargerButton.setOnAction(event -> handleDownloadPDF(cours));

            // Ajouter les éléments à la VBox
            vbox.getChildren().addAll(imageView, label, telechargerButton);

            // Ajouter la VBox à la HBox
            hbox.getChildren().add(vbox);
        }

        // Ajouter la HBox (ligne de cours) à la pageBox
        pageBox.getChildren().add(hbox);

        return pageBox;
    }

    @FXML
    private void handleDownloadPDF(Cours cours) {
        if (cours != null && cours.getPdfFile() != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    Blob pdfBlob = cours.getPdfFile();
                    byte[] buffer = pdfBlob.getBytes(1, (int) pdfBlob.length());

                    fos.write(buffer);

                    // Afficher une alerte de succès
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Téléchargement réussi");
                    alert.setHeaderText(null);
                    alert.setContentText("Le fichier PDF a été téléchargé avec succès !");
                    alert.showAndWait();
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // Afficher une alerte d'erreur si aucun cours n'est sélectionné ou si aucun PDF n'est disponible
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de téléchargement");
            alert.setHeaderText(null);
            alert.setContentText("Aucun fichier PDF à télécharger pour le cours sélectionné.");
            alert.showAndWait();
        }
    }

    private void loadCoursFromDatabase() throws SQLException {
        List<Cours> cours = serviceCours.readAll();
        coursList.setAll(cours);
        allCoursList.addAll(cours); // Remplir la liste complète
    }

    @FXML
    private void handleSearch(ActionEvent actionEvent) {
        String searchTerm = searchField.getText().toLowerCase().trim();

        List<Cours> filtered;
        if (searchTerm.isEmpty()) {
            // Afficher tous les éléments si le champ de recherche est vide
            filtered = new ArrayList<>(allCoursList);
        } else {
            // Filtrer les éléments selon le terme de recherche
            filtered = allCoursList.stream()
                    .filter(cours -> cours.getTitre().toLowerCase().contains(searchTerm))
                    .collect(Collectors.toList());
        }

        if (filtered.isEmpty()) {
            noResultsLabel.setVisible(true);
        } else {
            noResultsLabel.setVisible(false);
        }

        // Remettre tous les cours en cas de recherche vide
        if (searchTerm.isEmpty()) {
            coursList.setAll(allCoursList);
        } else {
            coursList.setAll(filtered);
        }

        int itemsPerPage = 3;
        int pageCount = (int) Math.ceil((double) coursList.size() / itemsPerPage);
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0); // Retour à la première page après la recherche
        pagination.setPageFactory(this::createPage);
    }
}

