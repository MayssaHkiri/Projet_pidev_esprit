package Services;

import Contracts.ServiceInterface;
import Entities.Quiz;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizService implements ServiceInterface<Quiz> {
    private final Connection con = DataSource.getInstance().getCon();

    @Override
    public void ajouter(Quiz quiz) throws SQLException {
        String req = "INSERT INTO quiz (description, titre, dateCr) VALUES (?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setString(1, quiz.getDescription());
        ps.setString(2, quiz.getTitre());
        ps.setDate(3, new java.sql.Date(quiz.getDateCr().getTime()));
        ps.executeUpdate();
    }

    @Override
    public void supprimer(Quiz quiz) throws SQLException {
        String req = "DELETE FROM quiz WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, quiz.getId());
        ps.executeUpdate();
    }

    @Override
    public void update(Quiz quiz) throws SQLException {
        String req = "UPDATE quiz SET description = ?, titre = ?, dateCr = ? WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setString(1, quiz.getDescription());
        ps.setString(2, quiz.getTitre());
        ps.setDate(3, new java.sql.Date(quiz.getDateCr().getTime()));
        ps.setInt(4, quiz.getId());
        ps.executeUpdate();
    }

    @Override
    public Quiz findById(int id) throws SQLException {
        String req = "SELECT * FROM quiz WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Quiz(rs.getInt("id"), rs.getString("description"), rs.getString("titre"), rs.getDate("dateCr"), null);
        }
        return null;
    }

    @Override
    public List<Quiz> readAll() throws SQLException {
        List<Quiz> list = new ArrayList<>();
        String req = "SELECT * FROM quiz";
        PreparedStatement ps = con.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new Quiz(rs.getInt("id"), rs.getString("description"), rs.getString("titre"), rs.getDate("dateCr"), null));
        }
        return list;
    }
}