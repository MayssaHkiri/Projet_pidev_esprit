package Entities;

import java.sql.Blob;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialBlob;

public class Formation {
    private int id;
    private String titre;
    private String description;
    private Blob imageFormation; // Changer le type pour String
    private String dateFormation; // Changer le type pour String

    public Formation() {
    }

    public Formation(int id, String titre, String description, Blob imageFormation, String dateFormation) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.imageFormation = imageFormation;
        this.dateFormation = dateFormation;
    }

    // Getters and setters for dateFormation

    public String getDateFormation() {
        return dateFormation;
    }

    public void setDateFormation(String dateFormation) {
        this.dateFormation = dateFormation;
    }

    // Getters and setters for other attributes

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

    public void setImageFormation(Blob imageFormation) {
        this.imageFormation = imageFormation;
    }

    public Blob getImageFormation() {
        return imageFormation;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", imageFormation=" + imageFormation +
                ", dateFormation='" + dateFormation + '\'' + // Include dateFormation in the toString method
                '}';
    }
}