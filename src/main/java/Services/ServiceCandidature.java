package Services;

import Entities.Candidature;
import Utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceCandidature implements IservicesOffres<Candidature> {
    private final DataSource dataSource;
    private Connection con ;


    public ServiceCandidature() {
        this.dataSource = DataSource.getInstance();
        this.con = dataSource.getCon();
    }


    @Override
    public void ajouter(Candidature candidature) throws SQLException {
        String query = "INSERT INTO `candidature` (`datePostulation`, `statutCandidature`) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(2, candidature.getDatePostulation());
            pstmt.setString(3, candidature.getStatutCandidature());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void supprimer(Candidature candidature ) throws SQLException {
        String query = "DELETE FROM `candidature` WHERE `idCandidature` = ?";
             Connection con = dataSource.getCon();
             PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setInt(1, candidature.getIdCandidature());
            pstmt.executeUpdate();

    }

    @Override
    public void supprimerAll() throws SQLException {
        String query = "DELETE FROM `candidature`";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Toutes les candidatures ont été supprimées avec succès.");
            } else {
                System.out.println("Aucune candidature trouvée à supprimer.");
            }
        }
    }

    @Override
    public Candidature findById(int id) throws SQLException {
        String query = "SELECT * FROM `candidature` WHERE `idCandidature` = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int idCandidature = rs.getInt("idCandidature");
                    String datePostulation = rs.getString("datePostulation");
                    String statutCandidature = rs.getString("statutCandidature");
                    return new Candidature(idCandidature, datePostulation, statutCandidature);
                }
            }
        }
        return null;
    }

    @Override
    public List<Candidature> findAll() throws SQLException {
        List<Candidature> candidatures = new ArrayList<>();
        String query = "SELECT * FROM `candidature`";
        try (PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int idCandidature = rs.getInt("idCandidature");
                String datePostulation = rs.getString("datePostulation");
                String statutCandidature = rs.getString("statutCandidature");
                candidatures.add(new Candidature(idCandidature, datePostulation, statutCandidature));
            }
        }
        return candidatures;
    }

    @Override
    public void update(Candidature candidature ) throws SQLException {
        String query = "UPDATE `candidature` SET `datePostulation` = ?, `statutCandidature` = ? WHERE `idCandidature` = ?";
        try (Connection con = dataSource.getCon();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, candidature.getDatePostulation());
            pstmt.setString(2, candidature.getStatutCandidature());
            pstmt.setInt(3, candidature.getIdCandidature());
            pstmt.executeUpdate();
        }
    }


}
