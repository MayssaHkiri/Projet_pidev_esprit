package Entities;

public class Offre {
    private int idOffre;
    private String titreOffre;
    private String descriptionOffre;
    private String niveauEtude;
    private String dureeContrat;
    private String datePublication;
    private String entreprise;
    private String dateLimite;
    private String email;

    public Offre(int idOffre, String titreOffre, String descriptionOffre, String niveauEtude, String dureeContrat, String datePublication, String entreprise, String dateLimite, String email) {
        this.idOffre = idOffre;
        this.titreOffre = titreOffre;
        this.descriptionOffre = descriptionOffre;
        this.niveauEtude = niveauEtude;
        this.dureeContrat = dureeContrat;
        this.datePublication = datePublication;
        this.entreprise = entreprise;
        this.dateLimite = dateLimite;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdOffre() {
        return idOffre;
    }

    public void setIdOffre(int idOffre) {
        this.idOffre = idOffre;
    }

    public String getTitreOffre() {
        return titreOffre;
    }

    public void setTitreOffre(String titreOffre) {
        this.titreOffre = titreOffre;
    }

    public String getDescriptionOffre() {
        return descriptionOffre;
    }

    public void setDescriptionOffre(String descriptionOffre) {
        this.descriptionOffre = descriptionOffre;
    }

    public String getNiveauEtude() {
        return niveauEtude;
    }

    public void setNiveauEtude(String niveauEtude) {
        this.niveauEtude = niveauEtude;
    }

    public String getDureeContrat() {
        return dureeContrat;
    }

    public void setDureeContrat(String dureeContrat) {
        this.dureeContrat = dureeContrat;
    }

    public String getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(String datePublication) {
        this.datePublication = datePublication;
    }

    public String getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(String entreprise) {
        this.entreprise = entreprise;
    }

    public String getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(String dateLimite) {
        this.dateLimite = dateLimite;
    }

    @Override
    public String toString() {
        return "Offre{" +
                "idOffre=" + idOffre +
                ", titreOffre='" + titreOffre + '\'' +
                ", descriptionOffre='" + descriptionOffre + '\'' +
                ", niveauEtude='" + niveauEtude + '\'' +
                ", dureeContrat='" + dureeContrat + '\'' +
                ", datePublication=" + datePublication +
                ", entreprise='" + entreprise + '\'' +
                ", dateLimite=" + dateLimite +
                ", email=" + email +
                '}';
    }

}
