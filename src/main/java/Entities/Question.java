package Entities;

import java.util.List;

public class Question {
    private int id;
    private Quiz quiz; // Many-to-one relationship with Quiz
    private String enonce;
    private List<Reponse> reponses; // One-to-many relationship with Reponse

    public Question(int id, Quiz quiz, String enonce, List<Reponse> reponses) {
        this.id = id;
        this.quiz = quiz;
        this.enonce = enonce;
        this.reponses = reponses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public String getEnonce() {
        return enonce;
    }

    public void setEnonce(String enonce) {
        this.enonce = enonce;
    }

    public List<Reponse> getReponses() {
        return reponses;
    }

    public void setReponses(List<Reponse> reponses) {
        this.reponses = reponses;
    }
}