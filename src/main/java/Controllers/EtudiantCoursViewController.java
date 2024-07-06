package Controllers;

import Entities.Chapitre;
import Entities.Cours;
import Entities.Matiere;
import Services.ServiceChapitre;
import Services.ServiceCours;
import Services.ServiceMatiere;
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
    private ServiceMatiere serviceMatiere = new ServiceMatiere();
    private ServiceChapitre serviceChapitre = new ServiceChapitre();

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
    private ChoiceBox<Matiere> matiereChoiceBox;
    @FXML
    private ChoiceBox<Chapitre> chapitreChoiceBox;

    private ObservableList<Matiere> matiereList = FXCollections.observableArrayList();
    private ObservableList<Chapitre> chapitreList = FXCollections.observableArrayList();

    @FXML
    public void initialize() throws SQLException {
        coursList = FXCollections.observableArrayList();
        allCoursList = new ArrayList<>(); // Initialiser la liste complète

        loadCoursFromDatabase();

        // Calculer le nombre de pages nécessaires pour afficher 5 cours par page
        int itemsPerPage = 5;
        int pageCount = (int) Math.ceil((double) coursList.size() / itemsPerPage);

        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createPage);

        matiereList.addAll(serviceMatiere.readAll());
        matiereChoiceBox.setItems(matiereList);

        matiereChoiceBox.setOnAction(event -> {
            Matiere selectedMatiere = matiereChoiceBox.getSelectionModel().getSelectedItem();
            if (selectedMatiere != null) {
                loadChapitresForMatiere(selectedMatiere.getId());
            }
        });
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

    private VBox createPage(int pageIndex) {
        VBox pageBox = new VBox(10);
        pageBox.setAlignment(Pos.CENTER);

        // Calculer l'index de début et de fin pour la page actuelle
        int itemsPerPage = 5;
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
            imageView.setFitWidth(50); // Largeur de l'image réduite
            imageView.setFitHeight(50); // Hauteur de l'image réduite

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
        Matiere selectedMatiere = matiereChoiceBox.getValue();
        Chapitre selectedChapitre = chapitreChoiceBox.getValue();

        if (selectedMatiere != null && selectedChapitre != null) {
            try {
                List<Cours> filteredCours = serviceCours.searchCourses(searchTerm, selectedMatiere.getId(), selectedChapitre.getId());
                coursList.setAll(filteredCours);

                // Mise à jour du label "Aucun résultat trouvé"
                noResultsLabel.setVisible(filteredCours.isEmpty());

                // Réinitialisation de la pagination
                int itemsPerPage = 5;
                int pageCount = (int) Math.ceil((double) coursList.size() / itemsPerPage);
                pagination.setPageCount(pageCount);
                pagination.setCurrentPageIndex(0);
                pagination.setPageFactory(this::createPage);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            coursList.setAll(allCoursList);
            noResultsLabel.setVisible(false);
        }
    }


    @FXML
    private void handleFilter(ActionEvent actionEvent) {
        Matiere selectedMatiere = matiereChoiceBox.getValue();
        Chapitre selectedChapitre = chapitreChoiceBox.getValue();

        if (selectedMatiere != null && selectedChapitre != null) {
            try {
                List<Cours> filteredCours = serviceCours.readByMatiereAndChapitre(selectedMatiere.getId(), selectedChapitre.getId());
                coursList.setAll(filteredCours);

                // Mise à jour du label "Aucun résultat trouvé"
                noResultsLabel.setVisible(filteredCours.isEmpty());

                // Réinitialisation de la pagination
                int itemsPerPage = 5;
                int pageCount = (int) Math.ceil((double) coursList.size() / itemsPerPage);
                pagination.setPageCount(pageCount);
                pagination.setCurrentPageIndex(0);
                pagination.setPageFactory(this::createPage);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            coursList.setAll(allCoursList);
            noResultsLabel.setVisible(false);
        }
    }
}
