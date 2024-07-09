
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
        String query = "INSERT INTO inscription (nom, prenom, email, numTel,idEtudiant,formation_id) VALUES (?, ?, ?, ?,1,2)";

        try (Connection con = dataSource.getCon();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, inscription.getNom());
            pst.setString(2, inscription.getPrenom());
            pst.setString(3, inscription.getEmail());
            pst.setString(4, inscription.getNumTel());

            int result = pst.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
