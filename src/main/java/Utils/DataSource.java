package Utils;

import Entities.Formation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSource {
    private String url = "jdbc:mysql://localhost:3306/pi_dev_db";
    private String login = "root";
    private String pwd = "";
    private static DataSource instance;

    private Connection con;

    private DataSource() {
        try {
            con = DriverManager.getConnection(url, login, pwd);
            System.out.println("Connexion établie avec la base de données");
        } catch (SQLException e) {
            System.out.println("Erreur de connexion : " + e.getMessage());
        }
    }

    public static DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
    }

    public List<Formation> getAllFormations() {
        List<Formation> formations = new ArrayList<>();
        String query = "SELECT * FROM formation";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titre = rs.getString("titre");
                String description = rs.getString("description");
                String imageUrl = rs.getString("imageUrl");
                int idEnseignant = rs.getInt("idEnseignant");

                Formation formation = new Formation(id, titre, description, imageUrl, idEnseignant);
                formations.add(formation);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des formations : " + e.getMessage());
        }

        return formations;
    }

    public void closeConnection() {
        try {
            if (con != null) {
                con.close();
                System.out.println("Connexion à la base de données fermée");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
        }
    }

    public Connection getCon() {
        return con;
    }
}
