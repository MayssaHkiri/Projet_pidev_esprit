package Controllers;

import Entities.Offre;
import Services.ServiceOffre;
import Utils.DataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;


import java.beans.PropertyEditorSupport;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AjouterOffre {

    @FXML
    private TextField dateLimiteOffre;

    @FXML
    private TextField datePublication;

    @FXML
    private TextField descriptionOffre;

    @FXML
    private TextField dureeContrat;
/*
    @FXML
    private ChoiceBox<?> niveauEtude;
    */


    @FXML
    private TextField nomEntreprise;

    @FXML
    private TextField titreOffre;
    @FXML
    private ChoiceBox<String> niveauEtude;

    @FXML
    public void initialize() {
        niveauEtude.getItems().addAll("Première année", "Deuxième année");
        niveauEtude.getSelectionModel().selectFirst();
    }

    ServiceOffre serviceOffre = new ServiceOffre();


    @FXML
    void ajouterDB(ActionEvent event) throws SQLException {

        serviceOffre.ajouter(new Offre(1, titreOffre.getText(), descriptionOffre.getText(), niveauEtude.getValue(), dureeContrat.getText(), datePublication.getText(), nomEntreprise.getText(), dateLimiteOffre.getText()));
        afficherPopup("Succès", "Ajout réussi", "L'offre a été ajoutée avec succès.");

        //System.out.println("seccess from ajouterDB");
    }
    private void afficherPopup(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public TextField getTitreOffreTextField() {
        return titreOffre;
    }

}
