package Services;

import Entities.ChoixPossible;
import Entities.Question;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionService {
    private final Connection con = DataSource.getInstance().getCon();

    
    public int ajouter(Question question, int quizId) throws SQLException {
        String req = "INSERT INTO question (quizId, enonce) VALUES (?, ?)";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, quizId);
            ps.setString(2, question.getEnonce());
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating quiz failed, no ID obtained.");
                }
            }
        }
    }


    public void supprimer(Question question) throws SQLException {
        String req = "DELETE FROM question WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, question.getId());
        ps.executeUpdate();
    }

    public void deleteByQuizId(int quizId) throws SQLException {
        String req = "DELETE FROM question WHERE quizId = ?";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, quizId);
            ps.executeUpdate();
        }
    }
    public void update(Question question) throws SQLException {
        String req = "UPDATE question SET idQuiz = ?, enonce = ? WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, question.getQuiz().getId());
        ps.setString(2, question.getEnonce());
        ps.setInt(3, question.getId());
        ps.executeUpdate();
    }


    public Question findById(int id) throws SQLException {
        String req = "SELECT * FROM question WHERE id = ?";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Question(rs.getInt("id"), null, rs.getString("enonce"), null);
            }
            return null;
        }
    }
    public List<ChoixPossible> findAllChoixPossible(Question question) throws SQLException {
        List<ChoixPossible> list = new ArrayList<>();
        String req = "SELECT * FROM choixpossible WHERE questionId = ?";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, question.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ChoixPossible(rs.getInt("id"), question, rs.getString("description")));
            }
        }
        return list;
    }

    
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
    public List<Question> findAllByQuizId(int quizId) throws SQLException {
        List<Question> list = new ArrayList<>();
        String req = "SELECT * FROM question WHERE quizId = ?";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, quizId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Question(rs.getInt("id"), null, rs.getString("enonce"), null));
            }
        }
        return list;
    }
    public void updateEnonce(Question questionToModify, String newTitre) throws SQLException {
        String req = "UPDATE question SET enonce = ? WHERE id = ?";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ps.setString(1, newTitre);
            ps.setInt(2, questionToModify.getId());
            ps.executeUpdate();
        }
    }

}