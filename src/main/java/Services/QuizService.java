package Services;

import Entities.ChoixPossible;
import Entities.Matiere;
import Entities.Question;
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
    public void deleteReponseByChoixPossibleId(int choixPossibleId) throws SQLException {
        String req = "DELETE FROM reponse WHERE choixPossibleId = ?";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, choixPossibleId);
            ps.executeUpdate();
        }
    }

    public void deleteChoixPossibleByQuestionId(int questionId) throws SQLException {
        // First, delete the dependent rows in the reponse table
        List<ChoixPossible> choixPossibles = findAllChoixPossibleByQuestionId(questionId);
        for (ChoixPossible choixPossible : choixPossibles) {
            deleteReponseByChoixPossibleId(choixPossible.getId());
        }

        // Then, delete the choixpossible
        String req = "DELETE FROM choixpossible WHERE questionId = ?";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, questionId);
            ps.executeUpdate();
        }
    }

    public void deleteQuestionByQuizId(int quizId) throws SQLException {
        // First, delete the dependent rows in the choixpossible table
        List<Question> questions = findAllQuestionsByQuizId(quizId);
        for (Question question : questions) {
            deleteChoixPossibleByQuestionId(question.getId());
        }

        // Then, delete the question
        String req = "DELETE FROM question WHERE quizId = ?";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, quizId);
            ps.executeUpdate();
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
        String req = "SELECT quiz.*, matiere.nom AS matiereName FROM quiz JOIN matiere ON quiz.matiereId = matiere.id";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Matiere matiere = new Matiere(rs.getString("matiereName"));
                Quiz quiz = new Quiz(rs.getInt("id"), rs.getString("titre"), rs.getString("description"), rs.getDate("dateCreation"));
                quiz.setMatiere(matiere);
                list.add(quiz);
            }

            return list;
        }
    }

    public void updateTitre(Quiz quiz, String newTitre) throws SQLException {
        String req = "UPDATE quiz SET titre = ? WHERE id = ?";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ps.setString(1, newTitre);
            ps.setInt(2, quiz.getId());
            ps.executeUpdate();
        }
    }

    public void updateDescription(Quiz quiz, String newDescription) throws SQLException {
        String req = "UPDATE quiz SET description = ? WHERE id = ?";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ps.setString(1, newDescription);
            ps.setInt(2, quiz.getId());
            ps.executeUpdate();
        }
    }

    public void updateMatiere(Quiz quiz, Matiere newMatiere) throws SQLException {
        String req = "UPDATE quiz SET matiereId = ? WHERE id = ?";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, newMatiere.getId());
            ps.setInt(2, quiz.getId());
            ps.executeUpdate();
        }
    }
    public List<Question> findAllQuestionsByQuizId(int quizId) throws SQLException {
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
    public List<ChoixPossible> findAllChoixPossibleByQuestionId(int questionId) throws SQLException {
        List<ChoixPossible> list = new ArrayList<>();
        String req = "SELECT * FROM choixpossible WHERE questionId = ?";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, questionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new ChoixPossible(rs.getInt("id"), null, rs.getString("description")));
            }
        }
        return list;
    }
}