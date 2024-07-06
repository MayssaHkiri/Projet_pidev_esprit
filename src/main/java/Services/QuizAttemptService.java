package Services;

import Entities.QuizAttempt;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizAttemptService {

    private final Connection con = DataSource.getInstance().getCon();

    public void ajouter(QuizAttempt quizAttempt) throws SQLException {
        String req = "INSERT INTO quiz_attempt (etudiantId, quizId, questionIds, reponseIds, score, dateAttempt) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, quizAttempt.getEtudiantId());
        ps.setInt(2, quizAttempt.getQuizId());
        // You need to convert the list of questionIds and reponseIds to a format that can be stored in your database
        ps.setString(3, quizAttempt.getQuestionIds().toString());
        ps.setString(4, quizAttempt.getReponseIds().toString());
        ps.setInt(5, quizAttempt.getScore());
        ps.setTimestamp(6, new Timestamp(quizAttempt.getDateAttempt().getTime()));
        ps.executeUpdate();
    }

    public void update(QuizAttempt quizAttempt) throws SQLException {
        String req = "UPDATE quiz_attempt SET etudiantId=?, quizId=?, questionIds=?, reponseIds=?, score=?, dateAttempt=? WHERE id=?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, quizAttempt.getEtudiantId());
        ps.setInt(2, quizAttempt.getQuizId());
        ps.setString(3, quizAttempt.getQuestionIds().toString());
        ps.setString(4, quizAttempt.getReponseIds().toString());
        ps.setInt(5, quizAttempt.getScore());
        ps.setTimestamp(6, new Timestamp(quizAttempt.getDateAttempt().getTime()));
        ps.setInt(7, quizAttempt.getId());
        ps.executeUpdate();
    }

    public void supprimer(QuizAttempt quizAttempt) throws SQLException {
        String req = "DELETE FROM quiz_attempt WHERE id=?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, quizAttempt.getId());
        ps.executeUpdate();
    }

    public List<QuizAttempt> readAll() throws SQLException {
        List<QuizAttempt> list = new ArrayList<>();
        String req = "SELECT * FROM quiz_attempt";
        PreparedStatement ps = con.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            // You need to convert the stored questionIds and reponseIds back to a list
            List<Integer> questionIds = convertStringToList(rs.getString("questionIds"));
            List<Integer> reponseIds = convertStringToList(rs.getString("reponseIds"));
            list.add(new QuizAttempt(rs.getInt("id"), rs.getInt("etudiantId"), rs.getInt("quizId"), questionIds, reponseIds, rs.getInt("score"), rs.getTimestamp("dateAttempt")));
        }
        return list;
    }

    private List<Integer> convertStringToList(String str) {
        List<Integer> list = new ArrayList<>();
        String[] numbers = str.split(",");
        for (String number : numbers) {
            list.add(Integer.parseInt(number.trim()));
        }
        return list;
    }
}