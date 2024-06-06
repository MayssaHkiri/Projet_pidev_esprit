package Entities;

public class Formation {
    private int id;
    private String titre;
    private String description;
    private int idEnseignant;

    // Constructeurs, getters et setters
    public Formation(int id, String titre, String description, int idEnseignant) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.idEnseignant = idEnseignant;
    }

    // Constructeur supplémentaire si nécessaire
    public Formation(int id, String titre, String description) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        // L'ID de l'enseignant n'est pas initialisé dans ce constructeur
        // Si nécessaire, tu peux ajouter une logique pour l'initialiser
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

    public int getIdEnseignant() {
        return idEnseignant;
    }

    public void setIdEnseignant(int idEnseignant) {
        this.idEnseignant = idEnseignant;
    }
}
