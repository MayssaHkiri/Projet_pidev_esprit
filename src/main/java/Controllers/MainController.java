package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController {
    @FXML
    private StackPane contentPane;


    @FXML
    private void loadGestionCoursPage() throws IOException {
        loadPage("CoursView.fxml");
    }

    @FXML
    private void loadGestionMatieresPage() throws IOException {
        loadPage("MatiereView.fxml");
    }

    @FXML
    private void loadGestionFormationPage() throws IOException {
        loadPage("FormationView.fxml");
    }

    private void loadPage(String fxmlFile) throws IOException {
        try {
            Pane pane = FXMLLoader.load(getClass().getResource("/" + fxmlFile));
            contentPane.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadGestionOffrePage() throws IOException {
        loadPage("ConsulterOffre.fxml");
    }

}
