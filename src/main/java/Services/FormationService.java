package Services;

import Entities.Formation;
import Utils.DataSource;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FormationService {
    private Connection cnx = DataSource.getInstance().getCon();

    public FormationService() {
        // Initialisation de la connexion
    }

    public boolean addFormation(Formation formation, int idEnseignant) throws SQLException {
        String req = "INSERT INTO formation (titre, description, imageFormation, idEnseignant, dateFormation) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, formation.getTitre());
            pst.setString(2, formation.getDescription());
            pst.setBlob(3, formation.getImageFormation());
            pst.setInt(4, idEnseignant);
            pst.setString(5, formation.getDateFormation());
            int result = pst.executeUpdate();
            System.out.println("Enseignant="+formation + idEnseignant);
            return result > 0;
        }
    }


    public boolean SupprimerFormation(int id) throws SQLException {
        String req = "DELETE FROM formation WHERE id = ?";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, id);

            int result = pst.executeUpdate();
            return result > 0;
        }
    }

    public Formation getFormationById(int id) throws SQLException {
        String req = "SELECT * FROM formation WHERE id = ?";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Formation(
                            rs.getInt("id"),
                            rs.getString("titre"),
                            rs.getString("description"),
                            rs.getBlob("imageFormation"),
                            rs.getString("dateFormation")
                    );
                }
            }
        }

        return null;
    }

    public List<Formation> getAllFormations() throws SQLException {
        List<Formation> formations = new ArrayList<>();
        String req = "SELECT * FROM formation";

        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(req)) {

            while (rs.next()) {
                Formation formation = new Formation(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getBlob("imageFormation"),
                        rs.getString("dateFormation")
                );

                formations.add(formation);
            }
        }

        return formations;
    }

    public List<Formation> searchFormations(String searchTerm) throws SQLException {
        List<Formation> formations = new ArrayList<>();
        String req = "SELECT * FROM formation WHERE titre LIKE ? OR description LIKE ?";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, "%" + searchTerm + "%");
            pst.setString(2, "%" + searchTerm + "%");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Formation formation = new Formation(
                            rs.getInt("id"),
                            rs.getString("titre"),
                            rs.getString("description"),
                            rs.getBlob("imageFormation"),
                            rs.getString("dateFormation")
                    );
                    formations.add(formation);
                }
            }
        }

        return formations;
    }

    public Formation getFormationByTitre(String titre) throws SQLException {
        String req = "SELECT * FROM formation WHERE titre = ?";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, titre);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Formation(
                            rs.getInt("id"),
                            rs.getString("titre"),
                            rs.getString("description"),
                            rs.getBlob("imageFormation"),
                            rs.getString("dateFormation")
                    );
                }
            }
        }

        return null;
    }

    public boolean ModifierFormation(Formation formation) throws SQLException {
        String req = "UPDATE formation SET titre = ?, description = ?, imageFormation = ?, dateFormation = ? WHERE id = ?";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, formation.getTitre());
            pst.setString(2, formation.getDescription());
            pst.setBlob(3, formation.getImageFormation());
            pst.setString(4, formation.getDateFormation());
            pst.setInt(5, formation.getId());

            int result = pst.executeUpdate();
            return result > 0;
        }
    }
}
