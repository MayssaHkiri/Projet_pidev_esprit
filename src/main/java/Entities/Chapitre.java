package Entities;

public class Chapitre {
    private int id;
    private String titre;
    private String description;
    private int idMatiere;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdMatiere() {
        return idMatiere;
    }

    public void setIdMatiere(int idMatiere) {
        this.idMatiere = idMatiere;
    }

    public Chapitre(int id, String titre, String description, int idMatiere) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.idMatiere = idMatiere;
    }

    public Chapitre(String titre, String description, int idMatiere) {
        this.titre = titre;
        this.description = description;
        this.idMatiere = idMatiere;
    }

    @Override
    public String toString() {
        return "Chapitre{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", idMatiere=" + idMatiere +
                '}';
    }
}

