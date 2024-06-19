package Controllers;

import Entities.Formation;
import Services.FormationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ConsulterFormation implements Initializable {

    @FXML
    private ImageView imageFormation1;
    @FXML
    private ImageView imageFormation2;
    @FXML
    private ImageView imageFormation3;
    @FXML
    private ImageView imageFormation4;
    @FXML
    private ImageView imageFormation5;
    @FXML
    private ImageView imageFormation6;

    @FXML
    private Label inscriptionLabel;

    @FXML
    private Label labelFormation1;
    @FXML
    private Label labelFormation2;
    @FXML
    private Label labelFormation3;
    @FXML
    private Label labelFormation4;
    @FXML
    private Label labelFormation5;
    @FXML
    private Label labelFormation6;


    private FormationService formationService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        formationService = new FormationService();
        loadFormations();
    }

    private void loadFormations() {
        try {
            List<Formation> formations = formationService.getAllFormations();
            if (formations.size() >= 6) {
                setupFormation(imageFormation1, formations.get(0));
                setupFormation(imageFormation2, formations.get(1));
                setupFormation(imageFormation3, formations.get(2));
                setupFormation(imageFormation4, formations.get(3));
                setupFormation(imageFormation5, formations.get(4));
                setupFormation(imageFormation6, formations.get(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupFormation(ImageView imageView, Formation formation) {
        // Charger l'image
        imageView.setImage(new Image("file:C:\\Users\\LENOVO\\Desktop\\PROJET PI\\Projet_pidev_esprit\\src\\main\\resources" + formation.getId() + ".png")); // Adapter le chemin selon votre structure
        // Définir un effet ombre portée pour le survol
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        imageView.setOnMouseEntered(event -> {
            imageView.setEffect(dropShadow);
            inscriptionLabel.setVisible(true);
        });
        imageView.setOnMouseExited(event -> {
            imageView.setEffect(null);
            inscriptionLabel.setVisible(false);
        });

    labelFormation1.setText(formation.getTitre());
    }
    @FXML
    public void handleInscription(ActionEvent event) {
        // Implémentez l'action à effectuer lorsque l'utilisateur clique sur "Inscrivez-vous"
        try {
            // Récupérer l'ID de la formation à partir de l'image cliquée
            ImageView imageView = (ImageView) event.getSource();
            int id = getIdFromImageView(imageView);

            // Vérifier si l'ID est valide (par exemple, > 0)
            if (id > 0) {
                // Instancier le contrôleur InscrireFormation et passer l'ID
                InscrireFormation inscrireFormation = new InscrireFormation();
                inscrireFormation.setId(id);

                // Afficher la fenêtre d'inscription
                inscrireFormation.showStage();
            } else {
                // Gérer le cas où l'ID de la formation n'est pas valide
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText("ID de formation invalide.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            // Gérer toute exception survenue lors de l'ouverture de la fenêtre d'inscription
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur est survenue lors de l'ouverture de la fenêtre d'inscription.");
            alert.showAndWait();
        }
    }
    // Méthode pour obtenir l'ID de la formation à partir de l'ImageView
    private int getIdFromImageView(ImageView imageView) {
        // Implémentez la logique pour récupérer l'ID correspondant à l'imageView cliquée
        // Par exemple, vous pouvez utiliser des attributs ou des tags pour stocker l'ID des formations
        return 1; // Exemple : retourne l'ID de la formation 1
    }



}
