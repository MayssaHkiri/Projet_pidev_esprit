package Entities;

import java.util.List;

public class Matiere {
    private int id;
    private String nom;
    private int coeff;
    private String ModeEval;
    private List<Quiz> quizzes;


    public Matiere(int id, String nom, List<Quiz> quizzes) {
        this.id = id;
        this.nom = nom;
        this.quizzes = quizzes;
    }

    public Matiere(String matiereName) {
        this.nom = matiereName;
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

    public int getCoeff() {
        return coeff;
    }

    public void setCoeff(int coeff) {
        this.coeff = coeff;
    }

    public String getModeEval() {
        return ModeEval;
    }

    public void setModeEval(String modeEval) {
        ModeEval = modeEval;
    }

    public Matiere(int id, String nom, int coeff, String modeEval) {
        this.id = id;
        this.nom = nom;
        this.coeff = coeff;
        ModeEval = modeEval;
    }

    public Matiere(String nom, int coeff, String modeEval) {
        this.nom = nom;
        this.coeff = coeff;
        ModeEval = modeEval;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    @Override
    public String toString() {
        return nom;
    }
}


