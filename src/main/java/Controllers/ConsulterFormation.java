package Controllers;

import Entities.Formation;
import Services.FormationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ConsulterFormation implements Initializable {

    @FXML
    private VBox formationsVBox;

    @FXML
    private TextField searchField;

    @FXML
    private MenuButton niveauxMenuButton;

    @FXML
    private MenuButton languagesMenuButton;

    private FormationService formationService;
    private ObservableList<Formation> formationsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        formationService = new FormationService();
        formationsList = FXCollections.observableArrayList();
        try {
            loadFormationsFromDatabase();
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
            showLoadFormationsErrorAlert();
        }
    }

    private void loadFormationsFromDatabase() throws SQLException, FileNotFoundException {
        List<Formation> formations = formationService.getAllFormations();
        formationsList.addAll(formations);

        for (Formation formation : formations) {
            StackPane formationPane = createFormationPane(formation);
            formationsVBox.getChildren().add(formationPane);
        }
    }

    private StackPane createFormationPane(Formation formation) {
        StackPane stackPane = new StackPane();
        stackPane.setOnMouseEntered(event -> handleMouseEnter(stackPane));
        stackPane.setOnMouseExited(event -> handleMouseExit(stackPane));
        stackPane.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-padding: 5;");

        try {
            // Charger l'image depuis le Blob
            Blob imageFormation = formation.getImageFormation();
            if (imageFormation != null && imageFormation.length() > 0) {
                InputStream is = imageFormation.getBinaryStream();
                Image image = new Image(is);

                // Afficher l'image dans ImageView
                ImageView imageView = new ImageView();
                imageView.setFitHeight(92.0);
                imageView.setFitWidth(80.0);
                imageView.setPreserveRatio(true);
                imageView.setImage(image);
                stackPane.getChildren().add(imageView);
            } else {
                // Gérer le cas où le Blob de l'image est vide ou null
                showImageLoadErrorAlert();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer l'erreur de chargement de l'image
            showImageLoadErrorAlert();
        }

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setSpacing(5);

        Label titleLabel = new Label("Titre : " + formation.getTitre());
        Label descriptionLabel = new Label("Description : " + formation.getDescription());

        vbox.getChildren().addAll(titleLabel, descriptionLabel);

        stackPane.getChildren().add(vbox);

        return stackPane;
    }


    private void handleMouseEnter(StackPane stackPane) {
        // Appliquer un effet de flou à l'image lors du survol
        stackPane.getChildren().get(0).setEffect(new javafx.scene.effect.GaussianBlur(10));

        // Afficher le bouton d'inscription
        Button inscrireButton = new Button("S'inscrire");
        inscrireButton.setOnAction(event -> handleInscription(stackPane));
        stackPane.getChildren().add(inscrireButton);
    }

    private void handleMouseExit(StackPane stackPane) {
        // Retirer l'effet de flou
        stackPane.getChildren().get(0).setEffect(null);

        // Cacher le bouton d'inscription
        stackPane.getChildren().removeIf(node -> node instanceof Button);
    }

    private void handleInscription(StackPane stackPane) {
        Formation formation = getFormationFromPane(stackPane);
        if (formation != null) {
            // Ouvrir une nouvelle fenêtre ou dialog pour gérer le processus d'inscription
            InscrireFormation.showStage(formation.getId());
        }
    }

    private Formation getFormationFromPane(StackPane stackPane) {
        for (Object node : stackPane.getChildren()) {
            if (node instanceof VBox) {
                VBox vbox = (VBox) node;
                for (Object label : vbox.getChildren()) {
                    if (label instanceof Label) {
                        String titre = ((Label) label).getText();
                        titre = titre.replace("Titre : ", "").trim();
                        try {
                            return formationService.getFormationByTitre(titre);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            // Gérer l'exception selon les besoins de votre application
                            showGetFormationErrorAlert();
                        }
                    }
                }
            }
        }
        return null;
    }

    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().trim();
        formationsVBox.getChildren().clear(); // Nettoyer les anciennes formations affichées

        try {
            List<Formation> formations = formationService.searchFormations(searchTerm);
            for (Formation formation : formations) {
                StackPane formationPane = createFormationPane(formation);
                formationsVBox.getChildren().add(formationPane);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showLoadFormationsErrorAlert();
        }
    }

    @FXML
    private void handleSQL() {
        // Exemple d'action pour SQL
        System.out.println("Affichage d'un mot qui contient SQL");
    }

    @FXML
    private void handleJava() {
        // Exemple d'action pour Java
        System.out.println("Affichage d'un mot qui contient Java");
    }

    @FXML
    private void handlePython() {
        // Exemple d'action pour Python
        System.out.println("Affichage d'un mot qui contient Python");
    }

    @FXML
    private void handleNiveau1() {
        // Action à exécuter pour Niveau 1
        System.out.println("Action pour Niveau 1");
    }

    @FXML
    private void handleNiveau2() {
        // Action à exécuter pour Niveau 2
        System.out.println("Action pour Niveau 2");
    }

    @FXML
    private void handleNiveau3() {
        // Action à exécuter pour Niveau 3
        System.out.println("Action pour Niveau 3");
    }

    @FXML
    private void handleNiveau4() {
        // Action à exécuter pour Niveau 4
        System.out.println("Action pour Niveau 4");
    }

    @FXML
    private void handleAccueil() {
        // Action à exécuter pour Accueil
        System.out.println("Action pour Accueil");
    }

    private void showLoadFormationsErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de Chargement des Formations");
        alert.setHeaderText(null);
        alert.setContentText("Une erreur est survenue lors du chargement des formations. Veuillez réessayer plus tard.");
        alert.showAndWait();
    }

    private void showImageLoadErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de Chargement d'Image");
        alert.setHeaderText(null);
        alert.setContentText("Une erreur est survenue lors du chargement de l'image de la formation. Veuillez réessayer.");
        alert.showAndWait();
    }

    private void showGetFormationErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de Récupération de Formation");
        alert.setHeaderText(null);
        alert.setContentText("Une erreur est survenue lors de la récupération de la formation. Veuillez réessayer.");
        alert.showAndWait();
    }
}