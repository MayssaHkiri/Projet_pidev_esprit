package Controllers;

import Entities.User;
import Services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UsersManagement {


        @FXML
        private TableColumn<User, String> emailColumn;

        @FXML
        private TableColumn<User, String> nomColumn;

        @FXML
        private TableColumn<User, String> prenomColumn;

        @FXML
        private TableColumn<User, String> roleColumn;

        @FXML
        private Button searchButton;

        @FXML
        private TextField searchField;

        @FXML
        private TableView<User> tableView;

        private UserService userService = new UserService() ;
        private ObservableList<User> usersList;

        @FXML
        public void initialize() throws SQLException {
                usersList = FXCollections.observableArrayList();
                loadUsersFromDatabase();

                emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
                nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
                prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
                roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

                tableView.setItems(usersList);
        }

        private void loadUsersFromDatabase() throws SQLException {
                List<User> users = userService.getAll();
                usersList.setAll(users);
        }

        @FXML
        void handleEdit(ActionEvent event) {
                User selectedUser = tableView.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                        boolean okClicked = showEditDialog(selectedUser);
                        if (okClicked) {
                                try {
                                        userService.update(selectedUser);
                                        tableView.refresh();
                                        showAlert("Succès", "Modification réussie", "L'utilisateur a été modifié avec succès.");
                                } catch (SQLException e) {
                                        showAlert("Erreur", "Impossible de modifier l'utilisateur", e.getMessage());
                                }
                        }
                } else {
                        showAlert("Aucune sélection", "Aucun utilisateur sélectionné.", "Veuillez sélectionner un utilisateur à modifier.");
                }
        }
        private boolean showEditDialog(User user) {
                try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/AccountEdit.fxml"));
                        Stage dialogStage = new Stage();
                        dialogStage.setTitle("Modifier Utilisateur");
                        dialogStage.initModality(Modality.WINDOW_MODAL);
                        Scene scene = new Scene(loader.load());
                        dialogStage.setScene(scene);

                        AccountEdit controller = loader.getController();
                        controller.setDialogStage(dialogStage);
                        controller.setUser(user);

                        dialogStage.showAndWait();

                        return controller.isOkClicked();
                } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                }
        }

        @FXML
        void handleDelete(ActionEvent event) {
                User selectedUser = tableView.getSelectionModel().getSelectedItem();
                if (selectedUser != null) {
                        try {
                                userService.delete(selectedUser.getId());
                                usersList.remove(selectedUser);
                                showAlert("Suppression réussie", "Utilisateur supprimé avec succès.", "");
                        } catch (SQLException e) {
                                showAlert("Erreur lors de la suppression", "Une erreur s'est produite lors de la suppression de l'utilisateur.", e.getMessage());
                        }
                } else {
                        showAlert("Aucune sélection", "Aucun utilisateur sélectionné.", "Veuillez sélectionner un utilisateur à supprimer.");
                }
        }
        private void showAlert(String title, String header, String content) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(header);
                alert.setContentText(content);
                alert.showAndWait();
        }

        @FXML
        private void handleAdd(ActionEvent event) throws IOException {
                // Récupérer la scène actuelle à partir de n'importe quel nœud de la scène
                Scene scene = tableView.getScene();

                // Récupérer la fenêtre principale (Stage) à partir de la scène
                Stage stage = (Stage) scene.getWindow();

                // Charger le fichier FXML pour la création de compte
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AccountCreation.fxml"));
                Parent root = loader.load();
                Scene newScene = new Scene(root);

                // Remplacer la scène actuelle par la nouvelle scène
                stage.setScene(newScene);
                stage.setTitle("Création de compte");
                stage.show();
        }
}


