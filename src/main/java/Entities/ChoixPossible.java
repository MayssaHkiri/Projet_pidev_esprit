package Entities;


public class ChoixPossible {
    private int id;
    private Question question;
    private Reponse reponse;
    private String description;

    public ChoixPossible(int id, Question question, String description) {
        this.id = id;
        this.question = question;
        this.description = description;
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

    public Reponse getReponse() {
        return reponse;
    }

    public void setReponse(Reponse reponse) {
        this.reponse = reponse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}