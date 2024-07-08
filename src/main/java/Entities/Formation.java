package Entities;

import java.sql.Blob;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialBlob;

public class Formation {
    private int id;
    private String titre;
    private String description;
    private Blob imageFormation; // Changer le type pour String si utilisation d'un chemin de fichier ou d'une URL
    private String dateFormation;
    private int NumberOfStudentsEnrolled; // Nouvel attribut

    public Formation() {
    }

    public Formation(int id, String titre, String description, Blob imageFormation, String dateFormation) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.imageFormation = imageFormation;
        this.dateFormation = dateFormation;
        this.NumberOfStudentsEnrolled = 0; // Initialisation à 0
    }

    // Getters and setters pour tous les attributs

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

    public Blob getImageFormation() {
        return imageFormation;
    }

    public void setImageFormation(Blob imageFormation) {
        this.imageFormation = imageFormation;
    }

    public String getDateFormation() {
        return dateFormation;
    }

    public void setDateFormation(String dateFormation) {
        this.dateFormation = dateFormation;
    }

    public int getNombreEtudiantsInscrits() {
        return NumberOfStudentsEnrolled;
    }

    public void setNombreEtudiantsInscrits(int nombreEtudiantsInscrits) {
        this.NumberOfStudentsEnrolled = nombreEtudiantsInscrits;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", imageFormation=" + imageFormation +
                ", dateFormation='" + dateFormation + '\'' +
                ", nombreEtudiantsInscrits=" + NumberOfStudentsEnrolled +
                '}';
    }
}
