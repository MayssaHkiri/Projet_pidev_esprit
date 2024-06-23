package Services;

import Entities.ChoixPossible;
import Utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ChoixPossibleService {
    private final Connection con = DataSource.getInstance().getCon();

    public int ajouter(ChoixPossible choixPossible) throws SQLException {
        System.out.println(choixPossible.getQuestion().getId());
        String req = "INSERT INTO choixpossible (questionId, description) VALUES (?, ?)";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, choixPossible.getQuestion().getId());
            ps.setString(2, choixPossible.getDescription());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating choixpossible failed, no ID obtained.");
                }
            }
        }
    }
}