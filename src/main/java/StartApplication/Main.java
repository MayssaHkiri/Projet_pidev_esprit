package StartApplication;

import Controllers.QuizController;
import Entities.Question;
import Entities.Quiz;
import Entities.Reponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        QuizController quizController = new QuizController();

        // Create a quiz
        Quiz quiz = new Quiz(0, "Quiz Description", "Quiz Title", new Date(), new ArrayList<>());

        // Create questions
        Question question1 = new Question(0, quiz, "Question 1", new ArrayList<>());
        Question question2 = new Question(1, quiz, "Question 2", new ArrayList<>());
        List<Question> questions = Arrays.asList(question1, question2);

        // Create responses
        Reponse reponse1 = new Reponse(0, question1, true, "Correct Answer to Question 1");
        Reponse reponse2 = new Reponse(1, question1, false, "Incorrect Answer to Question 1");
        Reponse reponse3 = new Reponse(2, question2, true, "Correct Answer to Question 2");
        Reponse reponse4 = new Reponse(3, question2, false, "Incorrect Answer to Question 2");
        List<List<Reponse>> reponses = new ArrayList<>();
        reponses.add(Arrays.asList(reponse1, reponse2));
        reponses.add(Arrays.asList(reponse3, reponse4));

        // Create the quiz
        quizController.createQuiz(quiz, questions, reponses);

        // Simulate a student answering the quiz
        int etudiantId = 1; // Replace with the actual student ID
        List<Reponse> studentResponses = Arrays.asList(reponse1, reponse3); // Replace with the actual student responses
        quizController.answerQuiz(etudiantId, quiz, studentResponses);

        // Check each response and update the quiz's score
        for (Reponse studentResponse : studentResponses) {
            quizController.checkResponseAndAddScore(quiz, studentResponse);
        }
        System.out.println("works!");
    }
}