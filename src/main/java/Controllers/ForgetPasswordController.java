package Controllers;

import java.sql.SQLException;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import Services.UserService;

public class ForgetPasswordController {

    @FXML
    private TextField emailField;

    private UserService userService = new UserService();

    private static final String ESPRIT_EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@esprit\\.tn$";

    @FXML
    public void handleResetPassword(ActionEvent event) {
        String email = emailField.getText();

        if (!isValidEspritEmail(email)) {
            showErrorAlert("Invalid Email", "Please enter a valid Esprit email address (e.g., example@esprit.tn).");
            return;
        }

        try {
            boolean isReset = userService.resetPasswordByEmail(email);
            if (isReset) {
                showSuccessAlert("Password Reset", "A new password has been sent to your email.");
            } else {
                showErrorAlert("Email Not Found", "The email address you entered does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Database Error", "An error occurred while accessing the database.");
        }
    }

    private boolean isValidEspritEmail(String email) {
        Pattern pattern = Pattern.compile(ESPRIT_EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        emailField.getScene().getWindow().hide();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Customizing the alert to have a green color
        alert.getDialogPane().setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724;");

        alert.showAndWait();
    }
}
