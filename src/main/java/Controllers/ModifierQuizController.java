package Controllers;

import Entities.ChoixPossible;
import Entities.Matiere;
import Entities.Question;
import Entities.Quiz;
import Services.ChoixPossibleService;
import Services.MatiereService;
import Services.QuestionService;
import Services.QuizService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ModifierQuizController {

    @FXML
    private Label titreLabel;
    @FXML
    private Button modifierTitre;
    @FXML
    private Button modifierDescription;
    @FXML
    private Button changerMatiere;
    @FXML
    private Button modifierQuestions;

    private QuizService quizService;
    private MatiereService matiereService;
    private QuestionService questionService;
    private Quiz quiz;
    private ChoixPossibleService choixPossibleService;

    public ModifierQuizController() {
        quizService = new QuizService();
        matiereService = new MatiereService();
        questionService = new QuestionService();
    }

    @FXML
    public void initialize() {
        quizService = new QuizService();
        modifierTitre.setOnAction(e -> handleModifierTitre());
        modifierDescription.setOnAction(e -> handleModifierDescription());
        changerMatiere.setOnAction(e -> handleChangerMatiere());
        modifierQuestions.setOnAction(this::handleModifierQuestions);
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        titreLabel.setText(quiz.getTitre());
    }

    public void handleModifierTitre() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Modifier le titre");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextField textField = new TextField();
        dialog.getDialogPane().setContent(textField);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                return textField.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(newTitre -> {
            try {
                this.quizService.updateTitre(this.quiz, newTitre);
                this.titreLabel.setText(newTitre);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update Successful");
                alert.setHeaderText(null);
                alert.setContentText("Title has been updated successfully!");
                alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void handleModifierDescription() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Modifier la description");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextArea textArea = new TextArea();
        dialog.getDialogPane().setContent(textArea);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                return textArea.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(newDescription -> {
            try {
                this.quizService.updateDescription(this.quiz, newDescription);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update Successful");
                alert.setHeaderText(null);
                alert.setContentText("Description has been updated successfully!");
                alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void handleChangerMatiere() {
        Dialog<Matiere> dialog = new Dialog<>();
        dialog.setTitle("Changer la matiere");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        ComboBox<Matiere> comboBox = new ComboBox<>();
        comboBox.setItems(FXCollections.observableArrayList(this.matiereService.getAllMatieres()));
        dialog.getDialogPane().setContent(comboBox);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveButtonType) {
                return comboBox.getValue();
            }
            return null;
        });

        Optional<Matiere> result = dialog.showAndWait();

        result.ifPresent(newMatiere -> {
            try {
                this.quizService.updateMatiere(this.quiz, newMatiere);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Update Successful");
                alert.setHeaderText(null);
                alert.setContentText("Matiere has been updated successfully!");
                alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void handleModifierQuestions(ActionEvent event) {
        try {
            List<Question> questionList = this.questionService.findAllByQuizId(this.quiz.getId());

            for (Question question : questionList) {
                System.out.println(question.getEnonce());
            }

            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Modifier Questions");

            ButtonType modifierEnonceButtonType = new ButtonType("Modifier Enoncé", ButtonBar.ButtonData.OK_DONE);
            ButtonType changerChoixPossibleButtonType = new ButtonType("Changer les Choix Possible", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(modifierEnonceButtonType, changerChoixPossibleButtonType, ButtonType.CANCEL);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == modifierEnonceButtonType) {
                    handleModifierEnonce(questionList);
                } else if (buttonType == changerChoixPossibleButtonType) {
                    try {
                        this.handleChangerChoixPossible(questionList);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
                return null;
            });

            dialog.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleModifierEnonce(List<Question> questionList) {
        // Select a question to modify
        Question questionToModify = selectQuestion(questionList);
        if (questionToModify == null) {
            return;
        }

        Dialog<String> modifierEnonceDialog = new Dialog<>();
        modifierEnonceDialog.setTitle("Modifier Enoncé");

        TextArea textArea = new TextArea();
        textArea.setText(questionToModify.getEnonce());
        modifierEnonceDialog.getDialogPane().setContent(textArea);

        ButtonType innerModifierEnonceButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        modifierEnonceDialog.getDialogPane().getButtonTypes().add(innerModifierEnonceButtonType);

        modifierEnonceDialog.setResultConverter(bt -> {
            if (bt == innerModifierEnonceButtonType) {
                return textArea.getText();
            }
            return null;
        });

        Optional<String> result = modifierEnonceDialog.showAndWait();

        result.ifPresent(newEnonce -> {
            // Here you can update the enoncé of the question
            questionToModify.setEnonce(newEnonce);
            try {
                questionService.updateEnonce(questionToModify, newEnonce);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void handleChangerChoixPossible(List<Question> questionList) throws SQLException {
        // Select a question to modify
        Question questionToModify = selectQuestion(questionList);
        if (questionToModify == null) {
            return;
        }

        ChoixPossible choixToModify = this.selectChoixPossible(questionToModify);
        if (choixToModify == null) {
            return;
        }

        Dialog<String> changerChoixPossibleDialog = new Dialog<>();
        changerChoixPossibleDialog.setTitle("Changer les Choix Possible");

        TextArea textArea = new TextArea();
        textArea.setText(choixToModify.getDescription());
        changerChoixPossibleDialog.getDialogPane().setContent(textArea);

        ButtonType innerChangerChoixPossibleButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        changerChoixPossibleDialog.getDialogPane().getButtonTypes().add(innerChangerChoixPossibleButtonType);

        changerChoixPossibleDialog.setResultConverter(bt -> {
            if (bt == innerChangerChoixPossibleButtonType) {
                return textArea.getText();
            }
            return null;
        });

        Optional<String> result = changerChoixPossibleDialog.showAndWait();

        result.ifPresent(newChoixPossible -> {
            // Here you can update the choix possible of the question
            choixToModify.setDescription(newChoixPossible);
            try {
                this.choixPossibleService.updateDescription(choixToModify);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private ChoixPossible selectChoixPossible(Question question) throws SQLException {
        Dialog<ChoixPossible> dialog = new Dialog<>();
        dialog.setTitle("Select a ChoixPossible");

        ComboBox<ChoixPossible> comboBox = new ComboBox<>();
        comboBox.setItems(FXCollections.observableArrayList(this.questionService.findAllChoixPossible(question)));
        dialog.getDialogPane().setContent(comboBox);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(okButtonType);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButtonType) {
                return comboBox.getValue();
            }
            return null;
        });

        Optional<ChoixPossible> result = dialog.showAndWait();
        return result.orElse(null);
    }
    private Question selectQuestion(List<Question> questionList) {
        Dialog<Question> dialog = new Dialog<>();
        dialog.setTitle("Select a Question");

        ComboBox<Question> comboBox = new ComboBox<>();
        comboBox.setItems(FXCollections.observableArrayList(questionList));
        dialog.getDialogPane().setContent(comboBox);

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(okButtonType);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == okButtonType) {
                return comboBox.getValue();
            }
            return null;
        });

        Optional<Question> result = dialog.showAndWait();
        return result.orElse(null);
    }

    public void handleCreerQuiz(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajoutQuiz.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Error while loading ajoutQuiz.fxml: " + e.getMessage());
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
}