package Services;

import Entities.ChoixPossible;
import Entities.Reponse;
import Utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ChoixPossibleService {
    private final Connection con = DataSource.getInstance().getCon();

    public int ajouter(ChoixPossible choixPossible) throws SQLException {
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

    public void updateDescription(ChoixPossible choixToModify) throws SQLException {
        String req = "UPDATE choixpossible SET description = ? WHERE id = ?";
        try (Connection con = DataSource.getInstance().getCon();
             PreparedStatement ps = con.prepareStatement(req)) {
            ps.setString(1, choixToModify.getDescription());
            ps.setInt(2, choixToModify.getId());
            ps.executeUpdate();
        }
    }

}