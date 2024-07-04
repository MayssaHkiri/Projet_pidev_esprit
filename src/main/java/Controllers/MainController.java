package Controllers;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainController {

    @FXML
    private StackPane contentPane;

    @FXML
    private VBox sidebar; // Assurez-vous que votre VBox de sidebar est correctement injecté depuis le fichier FXML.

    @FXML
    public void loadGestionCoursPage() {
        loadPage("GererCours.fxml");
    }

    @FXML
    public void loadGestionFormationsPage() {
        // Masquer le sidebar spécifiquement pour la gestion des formations
        sidebar.setManaged(false);
        sidebar.setVisible(false);

        loadPage("GererFormation.fxml");

        // Restaurer l'état du sidebar après chargement
        sidebar.setManaged(true);
        sidebar.setVisible(true);
    }

    @FXML
    public void loadGestionOffresPage() {
        loadPage("GererOffres.fxml");
    }

    @FXML
    public void loadGestionUtilisateursPage() {
        loadPage("GererUtilisateurs.fxml");
    }

    private void loadPage(String fxmlFile) {
        try {
            Pane pane = FXMLLoader.load(getClass().getResource("/" + fxmlFile));
            contentPane.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
