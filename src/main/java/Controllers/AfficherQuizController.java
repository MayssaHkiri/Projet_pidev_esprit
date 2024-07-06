package Controllers;

import Entities.ChoixPossible;
import Entities.Question;
import Entities.Quiz;
import Entities.Reponse;
import Services.QuestionService;
import Services.ReponseService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AfficherQuizController implements Initializable {
    @FXML
    private Label titreLabel;
    @FXML
    private TextArea descriptionLabel;
    @FXML
    private TextArea dateCreationLabel;
    @FXML
    private TextArea matiereLabel;
    @FXML
    private VBox vboxContainer;
    private QuestionService questionService;
    private ReponseService reponseService;
    private Quiz quiz;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnCreerQuiz;

    public AfficherQuizController() {
        questionService = new QuestionService();
        reponseService = new ReponseService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnModifier.setOnAction(event -> {
            handleModifierButton(quiz);
        });
        btnCreerQuiz.setOnAction(event -> handleCreerQuiz());

    }

    public void displayQuiz(Quiz quiz) {
        this.quiz = quiz;
        titreLabel.setText(quiz.getTitre());
        descriptionLabel.setText(quiz.getDescription());
        dateCreationLabel.setText(quiz.getDateCreation().toString());
        matiereLabel.setText(quiz.getMatiereName());
    }

    @FXML
    public void showQuestionsDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Questions");

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        dialog.getDialogPane().setMinWidth(500);
        dialog.getDialogPane().setMinHeight(500);
        try {
            List<Question> questionList = questionService.findAllByQuizId(this.quiz.getId());
            for (Question question : questionList) {
                Label questionLabel = new Label(question.getEnonce());
                Button showAnswerButton = new Button("Afficher les reponses");
                showAnswerButton.setOnAction(e -> showAnswer(question.getId()));
                vbox.getChildren().addAll(questionLabel, showAnswerButton);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private void showAnswer(int questionId) {
        try {
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Choix Possibles");
            VBox vbox = new VBox();
            vbox.setSpacing(10);
            dialog.getDialogPane().setMinWidth(500);
            dialog.getDialogPane().setMinHeight(500);
            List<ChoixPossible> listeChoix= questionService.findAllChoixPossible(questionService.findById(questionId));

            for (ChoixPossible choixPossible : listeChoix) {
                Label label = new Label(choixPossible.getDescription());
                if (reponseService.isCorrect(choixPossible.getId())) {
                    label.setStyle("-fx-text-fill: green;");
                } else {
                    label.setStyle("-fx-text-fill: red;");
                }
                vbox.getChildren().addAll(label);
            }

            dialog.getDialogPane().setContent(vbox);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void handleConsulterQuiz(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gestionQuiz.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error while loading gestionQuiz.fxml: " + e.getMessage());
        }
    }
    public void handleModifierButton(Quiz quiz) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifierQuiz.fxml"));
            Parent root = loader.load();

            ModifierQuizController controller = loader.getController();
            controller.setQuiz(quiz);

            Stage stage = (Stage) titreLabel.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
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
}