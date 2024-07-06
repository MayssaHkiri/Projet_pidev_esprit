package Entities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;
import java.util.List;

public class Quiz {
    private int id;
    private StringProperty description;
    private StringProperty titre;
    private Date dateCreation;
    private List<Question> questions;
    private Matiere matiere;


    public Quiz(int id, String description, String titre, Date dateCreation, List<Question> questions) {
        this.id = id;
        this.description = new SimpleStringProperty(description);
        this.titre = new SimpleStringProperty(titre);
        this.dateCreation = dateCreation;
        this.questions = questions;
    }

    public Quiz(String description, String titre, Date dateCreation, List<Question> questions) {
        this.description = new SimpleStringProperty(description);
        this.titre = new SimpleStringProperty(titre);
        this.dateCreation = dateCreation;
        this.questions = questions;
    }

    public Quiz(int i, String titre, String description, Date date) {
        this.id = i;
        this.titre = new SimpleStringProperty(titre);
        this.description = new SimpleStringProperty(description);
        this.dateCreation = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getTitre() {
        return titre.get();
    }

    public void setTitre(String titre) {
        this.titre.set(titre);
    }

    public StringProperty titreProperty() {
        return titre;
    }
    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public StringProperty matiereProperty() {
        return new SimpleStringProperty(matiere.getNom());
    }
    public String getMatiereName() {
        return this.matiere.getNom();
    }
}