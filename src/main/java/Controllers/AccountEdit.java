package Controllers;

import Entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.regex.Pattern;

public class AccountEdit {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;

    private Stage dialogStage;
    private User user;
    private boolean okClicked = false;

    private static final String NAME_PATTERN = "^[a-zA-Z]+$";
    private static final String ESPRIT_EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@esprit\\.tn$";

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setUser(User user) {
        this.user = user;

        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        emailField.setText(user.getEmail());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleSave() {
        String name = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();

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

        user.setNom(name);
        user.setPrenom(prenom);
        user.setEmail(email);

        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isValidName(String name) {
        Pattern pattern = Pattern.compile(NAME_PATTERN);
        return pattern.matcher(name).matches();
    }

    private boolean isValidEspritEmail(String email) {
        Pattern pattern = Pattern.compile(ESPRIT_EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
