package Services;

import Entities.Matiere;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatiereService {
    private Connection con;

    public MatiereService() {
        con = DataSource.getInstance().getCon();
    }

    public List<Matiere> getAllMatieres() {
        List<Matiere> matieres = new ArrayList<>();
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM matiere")) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                matieres.add(new Matiere(id, nom));
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching matieres: " + e.getMessage());
        }
        return matieres;
    }

    public Matiere getMatiereByName(String matiereNom) throws SQLException {
        System.out.println("Fetching matiere with name: " + matiereNom);
        con = DataSource.getInstance().getCon(); // Ensure to use the current connection
        if (con == null || con.isClosed()) {
            throw new SQLException("Connection is closed");
        }
        Matiere matiere = null;
        try (PreparedStatement pstmt = con.prepareStatement("SELECT * FROM matiere WHERE nom = ?")) {
            pstmt.setString(1, matiereNom);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String nom = rs.getString("nom");
                    matiere = new Matiere(id, nom);
                    System.out.println("Fetched matiere: " + matiere);
                } else {
                    System.out.println("No matiere found with name: " + matiereNom);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while fetching matiere: " + e.getMessage());
        }
        return matiere;
    }
}
