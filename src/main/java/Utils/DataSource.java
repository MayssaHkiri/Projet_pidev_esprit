package Utils;

import Entities.Formation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSource {
    private final String url = "jdbc:mysql://localhost:3306/pi_dev_db";
    private final String login = "root";
    private final String pwd = "root"; // Mot de passe vide
    private static DataSource data;

    private Connection con;

    private DataSource() {
        try {
            con = DriverManager.getConnection(url, login, pwd);
            System.out.println("Connexion établie");
        } catch (SQLException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
        }
    }

    public Connection getCon() {
        return con;
    }

    public static DataSource getInstance() {
        if (data == null) {
            data = new DataSource();
        }
        return data;
    }

    public List<Formation> getAllFormations() {
        List<Formation> formations = new ArrayList<>();
        String query = "SELECT id, titre, description, image_url FROM formation";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Formation formation = new Formation();
                formation.setId(rs.getInt("id"));
                formation.setTitre(rs.getString("titre"));
                formation.setDescription(rs.getString("description"));
                formation.setImageFormation(rs.getBlob("imageFormation"));

                formations.add(formation);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des formations : " + e.getMessage());
        }

        return formations;
    }
}
