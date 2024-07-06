package Controllers;

import Entities.ChoixPossible;
import Entities.Question;
import Entities.Quiz;
import Services.ChoixPossibleService;
import Services.QuestionService;
import Services.QuizService;
import Services.ReponseService;
import Utils.StageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ListQuizEtudiantController {
    @FXML
    private TableView<Quiz> quizTable;
    @FXML
    private TableColumn<Quiz, String> titre;
    @FXML
    private TableColumn<Quiz, String> description;
    @FXML
    private TableColumn<Quiz, Button> actions;

    private QuizService quizService;
    private QuestionService questionService;
    private ReponseService reponseService;
    private String selectedMatiere;

    public ListQuizEtudiantController() {
        quizService = new QuizService();
        questionService = new QuestionService();
        reponseService = new ReponseService();
    }

    public void setMatiere(String matiere) {
        this.selectedMatiere = matiere;
        displayQuizzes();
    }

    private void displayQuizzes() {
        try {
            ObservableList<Quiz> quizzes = FXCollections.observableArrayList(quizService.findAllByMatiere(selectedMatiere));
            for (Quiz quiz : quizzes) {
                List<Question> questions = quizService.findAllQuestionsByQuizId(quiz.getId());
                for (Question question : questions) {
                    List<ChoixPossible> choixPossibles = quizService.findAllChoixPossibleByQuestionId(question.getId());
                    question.setChoixPossibles(choixPossibles);
                }
                quiz.setQuestions(questions);
            }
            quizTable.setItems(quizzes);
            titre.setCellValueFactory(cellData -> cellData.getValue().titreProperty());
            description.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
            actions.setCellFactory(param -> new TableCell<>() {
                private final Button btn = new Button("Passer Quiz");


                @Override
                protected void updateItem(Button item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                        btn.setOnAction(event -> {
                            Quiz quiz = getTableView().getItems().get(getIndex());
                            List<Question> questions = null;
                            try {
                                questions = quizService.findAllQuestionsByQuizId(quiz.getId());
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                            Dialog<Entities.QuizResult> dialog = new Dialog<>();
                            dialog.setTitle("Quiz");
                            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.NEXT, ButtonType.CANCEL);
                            dialog.getDialogPane().setMinWidth(500);
                            dialog.getDialogPane().setMinHeight(500);
                            dialog.getDialogPane().setStyle("-fx-font-size: 16");

                            int[] score = {0};

                            List<Question> finalQuestions = questions;
                            dialog.setResultConverter(buttonType -> {
                                if (buttonType == ButtonType.NEXT) {
                                    // Calculate score
                                    return new Entities.QuizResult(quiz, score[0], score[0] >= 0.8 * finalQuestions.size());
                                }
                                return null;
                            });

                            // Display the first question
                            try {
                                displayQuestion(dialog, questions, 0, score);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                            Optional<Entities.QuizResult> result = dialog.showAndWait();
                            result.ifPresent(quizResult -> {
                                if (quizResult.isPassed()) {
                                    System.out.println("Félications t'as passé le quiz");
                                } else {
                                    System.out.println("Echec, faut avoir 80% pour passer");
                                }
                            });
                        });
                        setAlignment(Pos.CENTER); // This line centers the button in the column
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayQuestion(Dialog<Entities.QuizResult> dialog, List<Question> questions, int index, int[] score) throws SQLException {
        if (index >= questions.size()) {
            // All questions have been answered, display the result
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getDialogPane().setMinWidth(500);
            System.out.println(questions.size());
            int passingScore = (int) (questions.size() * 0.8);
            if (score[0] >= passingScore) {
                alert.setTitle("Félicitations!");
                alert.setHeaderText(null);
                alert.setContentText("Vous avez passer le quiz!");
                dialog.close();
            } else {
                alert.setTitle("Try Again");
                alert.setHeaderText(null);
                alert.setContentText("Echec. Bonne chance la prochaine fois!");
                dialog.close();
            }
            alert.showAndWait();
            return;
        }

        Question question = questions.get(index);
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Label questionLabel = new Label(question.getEnonce());
        questionLabel.setStyle("-fx-font-size: 24;");
        vbox.getChildren().add(questionLabel);

        for (ChoixPossible choix : quizService.findAllChoixPossibleByQuestionId(question.getId())) {
            CheckBox checkBox = new CheckBox();
            checkBox.setStyle("-fx-font-size: 24;");
            Label label = new Label(choix.getDescription());
            label.setStyle("-fx-font-size: 24;");
            HBox hbox = new HBox(checkBox, label);
            hbox.setSpacing(10);
            vbox.getChildren().add(hbox);
        }

        dialog.getDialogPane().setContent(vbox);

        ButtonType nextButtonType = new ButtonType("Next", ButtonBar.ButtonData.NEXT_FORWARD);
        dialog.getDialogPane().getButtonTypes().setAll(nextButtonType, ButtonType.CANCEL);

        Node nextButton = dialog.getDialogPane().lookupButton(nextButtonType);
        nextButton.addEventFilter(ActionEvent.ACTION, event -> {
            // Check answers and calculate score
            for (Node node : vbox.getChildren()) {
                if (node instanceof HBox) {
                    HBox hbox = (HBox) node;
                    CheckBox checkBox = (CheckBox) hbox.getChildren().get(0);
                    Label label = (Label) hbox.getChildren().get(1);
                    try {
                        boolean isCorrect = questionService.findAllChoixPossible(question).stream().anyMatch(choix -> {
                            try {
                                return choix.getDescription().equals(label.getText()) && reponseService.isCorrect(choix.getId());
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        if (checkBox.isSelected() && isCorrect) {
                            score[0]++;
                        } else if (checkBox.isSelected() && !isCorrect) {
                            score[0]--;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            // Display next question
            try {
                displayQuestion(dialog, questions, index + 1, score);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            event.consume();
        });
    }
    public void handleAccueil(ActionEvent actionEvent) {
        Stage mainStudentStage = StageManager.getInstance().getMainStudentStage();
        if (mainStudentStage != null && mainStudentStage.isShowing()) {
            ((Node) actionEvent.getSource()).getScene().getWindow().hide();
        } else {
            ((Node) actionEvent.getSource()).getScene().getWindow().hide();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainStudent.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                StageManager.getInstance().setMainStudentStage(stage);
            } catch (IOException e) {
                System.out.println("Error while loading mainTeacher.fxml: " + e.getMessage());
            }
        }
    }
}
