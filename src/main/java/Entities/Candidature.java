package Entities;

public class Candidature {
    private int idCandidature;
    private String datePostulation;
    private String statutCandidature;

    public Candidature(int idCandidature, String datePostulation, String statutCandidature) {
        this.idCandidature = idCandidature;
        this.datePostulation = datePostulation;
        this.statutCandidature = statutCandidature;
    }

    public int getIdCandidature() {
        return idCandidature;
    }

    public void setIdCandidature(int idCandidature) {
        this.idCandidature = idCandidature;
    }

    public String getDatePostulation() {
        return datePostulation;
    }

    public void setDatePostulation(String datePostulation) {
        this.datePostulation = datePostulation;
    }

    public String getStatutCandidature() {
        return statutCandidature;
    }

    public void setStatutCandidature(String statutCandidature) {
        this.statutCandidature = statutCandidature;
    }

    @Override
    public String toString() {
        return "Candidature{" +
                "idCandidature=" + idCandidature +
                ", datePostulation=" + datePostulation +
                ", statutCandidature='" + statutCandidature + '\'' +
                '}';
    }
}
