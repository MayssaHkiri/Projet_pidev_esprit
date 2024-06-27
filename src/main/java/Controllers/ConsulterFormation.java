package Controllers;

import Entities.Formation;
import Utils.DataSource;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class ConsulterFormation {

    @FXML
    private Button accueilButton;

    @FXML
    private MenuButton niveauxMenuButton;

    @FXML
    private MenuButton languagesMenuButton;

    @FXML
    private TextField searchField;

    @FXML
    private ImageView imageFormation1;

    @FXML
    private Label labelFormation1;

    @FXML
    private ImageView imageFormation2;

    @FXML
    private Label labelFormation2;

    @FXML
    private ImageView imageFormation3;

    @FXML
    private Label labelFormation3;

    @FXML
    private ImageView imageFormation4;

    @FXML
    private Label labelFormation4;

    @FXML
    private ImageView imageFormation5;

    @FXML
    private Label labelFormation5;

    @FXML
    private ImageView imageFormation6;

    @FXML
    private Label labelFormation6;

    private DataSource dataSource;

    @FXML
    public void initialize() {
        dataSource = DataSource.getInstance();
        loadFormations();
    }

    private void loadFormations() {
        List<Formation> formations = dataSource.getAllFormations();

        if (formations.size() >= 1) {
            updateFormationView(imageFormation1, labelFormation1, formations.get(0));
        }
        if (formations.size() >= 2) {
            updateFormationView(imageFormation2, labelFormation2, formations.get(1));
        }
        if (formations.size() >= 3) {
            updateFormationView(imageFormation3, labelFormation3, formations.get(2));
        }
        if (formations.size() >= 4) {
            updateFormationView(imageFormation4, labelFormation4, formations.get(3));
        }
        if (formations.size() >= 5) {
            updateFormationView(imageFormation5, labelFormation5, formations.get(4));
        }
        if (formations.size() >= 6) {
            updateFormationView(imageFormation6, labelFormation6, formations.get(5));
        }
    }

    private void updateFormationView(ImageView imageView, Label label, Formation formation) {
        if (formation != null) {
            imageView.setImage(new Image(formation.getImageUrl()));
            label.setText("Titre : " + formation.getTitre() + "\nDescription : " + formation.getDescription() + "\nID Enseignant : " + formation.getIdEnseignant());
        } else {
            imageView.setImage(null);
            label.setText("");
        }
    }

    @FXML
    public void handleAccueil() {
        System.out.println("Bouton Accueil cliqué !");
        // Ajoutez votre logique spécifique pour le bouton Accueil
    }

    @FXML
    public void handleNiveau1() {
        System.out.println("Niveau 1 sélectionné !");
        // Ajoutez votre logique spécifique pour le niveau 1
    }

    @FXML
    public void handleNiveau2() {
        System.out.println("Niveau 2 sélectionné !");
        // Ajoutez votre logique spécifique pour le niveau 2
    }

    @FXML
    public void handleNiveau3() {
        System.out.println("Niveau 3 sélectionné !");
        // Ajoutez votre logique spécifique pour le niveau 3
    }

    @FXML
    public void handleNiveau4() {
        System.out.println("Niveau 4 sélectionné !");
        // Ajoutez votre logique spécifique pour le niveau 4
    }

    @FXML
    public void handlePython() {
        System.out.println("Langue Python sélectionnée !");
        // Ajoutez votre logique spécifique pour Python
    }

    @FXML
    public void handleJava() {
        System.out.println("Langue Java sélectionnée !");
        // Ajoutez votre logique spécifique pour Java
    }

    @FXML
    public void handleSQL() {
        System.out.println("Langue SQL sélectionnée !");
        // Ajoutez votre logique spécifique pour SQL
    }

    @FXML
    public void handleSearch() {
        System.out.println("Bouton Rechercher cliqué !");
        // Ajoutez votre logique spécifique pour le bouton Rechercher
    }

    @FXML
    private void handleMouseEnter(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView imageView = (ImageView) event.getSource();
            Label label = getLabelForImageView(imageView);
            if (label != null) {
                label.setVisible(true);
            }
        }
    }

    @FXML
    private void handleMouseExit(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView imageView = (ImageView) event.getSource();
            Label label = getLabelForImageView(imageView);
            if (label != null) {
                label.setVisible(false);
            }
        }
    }

    private Label getLabelForImageView(ImageView imageView) {
        if (imageView == imageFormation1) return labelFormation1;
        if (imageView == imageFormation2) return labelFormation2;
        if (imageView == imageFormation3) return labelFormation3;
        if (imageView == imageFormation4) return labelFormation4;
        if (imageView == imageFormation5) return labelFormation5;
        if (imageView == imageFormation6) return labelFormation6;
        return null;
    }
}
