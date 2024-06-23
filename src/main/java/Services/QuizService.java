package Services;

import Entities.Quiz;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizService {
    private final Connection con = DataSource.getInstance().getCon();

    public int ajouter(Quiz quiz) throws SQLException {
        String req = "INSERT INTO quiz (description, titre, dateCreation, matiereId) VALUES (?, ?, ?, ?)";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, quiz.getDescription());
            ps.setString(2, quiz.getTitre());
            ps.setDate(3, new java.sql.Date(quiz.getDateCreation().getTime()));
            ps.setObject(4, quiz.getMatiere().getId());
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


    public void supprimer(Quiz quiz) throws SQLException {
        try (Connection con = DataSource.getInstance().getCon()) {
            // Start a transaction
            con.setAutoCommit(false);

            try {
                String req = "DELETE FROM reponse WHERE questionId IN (SELECT id FROM question WHERE quizId = ?)";
                try (PreparedStatement ps = con.prepareStatement(req)) {
                    ps.setInt(1, quiz.getId());
                    ps.executeUpdate();
                }

                String req2 = "DELETE FROM choixpossible WHERE questionId IN (SELECT id FROM question WHERE quizId = ?)";
                try (PreparedStatement ps2 = con.prepareStatement(req2)) {
                    ps2.setInt(1, quiz.getId());
                    ps2.executeUpdate();
                }

                String req3 = "DELETE FROM question WHERE quizId = ?";
                try (PreparedStatement ps3 = con.prepareStatement(req3)) {
                    ps3.setInt(1, quiz.getId());
                    ps3.executeUpdate();
                }

                String req4 = "DELETE FROM quiz WHERE id = ?";
                try (PreparedStatement ps4 = con.prepareStatement(req4)) {
                    ps4.setInt(1, quiz.getId());
                    ps4.executeUpdate();
                }

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }


    public void update(Quiz quiz) throws SQLException {
        String req = "UPDATE quiz SET description = ?, titre = ?, dateCreation = ? WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setString(1, quiz.getDescription());
        ps.setString(2, quiz.getTitre());
        ps.setDate(3, new java.sql.Date(quiz.getDateCreation().getTime()));
        ps.setInt(4, quiz.getId());
        ps.executeUpdate();
    }


    public Quiz findById(int id) throws SQLException {
        String req = "SELECT * FROM quiz WHERE id = ?";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Quiz(rs.getInt("id"), rs.getString("description"), rs.getString("titre"), rs.getDate("dateCreation"), null);
            }
        }
        return null;
    }


    public List<Quiz> readAll() throws SQLException {
        List<Quiz> list = new ArrayList<>();
        String req = "SELECT * FROM quiz";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Quiz(rs.getInt("id"), rs.getString("description"), rs.getString("titre"), rs.getDate("dateCreation"), null));
            }

            return list;
        }
    }
}