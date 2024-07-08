package Controllers;

import Entities.User;
import Utils.StageManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import Services.QuizService;
import Entities.Quiz;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class HomeQuizController {
    @FXML
    private TableView<Quiz> quizTable;
    @FXML
    private TableColumn<Quiz, String> titre;
    @FXML
    private TableColumn<Quiz, String> matiere;
    @FXML
    private TableColumn<Quiz, Void> actions;
    @FXML
    private Button btnCreerQuiz;
    private User authenticatedUser;

    private QuizService quizService;
    @FXML
    private MainTeacherController mainTeacherController;
    public HomeQuizController() {
        quizService = new QuizService();
    }

    @FXML
    public void initialize() {
        findAll();
        actions.setCellFactory(param -> new TableCell<>() {
            private final Button btnAfficher = new Button("Afficher");
            private final Button btnModifier = new Button("Modifier");
            private final Button btnSupprimer = new Button("Supprimer");
            private final HBox pane = new HBox(btnAfficher, btnModifier, btnSupprimer);

            {

                pane.setAlignment(Pos.CENTER); // Center the buttons
                btnAfficher.setOnAction(event -> {
                    mainTeacherController.loadPage("affichageQuiz.fxml", authenticatedUser, getTableView().getItems().get(getIndex()));
                });

                btnModifier.setOnAction(event -> {
                    mainTeacherController.loadPage("modifierQuiz.fxml", authenticatedUser, getTableView().getItems().get(getIndex()));
                });

                btnSupprimer.setOnAction(event -> {
                    Quiz quiz = getTableView().getItems().get(getIndex());

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Supprimer le Quiz");
                    alert.setContentText("Êtes-vous sûr de vouloir supprimer ce quiz ?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        try {
                            quizService.supprimer(quiz);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        findAll();
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });
    }

    public void findAll() {
        try {
            ObservableList<Quiz> quizzes = FXCollections.observableArrayList(quizService.readAll());
            quizTable.setItems(quizzes);
            titre.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
            matiere.setCellValueFactory(cellData -> cellData.getValue().matiereProperty());
        } catch (SQLException e) {
            System.out.println("Error while displaying all quizzes: " + e.getMessage());
        }
    }

    public void handleCreationQuiz(ActionEvent actionEvent) {
        mainTeacherController.loadPage("ajoutQuiz.fxml", authenticatedUser, null);
    }
    public void handleModifierQuiz(ActionEvent actionEvent) {
        Quiz selectedQuiz = quizTable.getSelectionModel().getSelectedItem();
        if (selectedQuiz != null) {
            mainTeacherController.loadPage("modifierQuiz.fxml", authenticatedUser, selectedQuiz);
        } else {
            System.out.println("No quiz selected");
        }
    }

    public void handleAffichageQuiz(ActionEvent actionEvent) {
        Quiz selectedQuiz = quizTable.getSelectionModel().getSelectedItem();
        if (selectedQuiz != null) {
            mainTeacherController.loadPage("affichageQuiz.fxml", authenticatedUser, selectedQuiz);
        } else {
            System.out.println("No quiz selected");
        }
    }

    public void setMainTeacherController(MainTeacherController mainTeacherController) {
        this.mainTeacherController = mainTeacherController;
    }
}