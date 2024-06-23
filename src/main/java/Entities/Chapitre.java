package Entities;

public class Chapitre {
    private int id;
    private String titre;
    private String descrip;
    private int idMatiere;

    public Chapitre(int id, String titre, String descrip, int idMatiere) {
        this.id = id;
        this.titre = titre;
        this.descrip = descrip;
        this.idMatiere = idMatiere;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public int getIdMatiere() {
        return idMatiere;
    }

    public void setIdMatiere(int idMatiere) {
        this.idMatiere = idMatiere;
    }
}