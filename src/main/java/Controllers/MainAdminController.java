package Controllers;

import Entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainAdminController {

    private User authenticatedUser;

    public void setUser(User user) {
        this.authenticatedUser = user;

    }

    @FXML
    private StackPane contentPane;

    @FXML
    private void loadGestionFormations() {
        loadPage("GererFormation.fxml", authenticatedUser);
    }

    @FXML
    private void loadGestionUsers() {
        loadPage("usersManagement.fxml", authenticatedUser);
    }

    @FXML
    private void loadProfil() {
        loadPage("UserProfile.fxml", authenticatedUser);
    }

    private void loadPage(String fxmlFile, User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/" + fxmlFile));
            Pane pane = loader.load();

            Object controller = loader.getController();
            if (controller instanceof UserProfileController) {
                ((UserProfileController) controller).setUser(user);
            } 
            if (controller instanceof GererFormation ) {
                ((GererFormation) controller).setUser(user) ;
            }
            if (controller instanceof UsersManagement  ) {
                ((UsersManagement) controller).setUser(user) ;
            }
            if (controller instanceof ConsulterOffre  ) {
                ((ConsulterOffre) controller).setUser(user) ;
            }

            contentPane.getChildren().setAll(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGestionOffrePage(ActionEvent actionEvent) {
        loadPage("ConsulterOffre.fxml", authenticatedUser);
    }
}
