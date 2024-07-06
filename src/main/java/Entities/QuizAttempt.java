package Entities;

import java.util.Date;
import java.util.List;

public class QuizAttempt {
    private int id;
    private int etudiantId;
    private int quizId;
    private List<Integer> questionIds;
    private List<Integer> reponseIds;
    private int score;
    private Date dateAttempt;

    public QuizAttempt(int id, int etudiantId, int quizId, List<Integer> questionIds, List<Integer> reponseIds, int score, Date dateAttempt) {
        this.id = id;
        this.etudiantId = etudiantId;
        this.quizId = quizId;
        this.questionIds = questionIds;
        this.reponseIds = reponseIds;
        this.score = score;
        this.dateAttempt = dateAttempt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEtudiantId() {
        return etudiantId;
    }

    public void setEtudiantId(int etudiantId) {
        this.etudiantId = etudiantId;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public List<Integer> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<Integer> questionIds) {
        this.questionIds = questionIds;
    }

    public List<Integer> getReponseIds() {
        return reponseIds;
    }

    public void setReponseIds(List<Integer> reponseIds) {
        this.reponseIds = reponseIds;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getDateAttempt() {
        return dateAttempt;
    }

    public void setDateAttempt(Date dateAttempt) {
        this.dateAttempt = dateAttempt;
    }
}