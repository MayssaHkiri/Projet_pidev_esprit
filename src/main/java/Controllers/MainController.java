package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class MainController {
    @FXML
    private StackPane contentPane;

    @FXML
    private void loadGestionCoursPage() {
        loadPage("CoursView.fxml");
    }

    @FXML
    private void loadGestionMatieresPage() {
        loadPage("MatiereView.fxml");
    }

    @FXML
    private void loadGestionFormationPage() {
        loadPage("FormationView.fxml");
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
