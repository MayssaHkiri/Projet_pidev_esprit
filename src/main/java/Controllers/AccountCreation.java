package Controllers;

import Entities.User;
import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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

    private static final String NAME_PATTERN = "^[a-zA-Z]+$";
    private static final String ESPRIT_EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@esprit\\.tn$";

    @FXML
    void AddNewUser(ActionEvent event) {
        String chosenRole = myChoiceBox.getValue();
        String email = tfEmail.getText();
        String name = tfName.getText();
        String prenom = tfPrenom.getText();

        if (!isValidName(name)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Name", null, "Le nom doit contenir uniquement des lettres.");
            return;
        }

        if (!isValidName(prenom)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Prenom", null, "Le prénom doit contenir uniquement des lettres.");
            return;
        }

        if (!isValidEspritEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", null, "Veuillez entrer une adresse email valide (e.g., example@esprit.tn).");
            return;
        }

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
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur ajouté", "L'utilisateur a été ajouté avec succès.");
                // Vider les champs après ajout réussi
                clearFields();
            } else {
                System.out.println("Échec de l'ajout de l'utilisateur");
                showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'ajout", "Impossible d'ajouter l'utilisateur.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Exception SQL", e.getMessage());
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

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean isValidName(String name) {
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        return pattern.matcher(name).matches();
    }

    private boolean isValidEspritEmail(String email) {
        Pattern pattern = Pattern.compile(ESPRIT_EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }
}
