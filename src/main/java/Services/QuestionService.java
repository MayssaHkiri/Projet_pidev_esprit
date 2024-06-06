package Services;

import Contracts.ServiceInterface;
import Entities.Question;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionService implements ServiceInterface<Question> {
    private final Connection con = DataSource.getInstance().getCon();

    @Override
    public void ajouter(Question question) throws SQLException {
        String req = "INSERT INTO question (idQuiz, enonce) VALUES (?, ?)";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, question.getQuiz().getId());
        ps.setString(2, question.getEnonce());
        ps.executeUpdate();
    }

    @Override
    public void supprimer(Question question) throws SQLException {
        String req = "DELETE FROM question WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, question.getId());
        ps.executeUpdate();
    }

    @Override
    public void update(Question question) throws SQLException {
        String req = "UPDATE question SET idQuiz = ?, enonce = ? WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, question.getQuiz().getId());
        ps.setString(2, question.getEnonce());
        ps.setInt(3, question.getId());
        ps.executeUpdate();
    }

    @Override
    public Question findById(int id) throws SQLException {
        String req = "SELECT * FROM question WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Question(rs.getInt("id"), null, rs.getString("enonce"), null);
        }
        return null;
    }

    @Override
    public List<Question> readAll() throws SQLException {
        List<Question> list = new ArrayList<>();
        String req = "SELECT * FROM question";
        PreparedStatement ps = con.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new Question(rs.getInt("id"), null, rs.getString("enonce"), null));
        }
        return list;
    }
}