package Services;

import Entities.Cours;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCours implements IService<Cours> {

    private Connection con1 = DataSource.getInstance().getCon();
    private Statement ste;

    public ServiceCours() {
        try {
            ste = con1.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public void ajouter(Cours cours) throws SQLException {
        String req = "INSERT INTO support (titre, description, enseignantId, idChapitre, pdfFile) VALUES    ( '" +
                cours.getTitre() + "', '" + cours.getDescription() + "', '" + cours.getEnseignantId() + "', '" + cours.getIdChapitre() + "', '" + cours.getPdfFile() + "');";


        ste.executeUpdate(req);
    }

    @Override
     public void supprimer(Cours cours) throws SQLException {
            String req = "DELETE FROM `Cours` WHERE `CoursId` = " + cours.getId();
            ste.executeUpdate(req);
            ste.close(); // Close the Statement
            con1.close(); // Close the Connection
        }


    @Override
    public void update(Cours cours) throws SQLException {
        System.out.println(cours+"++++++++++++");
            String req = "UPDATE `Cours` SET `titre` = '" + cours.getTitre() + "', `description` = '" + cours.getDescription()  + "', `enseignantId` = '" + cours.getEnseignantId()  + "', '" + cours.getPdfFile() + "' WHERE `id` = " + cours.getId(); // Assuming id is the primary key of your Cours table
            ste.executeUpdate(req);
            ste.close(); // Close the Statement
            con1.close(); // Close the Connection



    }

    @Override
    public Cours findbyId(int e) throws SQLException {
        return null;
    }

    public List<Cours> readAll() throws SQLException {
        List<Cours> cours = new ArrayList<>();
        String sql = "SELECT * FROM support";
        try (PreparedStatement ps = con1.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String titre = rs.getString("titre");
                String description = rs.getString("description");
                int enseignantId = rs.getInt("enseignantId");
                int idChapitre = rs.getInt("idChapitre");
                Blob pdfFile = rs.getBlob("pdfFile");
                cours.add(new Cours(titre, description, enseignantId, idChapitre, pdfFile));
            }
        }
        return cours;
    }
}
