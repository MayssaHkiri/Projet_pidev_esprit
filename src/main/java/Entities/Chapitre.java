package Entities;

public class Chapitre {
    private int id;
    private String chapitreTitre;
    private String chapitreDescription;
    private int idMatiere;

    // Constructor with all attributes
    public Chapitre(int id, String chapitreTitre, String chapitreDescription, int idMatiere) {
        this.id = id;
        this.chapitreTitre = chapitreTitre;
        this.chapitreDescription = chapitreDescription;
        this.idMatiere = idMatiere;
    }

    // Constructor without id (for new entries)
    public Chapitre(String chapitreTitre, String chapitreDescription, int idMatiere) {
        this.chapitreTitre = chapitreTitre;
        this.chapitreDescription = chapitreDescription;
        this.idMatiere = idMatiere;
    }
    public Chapitre(String chapitreTitre, String chapitreDescription) {
        this.chapitreTitre = chapitreTitre;
        this.chapitreDescription = chapitreDescription;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChapitreTitre() {
        return chapitreTitre;
    }

    public void setChapitreTitre(String chapitreTitre) {
        this.chapitreTitre = chapitreTitre;
    }

    public String getChapitreDescription() {
        return chapitreDescription;
    }

    public void setChapitreDescription(String chapitreDescription) {
        this.chapitreDescription = chapitreDescription;
    }

    public int getIdMatiere() {
        return idMatiere;
    }

    public void setIdMatiere(int idMatiere) {
        this.idMatiere = idMatiere;
    }

    @Override
    public String toString() {
        return chapitreTitre;
    }
}
