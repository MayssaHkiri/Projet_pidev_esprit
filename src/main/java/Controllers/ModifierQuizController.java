package Controllers;

import Entities.*;
import Services.*;
import Utils.StageManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

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
    @FXML
    private Label messageLabel;

    private QuizService quizService;
    private MatiereService matiereService;
    private QuestionService questionService;
    private ChoixPossible choixPossible;
    private ReponseService reponseService;
    private Quiz quiz;
    private int questionId;
    private ChoixPossibleService choixPossibleService;

    public ModifierQuizController() {
        quizService = new QuizService();
        matiereService = new MatiereService();
        questionService = new QuestionService();
        reponseService = new ReponseService();
        choixPossibleService = new ChoixPossibleService();
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
        titreLabel.setText(quiz.getDescription());
    }

    public void handleModifierTitre() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Modifier le titre");
        dialog.getDialogPane().setMinWidth(400);
        dialog.getDialogPane().setMinHeight(150);
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextField textField = new TextField();
        dialog.getDialogPane().setContent(textField);

        // Enable/disable save button based on text field content
        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true); // Initially disable save button

        // Listen for changes in the text field to enable/disable save button
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty()); // Disable if empty
        });

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
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Update Failed");
                alert.setHeaderText(null);
                alert.setContentText("Failed to update title: " + e.getMessage());
                alert.showAndWait();
            }
        });
    }


    public void handleModifierDescription() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Modifier la description");
        dialog.getDialogPane().setMinWidth(400);
        dialog.getDialogPane().setMinHeight(400);
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextArea textArea = new TextArea();
        dialog.getDialogPane().setContent(textArea);

        // Enable/disable save button based on text field content
        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true); // Initially disable save button

        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty()); // Disable if empty
        });

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
        dialog.getDialogPane().setMinWidth(400);
        dialog.getDialogPane().setMinHeight(100);
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

        Node saveButton = modifierEnonceDialog.getDialogPane().lookupButton(innerModifierEnonceButtonType);
        saveButton.setDisable(true);

        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            saveButton.setDisable(newValue.trim().isEmpty()); // Disable if empty
        });
        modifierEnonceDialog.setResultConverter(bt -> {
            if (bt == innerModifierEnonceButtonType) {
                return textArea.getText();
            }
            return null;
        });

        Optional<String> result = modifierEnonceDialog.showAndWait();

        result.ifPresent(newEnonce -> {
            questionToModify.setEnonce(newEnonce);
            messageLabel.setText("Enoncé modifié !.");
            try {
                questionService.updateEnonce(questionToModify, newEnonce);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void handleChangerChoixPossible(List<Question> questionList) throws SQLException {
        Question questionToModify = selectQuestion(questionList);
        if (questionToModify == null) {
            return;
        }

        ChoixPossible choixToModify = this.selectChoixPossible(questionToModify);
        if (choixToModify == null) {
            return;
        }

        Reponse reponse = reponseService.findByChoixPossibleIdAndQuestionId(choixToModify.getId(), questionToModify.getId());
        choixToModify.setReponse(reponse);

        Dialog<ChoixPossible> changerChoixPossibleDialog = new Dialog<>();
        changerChoixPossibleDialog.setTitle("Changer les Choix Possible");

        TextArea textArea = new TextArea();
        textArea.setText(choixToModify.getDescription());

        VBox dialogContent = new VBox(textArea);
        changerChoixPossibleDialog.getDialogPane().setContent(dialogContent);

        ButtonType innerChangerChoixPossibleButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        changerChoixPossibleDialog.getDialogPane().getButtonTypes().add(innerChangerChoixPossibleButtonType);

        changerChoixPossibleDialog.setResultConverter(bt -> {
            if (bt == innerChangerChoixPossibleButtonType) {
                choixToModify.setDescription(textArea.getText());
                return choixToModify;
            }
            return null;
        });

        Optional<ChoixPossible> result = changerChoixPossibleDialog.showAndWait();

        result.ifPresent(newChoixPossible -> {
            try {
                this.choixPossibleService.updateDescription(newChoixPossible);
                messageLabel.setText("Choix Possibles modifiés!");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private ChoixPossible selectChoixPossible(Question question) throws SQLException {
        Dialog<ChoixPossible> dialog = new Dialog<>();
        dialog.setTitle("Select a ChoixPossible");

        dialog.getDialogPane().setMinWidth(300);

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
        dialog.getDialogPane().setMinWidth(300);

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

    public void handleAjouterQuestion(ActionEvent event) {
        try {
            Dialog<ButtonType> enonceDialog = new Dialog<>();
            enonceDialog.setTitle("Enter Enoncé");

            ButtonType nextButtonType = new ButtonType("Suivant", ButtonBar.ButtonData.OK_DONE);
            enonceDialog.getDialogPane().getButtonTypes().addAll(nextButtonType, ButtonType.CANCEL);

            TextField enonceTextField = new TextField();
            enonceTextField.setPromptText("Enoncé");
            enonceDialog.getDialogPane().setContent(enonceTextField);

            Optional<ButtonType> enonceResult = enonceDialog.showAndWait();
            if (enonceResult.isPresent() && enonceResult.get() == nextButtonType) {
                String enonce = enonceTextField.getText();

                Question question = new Question(0, quizService.findById(this.quiz.getId()), enonce);
                int questionId = questionService.ajouter(question, this.quiz.getId());
                setQuestionId(questionId);

                handleAjouterChoixPossible(event);

                handleAjouterReponses(event);

                messageLabel.setText("Question ajoutée!");
            }
        } catch (SQLException e) {
            System.out.println("Error while saving question: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
    @FXML
    public void handleAjouterChoixPossible(ActionEvent event) {
        int questionId = getQuestionId();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter choix possible");

        ButtonType saveButtonType = new ButtonType("Sauvegarder", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        TextField textField1 = new TextField();
        textField1.setPromptText("Input 1");
        TextField textField2 = new TextField();
        textField2.setPromptText("Input 2");

        VBox vbox = new VBox();
        vbox.getChildren().addAll(textField1, textField2);

        Button ajouterChoixButton = new Button("Ajouter choix");
        ajouterChoixButton.setOnAction(e -> {
            TextField newTextField = new TextField();
            newTextField.setPromptText("New Input");
            vbox.getChildren().add(newTextField);
        });
        vbox.getChildren().add(ajouterChoixButton);

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);

        dialog.getDialogPane().setContent(scrollPane);

        Optional<ButtonType> result = dialog.showAndWait();

        result.ifPresent(buttonType -> {
            for (Node node : vbox.getChildren()) {
                if (node instanceof TextField textField) {
                    String text = textField.getText();
                    try {
                        ChoixPossible choixPossible = new ChoixPossible(0, questionService.findById(questionId), text);
                        choixPossibleService.ajouter(choixPossible);
                        System.out.println("Saved: " + text);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    @FXML
    public void handleAjouterReponses(ActionEvent event) throws SQLException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter réponses");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        VBox vbox = new VBox();

        Map<CheckBox, ChoixPossible> map = new HashMap<>();

        for (ChoixPossible choixPossible : questionService.findAllChoixPossible(questionService.findById(this.getQuestionId()))) {
            CheckBox checkBox = new CheckBox();
            Label label = new Label(choixPossible.getDescription());
            vbox.getChildren().addAll(checkBox, label);

            map.put(checkBox, choixPossible);
        }

        dialog.getDialogPane().setContent(vbox);

        Optional<ButtonType> result = dialog.showAndWait();

        result.ifPresent(buttonType -> {
            if (buttonType == saveButtonType) {
                for (Node node : vbox.getChildren()) {
                    if (node instanceof CheckBox checkBox && checkBox.isSelected()) {

                        ChoixPossible choixPossible = map.get(checkBox);

                        Reponse reponse = new Reponse(0, choixPossible, true);

                        try {
                            reponseService.ajouter(reponse);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (node instanceof CheckBox checkBox && !checkBox.isSelected()){
                        ChoixPossible choixPossible = map.get(checkBox);

                        Reponse reponse = new Reponse(0, choixPossible, false);
                        try {
                            reponseService.ajouter(reponse);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        messageLabel.setText("Réponses Ajoutées!");
                    }
                }
            }
        });
    }

    @FXML
    public void handleSupprimerQuestion() throws SQLException {
        Dialog<Question> dialog = new Dialog<>();
        dialog.setTitle("Supprimer une Question");
        dialog.setHeaderText("Sélectionnez une question à supprimer");

        ButtonType supprimerButtonType = new ButtonType("Supprimer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(supprimerButtonType, ButtonType.CANCEL);

        ListView<Question> listView = new ListView<>();
        listView.setCellFactory(param -> new ListCell<Question>() {
            @Override
            protected void updateItem(Question item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getEnonce() == null) {
                    setText(null);
                } else {
                    setText(item.getEnonce());
                }
            }
        });
        ArrayList<Question> questions = new ArrayList<>(quizService.findAllQuestionsByQuizId(this.quiz.getId()));
        listView.setItems(FXCollections.observableArrayList(questions));
        dialog.getDialogPane().setContent(listView);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == supprimerButtonType) {
                return listView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        Optional<Question> result = dialog.showAndWait();

        result.ifPresent(question -> {
            try {
                quizService.deleteChoixPossibleByQuestionId(question.getId());

                quizService.deleteQuestionByQuizId(this.quiz.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
    public void handleAccueil(ActionEvent actionEvent) {
        Stage mainTeacherStage = StageManager.getInstance().getMainTeacherStage();
        if (mainTeacherStage != null && mainTeacherStage.isShowing()) {
            ((Node) actionEvent.getSource()).getScene().getWindow().hide();
        } else {
            ((Node) actionEvent.getSource()).getScene().getWindow().hide();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainTeacher.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                StageManager.getInstance().setMainTeacherStage(stage);
            } catch (IOException e) {
                System.out.println("Error while loading mainTeacher.fxml: " + e.getMessage());
            }
        }
    }
}