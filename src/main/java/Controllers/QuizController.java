package Controllers;

import Entities.*;
import Services.*;
import Utils.DataSource;
import Utils.StageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class QuizController implements Initializable {
    private final MatiereService matiereService = new MatiereService();
    private final QuizService quizService = new QuizService();
    private final QuestionService questionService = new QuestionService();
    private final ReponseService reponseService = new ReponseService();
    private final ChoixPossibleService choixPossibleService = new ChoixPossibleService();
    @FXML
    private ComboBox<String> matiereComboBox;
    @FXML
    private TextField titreField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextArea enonceTextArea;
    @FXML
    private Label messageLabel;
    private Connection con;
    private int questionId;
    private int quizId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        matiereComboBox.setItems(getMatieres());
        con = DataSource.getInstance().getCon();
    }

    private void checkConnection() throws SQLException {
        if (con == null || con.isClosed()) {
            con = DataSource.getInstance().getCon();
        }
    }


    public void createNewQuiz(ActionEvent event) {
        try {
            String titre = titreField.getText();
            String description = descriptionArea.getText();
            String matiereName = matiereComboBox.getValue();
            String enonce = enonceTextArea.getText();

            if (!titre.isEmpty() && !description.isEmpty() && matiereName != null && !enonce.isEmpty()) {
                Matiere matiere = matiereService.getMatiereByName(matiereName);
                System.out.println(matiere);
                if (matiere == null) {
                    System.out.println("Selected matiere not found");
                    return;
                }

                Quiz quiz = new Quiz(0, description, titre, new Date());
                quiz.setMatiere(matiere);

                int quizId = quizService.ajouter(quiz);
                this.setQuizId(quizId);
                Question question = new Question(0, quiz, enonce);
                int questionId = questionService.ajouter(question, quizId);
                setQuestionId(questionId);
                handleAjouterChoixPossible(event);

                messageLabel.setText("Question crée, ajouter maintenant les choix possibles.");

                titreField.clear();
                descriptionArea.clear();
                matiereComboBox.getSelectionModel().clearSelection();
                enonceTextArea.clear();
            } else {
                Question question = new Question(0, quizService.findById(this.quizId), enonce);
                int questionId = questionService.ajouter(question, quizId);
                setQuestionId(questionId);
                handleAjouterChoixPossible(event);

                messageLabel.setText("Question ajoutée! Ajouter maintenant les choix possibles.");
            }
        } catch (SQLException e) {
            System.out.println("Error while saving question: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }


    public ObservableList<String> getMatieres() {
        ObservableList<String> matieres = FXCollections.observableArrayList();

        List<Matiere> fetchedData = matiereService.getAllMatieres();

        for (Matiere matiere : fetchedData) {
            matieres.add(matiere.getNom());
        }

        return matieres;
    }

    @FXML
    public void handleAjouterChoixPossible(ActionEvent event) {
        int questionId = getQuestionId();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Ajouter choix possible");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
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
            messageLabel.setText("Les choix sont bien enregistrés, ajouter maintenant les réponses.");
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

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
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
