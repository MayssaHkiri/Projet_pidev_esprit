package Entities;

import java.sql.Blob;
import java.sql.SQLException;
import javax.sql.rowset.serial.SerialBlob;
import java.util.Date; // Import Date class for dateFormation

public class Formation {
    private int id;
    private String titre;
    private String description;
    private Blob imageFormation; // Changer le type pour String
    private Date dateFormation; // New attribute

    public Formation() {
    }

    public Formation(int id, String titre, String description, Blob imageFormation, Date dateFormation) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.imageFormation = imageFormation;
        this.dateFormation = dateFormation;
    }

    // Getters and setters for dateFormation

    public Date getDateFormation() {
        return dateFormation;
    }

    public void setDateFormation(Date dateFormation) {
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
                ", dateFormation=" + dateFormation + // Include dateFormation in the toString method
                '}';
    }
}
