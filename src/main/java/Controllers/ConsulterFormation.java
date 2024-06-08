package Controllers;

import Entities.Formation;
import Services.FormationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

import java.sql.SQLException;

public class ConsulterFormation implements Initializable {
    @FXML
    private Label LbDescr;

    @FXML
    private Label LbIdEns;

    @FXML
    private Label LbTitre;

    @FXML
    private ComboBox<String> btNiveau;

    @FXML
    private Label lbNuméro;

    @FXML
    void Inscription(ActionEvent event) {

    }
    private FormationService formationService ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        formationService = new FormationService();
        loadFormationData(2);  // Charger les données de la formation avec ID 2
    }

    private void loadFormationData(int idFormation) {
        try {
            Formation formation = formationService.getFormationById(idFormation);
            if (formation != null) {
                LbTitre.setText(formation.getTitre());
                LbDescr.setText(formation.getDescription());
                LbIdEns.setText(String.valueOf(formation.getIdEnseignant()));
                lbNuméro.setText(String.valueOf(formation.getId()));

            } else {
                System.out.println("Formation not found for ID: " + idFormation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}