package Controllers;

import Entities.Question;
import Entities.Quiz;
import Entities.QuizAttempt;
import Entities.Reponse;
import Services.QuestionService;
import Services.QuizAttemptService;
import Services.QuizService;
import Services.ReponseService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuizController {
    private final QuizService quizService = new QuizService();
    private final QuestionService questionService = new QuestionService();
    private final ReponseService reponseService = new ReponseService();
    private final QuizAttemptService quizAttemptService = new QuizAttemptService();

    public void createQuiz(Quiz quiz, List<Question> questions, List<List<Reponse>> reponses) {
        try {
            quizService.ajouter(quiz);
            for (int i = 0; i < questions.size(); i++) {
                Question question = questions.get(i);
                question.setQuiz(quiz);
                questionService.ajouter(question);
                for (Reponse reponse : reponses.get(i)) {
                    reponse.setQuestion(question);
                    reponseService.ajouter(reponse);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while creating quiz");
        }
    }

    public void answerQuiz(int etudiantId, Quiz quiz, List<Reponse> reponses) {
        try {
            List<Integer> questionIds = new ArrayList<>();
            List<Integer> reponseIds = new ArrayList<>();
            int score = 0;

            for (Question question : quiz.getQuestions()) {
                questionIds.add(question.getId());
                for (Reponse userResponse : reponses) {
                    if (userResponse.getQuestion().getId() == question.getId()) {
                        reponseIds.add(userResponse.getId());
                        if (checkResponseAndAddScore(quiz, userResponse)) {
                            score++;
                        }
                    }
                }
            }

            QuizAttempt quizAttempt = new QuizAttempt(0, etudiantId, quiz.getId(), questionIds, reponseIds, score, new Date());
            quizAttemptService.ajouter(quizAttempt);
        } catch (SQLException e) {
            System.out.println("Error while answering quiz");
        }
    }

    public boolean checkResponseAndAddScore(Quiz quiz, Reponse reponse) {
        for (Question question : quiz.getQuestions()) {
            for (Reponse correctReponse : question.getReponses()) {
                if (correctReponse.getId() == reponse.getId() && correctReponse.isCorrect()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void updateQuiz(Quiz quiz) {
        try {
            quizService.update(quiz);
        } catch (SQLException e) {
            System.out.println("Error while updating quiz");
        }
    }

    public void deleteQuiz(Quiz quiz) {
        try {
            quizService.supprimer(quiz);
        } catch (SQLException e) {
            System.out.println("Error while deleting quiz");
        }
    }
}