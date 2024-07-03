package Controllers;

import Entities.Chapitre;
import Entities.Matiere;
import Services.ServiceChapitre;
import Services.ServiceMatiere;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AjoutChapitreView implements Initializable {

    @FXML
    private TextField titreField;
    @FXML
    private TextField descriptionField;

    private int matiereId;
    private ServiceChapitre serviceChapitre = new ServiceChapitre();

    @FXML
    private ChoiceBox<Matiere> matiereChoiceBox;

    private ObservableList<Matiere> matiereList;

    public void setMatiereId(int matiereId) {
        this.matiereId = matiereId;
    }

    public void setDialogStage(Stage dialogStage) {
        // Implementation
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ServiceMatiere serviceMatiere = new ServiceMatiere();
        matiereList = FXCollections.observableArrayList();

        try {
            matiereList.addAll(serviceMatiere.readAll());
            matiereChoiceBox.setItems(matiereList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAdd(ActionEvent actionEvent) {
        String titre = titreField.getText();
        String description = descriptionField.getText();
        Matiere selectedMatiere = matiereChoiceBox.getValue();

        if (titre != null && !titre.isEmpty() && description != null && !description.isEmpty() && selectedMatiere != null) {
            try {
                Chapitre chapitre = new Chapitre(titre, description, selectedMatiere.getId());
                serviceChapitre.ajouter(chapitre);
                titreField.clear();
                descriptionField.clear();

                showAlert(Alert.AlertType.INFORMATION, "Ajout réussi", null, "Le chapitre a été ajouté avec succès !");
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Exception SQL", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Ajout impossible", null, "Vous n'avez pas rempli les champs obligatoires !");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) ((javafx.scene.Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
