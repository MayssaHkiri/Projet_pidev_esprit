package Controllers;

import Entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        user.setNom(nomField.getText());
        user.setPrenom(prenomField.getText());
        user.setEmail(emailField.getText());

        okClicked = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }
}
