package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainAdminController {
    @FXML
    private StackPane contentPane;

    @FXML
    private void loadGestionFormations() {
        loadPage("GererFormation.fxml");
    }

    @FXML
    private void loadGestionUsers() {
        loadPage("usersManagement.fxml");
    }

    @FXML
    private void loadProfil() {
        loadPage("UserProfile.fxml");
    }

    private void loadPage(String fxmlFile) {
        try {
            Pane pane = FXMLLoader.load(getClass().getResource("/" + fxmlFile));
            contentPane.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadGestionOffrePage(ActionEvent actionEvent) {
        loadPage("ConsulterOffre.fxml");
    }
}
