package Services;

import Entities.Offre;
import Entities.User;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceOffre implements IservicesOffres<Offre>{

    private Connection con= DataSource.getInstance().getCon();
    private Statement ste;

    public ServiceOffre(){
        try {
            ste=con.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    @Override
    public void ajouter(Offre offre) throws SQLException {
        String query = "INSERT INTO `offre` (`titreOffre`, `descriptionOffre`, `niveauEtude`, `dureeContrat`, `datePublication`, `entreprise`, `dateLimite`, `email`) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, offre.getTitreOffre());
            pstmt.setString(2, offre.getDescriptionOffre());
            pstmt.setString(3, offre.getNiveauEtude());
            //pstmt.setString(4, offre.getDureeContrat());
            pstmt.setString(4, "3mois");
            pstmt.setString(5, offre.getDatePublication());
            pstmt.setString(6, offre.getEntreprise());
            pstmt.setString(7, offre.getDateLimite());
            pstmt.setString(8, offre.getEmail());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void supprimer(Offre offre) throws SQLException {
        String query = "DELETE FROM `offre` WHERE `titreOffre` = ? AND `datePublication` = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, offre.getTitreOffre());
            pstmt.setString(2, offre.getDatePublication());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Offre supprimée avec succès.");
            } else {
                System.out.println("Aucune offre trouvée avec les critères spécifiés.");
            }
        }
    }

    @Override
    public void supprimerAll() throws SQLException {
        String query = "DELETE FROM `offre`";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Toutes les offres ont été supprimées avec succès.");
            } else {
                System.out.println("Aucune offre trouvée à supprimer.");
            }
        }
    }

    @Override
    public Offre findById(int idOffre) throws SQLException {
        String query = "SELECT * FROM `offre` WHERE `idOffre` = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, idOffre);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Créez et retournez une instance d'Offre avec les données de la ligne
                    return mapOffreFromResultSet(rs);
                }
            }
        }
        return null;
    }

    private Offre mapOffreFromResultSet(ResultSet rs) throws SQLException {
        // Mapping des colonnes du ResultSet aux attributs de la classe Offre
        int id = rs.getInt("idOffre");
        String titreOffre = rs.getString("titreOffre");
        String descriptionOffre = rs.getString("descriptionOffre");
        String niveauEtude = rs.getString("niveauEtude");
        String dureeContrat = rs.getString("dureeContrat");
        String datePublication = rs.getString("datePublication");
        String entreprise = rs.getString("entreprise");
        String dateLimite = rs.getString("dateLimite");
        String email = rs.getString("email");

        // Créez et retournez une instance de Offre avec les données récupérées
        return new Offre(id, titreOffre, descriptionOffre, niveauEtude, dureeContrat, datePublication, entreprise, dateLimite, email);
    }


    @Override
    public List<Offre> findAll() throws SQLException {
        String query = "SELECT * FROM `offre`";
        List<Offre> offres = new ArrayList<>();

        try (PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                // Mapper les données du ResultSet à un objet Offre
                Offre offre = mapOffreFromResultSet(rs);
                offres.add(offre);

            }
        }

        return offres;
    }

    @Override
    public void update(Offre offre) throws SQLException {
        String query = "UPDATE `offre` SET `titreOffre` = ?, `descriptionOffre` = ?, `niveauEtude` = ?, `dureeContrat` = ?, `datePublication` = ?, `entreprise` = ?, `dateLimite` = ?, `email` = ? WHERE `idOffre` = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, offre.getTitreOffre());
            pstmt.setString(2, offre.getDescriptionOffre());
            pstmt.setString(3, offre.getNiveauEtude());
            pstmt.setString(4, offre.getDureeContrat());
            pstmt.setString(5, offre.getDatePublication());
            pstmt.setString(6, offre.getEntreprise());
            pstmt.setString(7, offre.getDateLimite());
            pstmt.setString(8, offre.getEmail());
            pstmt.setInt(9, offre.getIdOffre());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Offre mise à jour avec succès.");
            } else {
                System.out.println("Aucune offre trouvée avec l'ID spécifié.");
            }
        }
    }

}
