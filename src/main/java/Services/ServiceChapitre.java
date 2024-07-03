package Services;

import Entities.Chapitre;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceChapitre implements IService<Chapitre> {
    private Connection con1 = DataSource.getInstance().getCon();
    private Statement ste;

    @Override
    public void ajouter(Chapitre chapitre) throws SQLException {
        String query = "INSERT INTO chapitre (titre, descrip, idMatiere) VALUES (?, ?, ?)";
        PreparedStatement pstmt = con1.prepareStatement(query);
        pstmt.setString(1, chapitre.getChapitreTitre());
        pstmt.setString(2, chapitre.getChapitreDescription());
        pstmt.setInt(3, chapitre.getIdMatiere());
        pstmt.executeUpdate();
    }

    @Override
    public void supprimer(Chapitre chapitre) throws SQLException {
        String query = "DELETE FROM chapitre WHERE id = ?";
        PreparedStatement pstmt = con1.prepareStatement(query);
        pstmt.setInt(1, chapitre.getId());
        pstmt.executeUpdate();
    }

    @Override
    public void update(Chapitre chapitre) throws SQLException {
        String query = "UPDATE chapitre SET titre = ?, descrip= ?, idMatiere = ? WHERE id = ?";
        PreparedStatement pstmt = con1.prepareStatement(query);
        pstmt.setString(1, chapitre.getChapitreTitre());
        pstmt.setString(2, chapitre.getChapitreDescription());
        pstmt.setInt(3, chapitre.getIdMatiere());
        pstmt.setInt(4, chapitre.getId());
        pstmt.executeUpdate();
    }

    @Override
    public Chapitre findbyId(int id) throws SQLException {
        String query = "SELECT * FROM chapitre WHERE id = ?";
        PreparedStatement pstmt = con1.prepareStatement(query);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return new Chapitre(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("descrip"),
                    rs.getInt("idMatiere")
            );
        }
        return null;
    }

    @Override
    public List<Chapitre> readAll() throws SQLException {
        List<Chapitre> list = new ArrayList<>();
        String query = "SELECT * FROM chapitre";
        ste = con1.createStatement();
        ResultSet rs = ste.executeQuery(query);

        while (rs.next()) {
            Chapitre chapitre = new Chapitre(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("descrip"),
                    rs.getInt("idMatiere")
            );
            list.add(chapitre);
        }
        return list;
    }

    public List<Chapitre> readByMatiere(int matiereId) throws SQLException {
        List<Chapitre> list = new ArrayList<>();
        String query = "SELECT * FROM chapitre WHERE idMatiere = ?";
        PreparedStatement pstmt = con1.prepareStatement(query);
        pstmt.setInt(1, matiereId);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Chapitre chapitre = new Chapitre(
                    rs.getInt("id"),
                    rs.getString("titre"),
                    rs.getString("descrip"),
                    rs.getInt("idMatiere")
            );
            list.add(chapitre);
        }
        return list;
    }
}
