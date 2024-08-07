package Services;

import Entities.ChoixPossible;
import Entities.Reponse;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReponseService{
    private final Connection con = DataSource.getInstance().getCon();

    public void ajouter(Reponse reponse) throws SQLException {
        String req = "INSERT INTO reponse (questionId, correct, choixPossibleId) VALUES (?, ?, ?)";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, reponse.getChoixPossible().getQuestion().getId());
            ps.setBoolean(2, reponse.isCorrect());
            ps.setInt(3, reponse.getChoixPossible().getId());
            ps.executeUpdate();
        }
    }

    public void supprimer(Reponse reponse) throws SQLException {
        String req = "DELETE FROM reponse WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, reponse.getId());
        ps.executeUpdate();
    }

    public void update(Reponse reponse) throws SQLException {
        String req = "UPDATE reponse SET idQuestion = ?, correct = ? WHERE id = ?";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ps.setBoolean(2, reponse.isCorrect());
            ps.setInt(3, reponse.getId());
            ps.executeUpdate();
        }
    }

    public Reponse findById(int id) throws SQLException {
        String req = "SELECT * FROM reponse WHERE id = ?";
        PreparedStatement ps = con.prepareStatement(req);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return new Reponse(rs.getInt("id"), null, rs.getBoolean("correct"));
        }
        return null;
    }

    public List<Reponse> readAll() throws SQLException {
        List<Reponse> list = new ArrayList<>();
        String req = "SELECT * FROM reponse";
        PreparedStatement ps = con.prepareStatement(req);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new Reponse(rs.getInt("id"), null, rs.getBoolean("correct")));
        }
        return list;
    }

    public boolean isCorrect(int choixPossibleId) throws SQLException {
        String req = "SELECT correct FROM reponse WHERE choixPossibleId = ?";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, choixPossibleId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                System.out.println("No row with choixPossibleId: " + choixPossibleId);
                return false;
            }
            boolean correct = rs.getBoolean("correct");
            System.out.println("correct for choixPossibleId " + choixPossibleId + ": " + correct);
            return correct;
        }
    }

    public List<Reponse> findAllByQuestionId(int questionId) throws SQLException {
        List<Reponse> list = new ArrayList<>();
        String req = "SELECT reponse.*, choixpossible.description FROM reponse JOIN choixpossible ON reponse.choixPossibleId = choixpossible.id WHERE reponse.questionId = ?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, questionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChoixPossible choixPossible = new ChoixPossible(rs.getInt("choixPossibleId"), null, rs.getString("description"));
                list.add(new Reponse(rs.getInt("id"), choixPossible, rs.getBoolean("correct")));
            }
        }
        return list;
    }

    public Reponse findByChoixPossibleIdAndQuestionId(int choixPossibleId, int questionId) throws SQLException {
        String req = "SELECT * FROM reponse WHERE choixPossibleId = ? AND questionId = ?";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, choixPossibleId);
            ps.setInt(2, questionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Reponse(rs.getInt("id"), rs.getBoolean("correct"));
            }
            return null;
        }
    }
}