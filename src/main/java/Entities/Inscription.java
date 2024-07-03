package Entities;

public class Inscription {
    private String nom;
    private String prenom;
    private String email;
    private String numTel;

    public Inscription(String nom, String prenom, String email, String numTel) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numTel = numTel;
    }

    // Getters and setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }
}
