package Controllers;

import Entities.User;
import Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AccountCreation implements Initializable {

    @FXML
    private ChoiceBox<String> myChoiceBox;
    private String[] roles = {"enseignant","etudiant"};
    UserService userService = new UserService() ;
    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfPrenom;

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
            } else {
                System.out.println("Échec de l'ajout de l'utilisateur");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myChoiceBox.getItems().addAll(roles);
        myChoiceBox.setOnAction(this::getRole);
    }
    public void getRole(ActionEvent event) {

        String myFood = myChoiceBox.getValue();

    }

}
