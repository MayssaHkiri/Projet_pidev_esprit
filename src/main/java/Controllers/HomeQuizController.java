package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import Services.QuizService;
import Entities.Quiz;
import javafx.stage.Stage;

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

    private QuizService quizService;

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
                btnCreerQuiz.setOnAction(event -> handleCreerQuiz());

                pane.setAlignment(Pos.CENTER); // Center the buttons
                btnAfficher.setOnAction(event -> {
                    Quiz quiz = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/affichageQuiz.fxml"));
                        Parent root = loader.load();

                        // Get the controller of the new interface and pass the selected Quiz
                        AfficherQuizController controller = loader.getController();
                        controller.displayQuiz(quiz);

                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                btnModifier.setOnAction(event -> {
                    Quiz quiz = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierQuiz.fxml"));
                        Parent root = loader.load();

                        ModifierQuizController controller = loader.getController();
                        controller.setQuiz(quiz);

                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();

                        // Add a close request handler to refresh the table view
                        stage.setOnCloseRequest(e -> findAll());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
            titre.setCellValueFactory(cellData -> cellData.getValue().titreProperty());
            matiere.setCellValueFactory(cellData -> cellData.getValue().matiereProperty());
        } catch (SQLException e) {
            System.out.println("Error while displaying all quizzes: " + e.getMessage());
        }
    }
    @FXML
    public void handleCreerQuiz() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutQuiz.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) btnCreerQuiz.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error while loading ajoutQuiz.fxml: " + e.getMessage());
        }
    }

    public void handleAjouterChoixPossible(ActionEvent actionEvent) {
    }
}