package Controllers;

import Entities.Matiere;
import Services.ServiceMatiere;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MatiereViewController {

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
        int coeff = Integer.parseInt(coeffField.getText());
        String modeEval = modeEvalField.getText();
        Matiere newMatiere = new Matiere(0, nom, coeff, modeEval);

        try {
            serviceMatiere.ajouter(newMatiere);
            matiereList.add(newMatiere);
            nomField.clear();
            coeffField.clear();
            modeEvalField.clear();
        } catch (SQLException e) {
            e.printStackTrace();
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

                } catch (SQLException e) {
                    e.printStackTrace();
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
