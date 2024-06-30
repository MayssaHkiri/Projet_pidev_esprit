package Services;

import Entities.Inscription;
import Utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InscriptionService {
    private DataSource dataSource;

    public InscriptionService() {
        this.dataSource = DataSource.getInstance();
    }

    public boolean add(Inscription inscription) {
        String query = "INSERT INTO inscriptions (idFormation, idEtudiant, dateD, dateF) VALUES (?, ?, ?, ?)";

        try (Connection con = dataSource.getCon();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setInt(1, inscription.getIdFormation());
            pst.setInt(2, inscription.getIdEtudiant());
            pst.setTimestamp(3, inscription.getDateD());
            pst.setTimestamp(4, inscription.getDateF());

            int result = pst.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
