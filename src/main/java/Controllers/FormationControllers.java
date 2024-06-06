package Controllers;
import Entities.Formation;
import Services.FormationService;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.List;

public class FormationControllers {

    @FXML
    private TableView<Formation> formationTable;

    @FXML
    private TableColumn<Formation, Integer> idColumn;

    @FXML
    private TableColumn<Formation, String> titreColumn;

    @FXML
    private TableColumn<Formation, String> descriptionColumn;

    @FXML
    private TextField titreField;

    @FXML
    private TextField descriptionField;

    private FormationService formationService;

    public void FormationController() {
        this.formationService = new FormationService();
    }

    @FXML
    private void initialize() {
        // Initialisation des colonnes de la table (identique à ce qui a été fait précédemment)

        // Charger les formations et les afficher dans la TableView
        loadFormationData();
    }

    private void loadFormationData() {
        List<Formation> formations = formationService.getAllFormations();
        ObservableList<Formation> formationsObservable = FXCollections.observableArrayList(formations);
        formationTable.setItems(formationsObservable);
    }

    @FXML
    private void handleAjouterFormation() {
        String titre = titreField.getText();
        String description = descriptionField.getText();

        // Vérifier si les champs sont vides
        if (titre.isEmpty() || description.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs vides");
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }

        // Ajouter la formation
        Formation newFormation = formationService.addFormation(titre, description);
        if (newFormation != null) {
            // Rafraîchir les données de la TableView
            loadFormationData();
            // Effacer les champs de saisie
            titreField.clear();
            descriptionField.clear();
        } else {
            // Afficher un message d'erreur si l'ajout a échoué
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Ajout impossible");
            alert.setContentText("Une erreur s'est produite lors de l'ajout de la formation.");
            alert.showAndWait();
        }
    }
}
