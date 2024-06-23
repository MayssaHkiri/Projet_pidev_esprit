package Entities;

import java.util.List;

public class Question {
    private int id;
    private Quiz quiz;
    private String enonce;
    private List<ChoixPossible> choixPossibles;

    public Question(int id, Quiz quiz, String enonce, List<ChoixPossible> choixPossibles) {
        this.id = id;
        this.quiz = quiz;
        this.enonce = enonce;
        this.choixPossibles = choixPossibles;
    }

    public Question(int id, Quiz quiz, String enonce) {
        this.id = id;
        this.quiz = quiz;
        this.enonce = enonce;
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
    public List<ChoixPossible> getChoixPossibles() {
        return choixPossibles;
    }
    public void setChoixPossibles(List<ChoixPossible> choixPossibles) {
        this.choixPossibles = choixPossibles;
    }
}