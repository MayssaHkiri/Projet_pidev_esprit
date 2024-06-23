package Entities;

import java.util.List;

public class Matiere {
    private int id;
    private String nom;
    private List<Quiz> quizzes;


    public Matiere(int id, String nom, List<Quiz> quizzes) {
        this.id = id;
        this.nom = nom;
        this.quizzes = quizzes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }
}