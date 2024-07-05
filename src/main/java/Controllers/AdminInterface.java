package Controllers;

import Entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminInterface {
    private User authenticatedUser;

    public void setUser(User user) {
        this.authenticatedUser = user;
        // Utilisez les informations de l'utilisateur authentifié ici
    }

    @FXML
    public void initialize() {

    }

    @FXML
    void GestionFormation(ActionEvent event) {

    }

    @FXML
    void GestionOffres(ActionEvent event) {

    }

    @FXML
    void GestionUsers(ActionEvent event) {
        try {
            // Charger l'interface de profil
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/usersManagement.fxml"));
            Parent root = loader.load();

            // Passer l'utilisateur authentifié au contrôleur de l'interface de profil
            UsersManagement usersManagement = loader.getController();
            usersManagement.setUser(authenticatedUser);

            // Afficher la nouvelle scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur d'entrée/sortie
        }
    }

    @FXML
    void Profil(ActionEvent event) {
        try {
            // Charger l'interface de profil
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserProfile.fxml"));
            Parent root = loader.load();

            // Passer l'utilisateur authentifié au contrôleur de l'interface de profil
            UserProfileController userProfileController = loader.getController();
            userProfileController.setUser(authenticatedUser);

            // Afficher la nouvelle scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur d'entrée/sortie
        }
    }

}
