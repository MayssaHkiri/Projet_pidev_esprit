package Entities;

public class Reponse {
    private int id;
    private Question question; // Many-to-one relationship with Question
    private boolean correct;
    private String Contenu;

    public Reponse(int id, Question question, boolean correct, String Contenu) {
        this.id = id;
        this.question = question;
        this.correct = correct;
        this.Contenu = Contenu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getContenu() {
        return Contenu;
    }

    public void setContenu(String contenu) {
        Contenu = contenu;
    }
}