package Services;

import Entities.Chapitre;
import Entities.Cours;
import Utils.DataSource;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

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
    @FXML
    private ChoiceBox<Chapitre> chapitreChoiceBox;

    @Override
    public void ajouter(Cours cours) throws SQLException {
        // Assuming chapitreId is set in the Cours object, otherwise retrieve it using appropriate logic
        int chapitreId = cours.getIdChapitre();
        int enseignatId= cours.getEnseignantId();

        System.out.println("id="+cours.getIdChapitre());


        String req = "INSERT INTO support (titre, description, enseignantId, idChapitre, pdfFile) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con1.prepareStatement(req)) {
            ps.setString(1, cours.getTitre());
            ps.setString(2, cours.getDescription());
            ps.setInt(3, enseignatId);
            ps.setInt(4, chapitreId);
            ps.setBlob(5, cours.getPdfFile());
            ps.executeUpdate();
        }
    }


    @Override
    public void supprimer(Cours cours) throws SQLException {
        String req = "DELETE FROM support WHERE id = " + cours.getId() + ";";
        ste.executeUpdate(req);
    }

    @Override
    public void update(Cours cours) throws SQLException {
        int id = cours.getIdChapitre();
        String req = "UPDATE support SET titre = '" + cours.getTitre() +
                "', description = '" + cours.getDescription() +
                "', enseignantId = 1, idChapitre = " + cours.getIdChapitre()+
                ", pdfFile = '" + cours.getPdfFile() +
                "' WHERE id = " + cours.getId() + ";";
        ste.executeUpdate(req);
    }
    private Connection con= DataSource.getInstance().getCon();

     public void UpdateCours(Cours cours) throws SQLException {
         String req = "UPDATE support SET titre = '" + cours.getTitre() +
                 "', description = '" + cours.getDescription() +
                 "', pdfFile = '" + cours.getPdfFile() +
                 "' WHERE id = " + cours.getId() + ";";
         ste.executeUpdate(req);
     }
    @Override
    public Cours findbyId(int idCours) throws SQLException {
        String query = "SELECT * FROM `support` WHERE `id` = ?";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, idCours);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Créez et retournez une instance d'Offre avec les données de la ligne
                    return mapOffreFromResultSet(rs);
                }
            }
        }
        return null;
    }
    private Cours mapOffreFromResultSet(ResultSet rs) throws SQLException {
        // Mapping des colonnes du ResultSet aux attributs de la classe Offre
        int id = rs.getInt("id");
        String titre = rs.getString("titre");
        String description = rs.getString("description");
        int enseignantId = 1; // Initialiser à 1
        int idChapitre = 9;   // Initialiser à 1
        Blob pdfFile = rs.getBlob("pdfFile");

        // Créez et retournez une instance de Offre avec les données récupérées
        return new Cours(id, titre, description, enseignantId, idChapitre, pdfFile);
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