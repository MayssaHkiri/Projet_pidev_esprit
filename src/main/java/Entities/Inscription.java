package Entities;
import java.sql.Timestamp;

public class Inscription {
    private int id;
    private int idFormation;
    private int idEtudiant;
    private Timestamp dateD;
    private Timestamp dateF;

    // Constructeurs, getters et setters
    public Inscription(int id, int idFormation, int idEtudiant, Timestamp dateD, Timestamp dateF) {
        this.id = id;
        this.idFormation = idFormation;
        this.idEtudiant = idEtudiant;
        this.dateD = dateD;
        this.dateF = dateF;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFormation() {
        return idFormation;
    }

    public void setIdFormation(int idFormation) {
        this.idFormation = idFormation;
    }

    public int getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(int idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    public Timestamp getDateD() {
        return dateD;
    }

    public void setDateD(Timestamp dateD) {
        this.dateD = dateD;
    }

    public Timestamp getDateF() {
        return dateF;
    }

    public void setDateF(Timestamp dateF) {
        this.dateF = dateF;
    }
}
