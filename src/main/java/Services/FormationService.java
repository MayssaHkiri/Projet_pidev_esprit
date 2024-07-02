package Services;

import Entities.Formation;
import Utils.DataSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FormationService {
    private Connection cnx = DataSource.getInstance().getCon();

    public FormationService() {
        // Initialisation de la connexion
    }

    public boolean addFormation(Formation formation) throws SQLException {
        String req = "INSERT INTO formation (titre, description, imageFormation, dateFormation) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, formation.getTitre());
            pst.setString(2, formation.getDescription());
            pst.setBlob(3, formation.getImageFormation());
            pst.setDate(4, new java.sql.Date(formation.getDateFormation().getTime())); // Convert Date to java.sql.Date

            int result = pst.executeUpdate();
            return result > 0;
        }
    }

    public boolean supprimerFormation(int id) throws SQLException {
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
                            rs.getDate("dateFormation")
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
                        rs.getDate("dateFormation")
                );

                formations.add(formation);
            }
        }

        return formations;
    }

    public List<Formation> searchFormations(String searchTerm) throws SQLException {
        List<Formation> formations = new ArrayList<>();
        String req = "SELECT * FROM formation WHERE titre LIKE ?";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, "%" + searchTerm + "%");

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Formation formation = new Formation(
                            rs.getInt("id"),
                            rs.getString("titre"),
                            rs.getString("description"),
                            rs.getBlob("imageFormation"),
                            rs.getDate("dateFormation")
                    );
                    formations.add(formation);
                }
            }
        }

        return formations;
    }

    public boolean modifierFormation(Formation formation) throws SQLException {
        String req = "UPDATE formation SET titre = ?, description = ?, imageFormation = ?, dateFormation = ? WHERE id = ?";

        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, formation.getTitre());
            pst.setString(2, formation.getDescription());
            pst.setBlob(3, formation.getImageFormation());
            pst.setDate(4, new java.sql.Date(formation.getDateFormation().getTime())); // Convert Date to java.sql.Date
            pst.setInt(5, formation.getId());

            int result = pst.executeUpdate();
            return result > 0;
        }
    }

    private Blob createBlob(InputStream inputStream) throws SQLException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, bytesRead);
        }
        return new javax.sql.rowset.serial.SerialBlob(baos.toByteArray());
    }
}