package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.InputStream;
import java.net.URL;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadFormations();
    }

    private void loadFormations() {
        for (int i = 1; i <= 6; i++) {
            ImageView imageView = getImageViewByIndex(i);
            Label label = getLabelByIndex(i);

            // Charger l'image depuis les ressources
            String imageName = "/image" + i + ".png"; // Assurez-vous que le nom correspond exactement au fichier
            InputStream stream = getClass().getResourceAsStream(imageName);
            if (stream != null) {
                Image image = new Image(stream);
                if (imageView != null) {
                    imageView.setImage(image);
                } else {
                    System.err.println("ImageView " + i + " is null.");
                }
            } else {
                System.err.println("Impossible de charger l'image : " + imageName);
            }

            // Définir un effet de survol
            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(5.0);
            if (imageView != null) {
                imageView.setOnMouseEntered(event -> {
                    imageView.setEffect(dropShadow);
                });
                imageView.setOnMouseExited(event -> {
                    imageView.setEffect(null);
                });
            }

            // Définir le texte du label (Formation 1, Formation 2, ...)
            label.setText("Formation " + i);
        }
    }


    private ImageView getImageViewByIndex(int index) {
        switch (index) {
            case 1: return imageFormation1;
            case 2: return imageFormation2;
            case 3: return imageFormation3;
            case 4: return imageFormation4;
            case 5: return imageFormation5;
            case 6: return imageFormation6;
            default: throw new IllegalArgumentException("Index must be between 1 and 6");
        }
    }

    private Label getLabelByIndex(int index) {
        switch (index) {
            case 1: return labelFormation1;
            case 2: return labelFormation2;
            case 3: return labelFormation3;
            case 4: return labelFormation4;
            case 5: return labelFormation5;
            case 6: return labelFormation6;
            default: throw new IllegalArgumentException("Index must be between 1 and 6");
        }
    }

    @FXML
    public void handleMouseEnter(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        imageView.setEffect(new DropShadow());
    }

    @FXML
    public void handleMouseExit(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        imageView.setEffect(null);
    }

    @FXML
    public void handleInscription(ActionEvent event) {
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

    private int getIdFromImageView(ImageView imageView) {
        // Implémentez la logique pour récupérer l'ID correspondant à l'imageView cliquée
        return 1; // Exemple : retourne l'ID de la formation 1
    }
}
