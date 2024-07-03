package Entities;

import java.io.File;
import java.sql.Blob;

public class Cours {
    private int id;
    private String titre;
    private String description;
    private int enseignantId;
    private int idChapitre;
    private Blob pdfFile; // New attribute for the PDF file


    public Cours(int coursId, String titre, String description,int enseignantId, int idChapitre,Blob pdfFile) {
        this.id = coursId;
        this.titre = titre;
        this.description = description;
        this.enseignantId = enseignantId;
        this.pdfFile = pdfFile;
    }

    public Cours(String titre, String description,int enseignantId, int idChapitre,Blob pdfFile) {

        this.titre = titre;
        this.description = description;
        this.enseignantId = enseignantId;
        this.idChapitre = idChapitre;
        this.pdfFile = pdfFile;
    }

    public Cours(String titre, String description, Blob pdfFile) {
        this.titre = titre;
        this.description = description;
        this.pdfFile = pdfFile;
    }


    public int getIdChapitre() {
        return idChapitre;
    }

    public void setIdChapitre(int idChapitre) {
        this.idChapitre = idChapitre;
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


    public int getEnseignantId() {
        return enseignantId;
    }

    public void setEnseignantId(int enseignantId) {
        this.enseignantId = enseignantId;
    }

    public Blob getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(Blob pdfFile) {
        this.pdfFile = pdfFile;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", enseignantId=" + enseignantId +
                ", idChapitre=" + idChapitre +
                ", pdfFile=" + pdfFile +
                '}';
    }
}