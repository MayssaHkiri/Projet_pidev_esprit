package Services;

import Contracts.ServiceInterface;
import Entities.Reponse;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseService implements ServiceInterface<Reponse> {
    private final Connection con = DataSource.getInstance().getCon();

    @Override
    public void ajouter(Reponse reponse) throws SQLException {
        String req = "INSERT INTO reponse (idQuestion, correct, Contenu) VALUES (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, reponse.getQuestion().getId());
        ps.setBoolean(2, reponse.isCorrect());
        ps.setString(3, reponse.getContenu());
        ps.executeUpdate();
    }

    @Override
    public void supprimer(Reponse reponse) throws SQLException {
        String req = "DELETE FROM reponse WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, reponse.getId());
        ps.executeUpdate();
    }

    @Override
    public void update(Reponse reponse) throws SQLException {
        String req = "UPDATE reponse SET idQuestion = ?, correct = ?, Contenu = ? WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, reponse.getQuestion().getId());
        ps.setBoolean(2, reponse.isCorrect());
        ps.setString(3, reponse.getContenu());
        ps.setInt(4, reponse.getId());
        ps.executeUpdate();
    }

    @Override
    public Reponse findById(int id) throws SQLException {
        String req = "SELECT * FROM reponse WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Reponse(rs.getInt("id"), null, rs.getBoolean("correct"), rs.getString("Contenu"));
        }
        return null;
    }

    @Override
    public List<Reponse> readAll() throws SQLException {
        List<Reponse> list = new ArrayList<>();
        String req = "SELECT * FROM reponse";
        PreparedStatement ps = con.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new Reponse(rs.getInt("id"), null, rs.getBoolean("correct"), rs.getString("Contenu")));
        }
        return list;
    }
}