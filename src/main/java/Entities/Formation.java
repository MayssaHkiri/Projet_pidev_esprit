package Entities;

public class Formation {
    private int id;
    private String titre;
    private String description;
    private String imageUrl;
    private int idEnseignant;

    public Formation() {}

    public Formation(int id, String titre, String description, String imageUrl, int idEnseignant) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.imageUrl = imageUrl;
        this.idEnseignant = idEnseignant;
    }

    public Formation(String titre, String description, String imageUrl, int idEnseignant) {
        this.titre = titre;
        this.description = description;
        this.imageUrl = imageUrl;
        this.idEnseignant = idEnseignant;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getIdEnseignant() {
        return idEnseignant;
    }

    public void setIdEnseignant(int idEnseignant) {
        this.idEnseignant = idEnseignant;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", idEnseignant=" + idEnseignant +
                '}';
    }
}
