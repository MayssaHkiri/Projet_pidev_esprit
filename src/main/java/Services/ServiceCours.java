package Services;

import Entities.Cours;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCours implements IserviceCours<Cours> {

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
        String req = "INSERT INTO support (titre, description, enseignantId, idChapitre, pdfFile) VALUES ('" +
                cours.getTitre() + "', '" + cours.getDescription() + "', 1, 1, '" + cours.getPdfFile() + "');";
        ste.executeUpdate(req);
    }

    @Override
    public void supprimer(Cours cours) throws SQLException {
        String req = "DELETE FROM support WHERE id = " + cours.getId() + ";";
        ste.executeUpdate(req);
    }

    @Override
    public void update(Cours cours) throws SQLException {
        String req = "UPDATE support SET titre = '" + cours.getTitre() +
                "', description = '" + cours.getDescription() +
                "', enseignantId = 1, idChapitre = 1, pdfFile = '" + cours.getPdfFile() +
                "' WHERE id = " + cours.getId() + ";";
        ste.executeUpdate(req);
    }

    @Override
    public Cours findbyId(int e) throws SQLException {
        return null;
    }

    @Override
    public List<Cours> readAll() throws SQLException {
        List<Cours> cours = new ArrayList<>();
        String sql = "SELECT * FROM support";

        ResultSet rs = ste.executeQuery(sql);

        while (rs.next()) {
            int id = rs.getInt("id");
            String titre = rs.getString("titre");
            String description = rs.getString("description");
            int enseignantId = 1; // Initialiser à 1
            int idChapitre = 1;   // Initialiser à 1
            Blob pdfFile = rs.getBlob("pdfFile");
            cours.add(new Cours(id ,titre, description, enseignantId, idChapitre, pdfFile));
        }

        return cours;
    }
}