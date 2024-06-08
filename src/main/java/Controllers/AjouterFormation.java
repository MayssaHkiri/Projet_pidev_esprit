package Controllers;

import Entities.Formation;
import Services.FormationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;



public class AjouterFormation {
    FormationService formationService = new FormationService() ;
    @FXML
    private ComboBox<String> btNiveau;

    @FXML
    private TextField tfDescription;

    @FXML
    private TextField tfIdEnseingant;

    @FXML
    private TextField tfTitre;

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
            if(success) {
                System.out.println("added ");

            }
            else {
                System.out.println("Not added");
            }
        }   catch (Exception e ) {
            System.out.println(e);
        }
    }


}
