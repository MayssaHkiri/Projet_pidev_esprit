package Controllers;

import Entities.Formation;
import Services.FormationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.SQLException;

public class AjouterFormation {
    FormationService formationService = new FormationService();
    ObservableList<Formation> formationList = FXCollections.observableArrayList();

    @FXML
    private ComboBox<String> btNiveau;

    @FXML
    private TextField tfDescription;

    @FXML
    private TextField tfIdEnseingant;

    @FXML
    private TextField tfTitre;

    // Méthode pour ajouter une formation
    @FXML
    void AjouterFormation(ActionEvent event) {
        try {
            String titre = tfTitre.getText();
            String description = tfDescription.getText();
            int idEnseignant = Integer.parseInt(tfIdEnseingant.getText());

            Formation formation = new Formation();
            formation.setTitre(titre);
            formation.setDescription(description);
            formation.setIdEnseignant(idEnseignant);

            boolean success = formationService.add(formation);
            if (success) {
                System.out.println("Formation ajoutée avec succès");
                clearFields(); // Vider les champs après ajout
            } else {
                System.out.println("Échec de l'ajout de la formation");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erreur de format de numéro: " + e);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur SQL: " + e);
        }
    }

    // Méthode pour vider les champs
    private void clearFields() {
        tfTitre.clear();
        tfDescription.clear();
        tfIdEnseingant.clear();
    }
}