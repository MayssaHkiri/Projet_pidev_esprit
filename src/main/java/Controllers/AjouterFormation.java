package Controllers;

import Entities.Formation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AjouterFormation {
    ObservableList<Formation> formationList = FXCollections.observableArrayList();

    @FXML
    private ComboBox<String> btNiveau;

    @FXML
    private TextField tfDescription;

    @FXML
    private TextField tfIdEnseignant;

    @FXML
    private TextField tfTitre;

    // Méthode pour ajouter une formation
    @FXML
    void handleAjouterFormation(ActionEvent event) {
        String titre = tfTitre.getText();
        String description = tfDescription.getText();
        int idEnseignant = Integer.parseInt(tfIdEnseignant.getText());
        String niveau = btNiveau.getValue(); // Récupérer le niveau sélectionné dans la ComboBox

        Formation formation = new Formation();
        formation.setTitre(titre);
        formation.setDescription(description);
        formation.setIdEnseignant(idEnseignant);
        // Assurez-vous que le niveau est correctement défini dans l'objet Formation si nécessaire

        // Simulation de l'ajout de la formation à une liste locale
        formationList.add(formation);
        System.out.println("Formation ajoutée avec succès (simulation)");
        clearFields(); // Vider les champs après ajout
    }

    // Méthode pour vider les champs
    private void clearFields() {
        tfTitre.clear();
        tfDescription.clear();
        tfIdEnseignant.clear();
        btNiveau.setValue(null); // Clear the ComboBox selection
    }
}
