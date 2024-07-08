package Controllers;

import Entities.User;
import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AccountCreation implements Initializable {

    @FXML
    private ChoiceBox<String> myChoiceBox;
    private String[] roles = {"enseignant", "etudiant"};

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfPrenom;
    UserService userService = new UserService();

    @FXML
    void AddNewUser(ActionEvent event) {
        String chosenRole = myChoiceBox.getValue();
        String email = tfEmail.getText();
        String name = tfName.getText();
        String prenom = tfPrenom.getText();

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setNom(name);
        newUser.setPrenom(prenom);
        newUser.setRole(chosenRole);

        try {
            boolean isAdded = userService.add(newUser);
            if (isAdded) {
                System.out.println("Utilisateur ajouté avec succès");
                // Afficher une alerte de succès
                showAlert(AlertType.INFORMATION, "Succès", "Utilisateur ajouté", "L'utilisateur a été ajouté avec succès.");
                // Vider les champs après ajout réussi
                clearFields();
            } else {
                System.out.println("Échec de l'ajout de l'utilisateur");
                showAlert(AlertType.ERROR, "Erreur", "Échec de l'ajout", "Impossible d'ajouter l'utilisateur.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Erreur", "Exception SQL", e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myChoiceBox.getItems().addAll(roles);
        myChoiceBox.setOnAction(this::getRole);
    }

    public void getRole(ActionEvent event) {
        String chosenRole = myChoiceBox.getValue();
    }

    private void clearFields() {
        tfEmail.clear();
        tfName.clear();
        tfPrenom.clear();
        myChoiceBox.setValue(null); // Réinitialiser la ChoiceBox
    }

    private void showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
