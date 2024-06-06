package Entities;

import java.util.Date;
import java.util.List;

public class Quiz {
    private int id;
    private String description, titre;
    private Date dateCr;
    private List<Question> questions; // One-to-many relationship with Question

    public Quiz(int id, String description, String titre, Date dateCr, List<Question> questions) {
        this.id = id;
        this.description = description;
        this.titre = titre;
        this.dateCr = dateCr;
        this.questions = questions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Date getDateCr() {
        return dateCr;
    }

    public void setDateCr(Date dateCr) {
        this.dateCr = dateCr;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}