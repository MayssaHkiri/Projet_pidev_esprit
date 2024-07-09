package Controllers;

import Entities.Matiere;
import Entities.User;
import Services.ServiceMatiere;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MatiereViewController {
    private User authenticatedUser;

    public void setUser(User user) {
        this.authenticatedUser = user;
    }

    @FXML
    private TableView<Matiere> tableView;
    @FXML
    private TableColumn<Matiere, String> nomColumn;
    @FXML
    private TableColumn<Matiere, Integer> coeffColumn;
    @FXML
    private TableColumn<Matiere, String> modeEvalColumn;
    @FXML
    private TextField nomField;
    @FXML
    private TextField coeffField;
    @FXML
    private TextField modeEvalField;

    private ServiceMatiere serviceMatiere;
    private ObservableList<Matiere> matiereList;

    @FXML
    public void initialize() {
        serviceMatiere = new ServiceMatiere();
        matiereList = FXCollections.observableArrayList();

        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        coeffColumn.setCellValueFactory(new PropertyValueFactory<>("coeff"));
        modeEvalColumn.setCellValueFactory(new PropertyValueFactory<>("modeEval"));

        tableView.setItems(matiereList);

        try {
            matiereList.addAll(serviceMatiere.readAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdd() {
        String nom = nomField.getText();
        String coeffText = coeffField.getText();
        String modeEval = modeEvalField.getText();

        if (nom == null || nom.isEmpty() || coeffText == null || coeffText.isEmpty() || modeEval == null || modeEval.isEmpty()) {
            showErrorDialog("Champs obligatoires", "Veuillez remplir les champs obligatoires", "Tous les champs doivent être remplis.");
            return;
        }

        String errorMessage = "";

        try {
            int coeff = Integer.parseInt(coeffText);
            if (coeff <= 0) {
                errorMessage += "Le coefficient doit être un entier positif!\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Le coefficient doit être un nombre entier!\n";
        }

        if (errorMessage.isEmpty()) {
            int coeff = Integer.parseInt(coeffText);
            Matiere newMatiere = new Matiere(nom, coeff, modeEval);

            try {
                serviceMatiere.ajouter(newMatiere);
                matiereList.add(newMatiere);
                nomField.clear();
                coeffField.clear();
                modeEvalField.clear();
                showSuccessDialog("Succès", "Ajout réussi", "La matière a été ajoutée avec succès.");
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorDialog("Erreur", "Impossible d'ajouter la matière", e.getMessage());
            }
        } else {
            showErrorDialog("Champs invalides", "Veuillez corriger les champs invalides", errorMessage);
        }
    }

    @FXML
    private void handleUpdate() {
        Matiere selectedMatiere = tableView.getSelectionModel().getSelectedItem();
        if (selectedMatiere != null) {
            boolean okClicked = showEditDialog(selectedMatiere);
            if (okClicked) {
                try {
                    serviceMatiere.update(selectedMatiere);
                    tableView.refresh();
                    showSuccessDialog("Succès", "Modification réussie", "La matière a été modifiée avec succès.");
                } catch (SQLException e) {
                    e.printStackTrace();
                    showErrorDialog("Erreur", "Impossible de modifier la matière", e.getMessage());
                }
            }
        }
    }

    @FXML
    private void handleDelete() {
        Matiere selectedMatiere = tableView.getSelectionModel().getSelectedItem();
        if (selectedMatiere != null) {
            try {
                serviceMatiere.supprimer(selectedMatiere);
                matiereList.remove(selectedMatiere);
                showSuccessDialog("Succès", "Suppression réussie", "La matière a été supprimée avec succès.");
            } catch (SQLException e) {
                e.printStackTrace();
                showErrorDialog("Erreur", "Impossible de supprimer la matière", "Cette matière est référencée par un ou plusieurs chapitres.");
            }
        } else {
            showErrorDialog("Aucune sélection", "Aucune matière sélectionnée", "Veuillez sélectionner une matière dans la table.");
        }
    }

    private void showSuccessDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean showEditDialog(Matiere matiere) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/EditMatiereView.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modifier Matiere");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);

            EditMatiereController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMatiere(matiere);

            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

