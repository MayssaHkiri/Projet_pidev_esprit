package Controllers;

import Entities.Formation;
import Services.FormationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class ConsulterFormation {

    @FXML
    private Label LbDescr;

    @FXML
    private Label LbIdEns;

    @FXML
    private Label LbTitre;

    @FXML
    private ComboBox<?> btNiveau;

    @FXML
    private Label lbNuméro;

    @FXML
    private TextField idFormationField;

    @FXML
    private TableView<Formation> tableView;
    @FXML
    private TableColumn<Formation, String> titreColumn;
    @FXML
    private TableColumn<Formation, String> descriptionColumn;

    private FormationService formationService = new FormationService();
    private ObservableList<Formation> formationList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        titreColumn.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        tableView.setItems(formationList);

        try {
            formationList.addAll(formationService.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erreur SQL", "Une erreur est survenue lors de la récupération des formations.");
        }
    }

    @FXML
    void Inscription(ActionEvent event) {
        try {
            int idFormation = Integer.parseInt(idFormationField.getText());
            Formation formation = formationService.getFormationById(idFormation);

            if (formation != null) {
                LbTitre.setText(formation.getTitre());
                LbDescr.setText(formation.getDescription());
                LbIdEns.setText(String.valueOf(formation.getIdEnseignant()));
                lbNuméro.setText(String.valueOf(formation.getId()));
            } else {
                showError("Formation non trouvée", "Aucune formation trouvée avec l'ID fourni.");
            }
        } catch (NumberFormatException e) {
            showError("ID invalide", "Veuillez entrer un ID de formation valide.");
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erreur SQL", "Une erreur est survenue lors de la récupération des détails de la formation.");
        }
    }

    @FXML
    private void handleAdd() {
        try {
            String titre = LbTitre.getText();
            String description = LbDescr.getText();
            int enseignantId = Integer.parseInt(LbIdEns.getText());

            Formation formation = new Formation(titre, description, enseignantId);

            formationService.add(formation);
            formationList.add(formation);
            clearFields();
        } catch (NumberFormatException e) {
            showError("Erreur de saisie", "Veuillez entrer des valeurs valides.");
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Erreur SQL", "Une erreur est survenue lors de l'ajout de la formation.");
        }
    }

    @FXML
    private void handleUpdate() {
        Formation selectedFormation = tableView.getSelectionModel().getSelectedItem();
        if (selectedFormation != null) {
            try {
                selectedFormation.setTitre(LbTitre.getText());
                selectedFormation.setDescription(LbDescr.getText());
                selectedFormation.setIdEnseignant(Integer.parseInt(LbIdEns.getText()));

                formationService.update(selectedFormation);
                tableView.refresh();
                clearFields();
            } catch (NumberFormatException e) {
                showError("Erreur de saisie", "Veuillez entrer des valeurs valides.");
            } catch (SQLException e) {
                e.printStackTrace();
                showError("Erreur SQL", "Une erreur est survenue lors de la mise à jour de la formation.");
            }
        } else {
            showError("Sélection requise", "Veuillez sélectionner une formation à mettre à jour.");
        }
    }

    @FXML
    private void handleDelete() {
        Formation selectedFormation = tableView.getSelectionModel().getSelectedItem();
        if (selectedFormation != null) {
            try {
                formationService.delete(selectedFormation.getId());
                formationList.remove(selectedFormation);
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
                showError("Erreur SQL", "Une erreur est survenue lors de la suppression de la formation.");
            }
        } else {
            showError("Sélection requise", "Veuillez sélectionner une formation à supprimer.");
        }
    }

    private void clearFields() {
        LbTitre.setText("");
        LbDescr.setText("");
        LbIdEns.setText("");
        lbNuméro.setText("");
        idFormationField.setText("");
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
