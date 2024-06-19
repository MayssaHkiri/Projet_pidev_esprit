package Services;

import Entities.Formation;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FormationService {
    private Connection cnx = DataSource.getInstance().getCon();
    private Statement ste;

    public FormationService() {
        try {
            ste = cnx.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public List<Formation> getAllFormations() throws SQLException {
        List<Formation> formations = new ArrayList<>();
        String req = "SELECT id, idEnseignant FROM formation";

        try (ResultSet res = ste.executeQuery(req)) {
            while (res.next()) {
                Formation formation = new Formation(
                        res.getInt("id"),
                        res.getInt("idEnseignant")
                );
                formations.add(formation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return formations;
    }

    public Formation getFormationById(int idFormation) throws SQLException {
        String req = "SELECT id, idEnseignant FROM formation WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, idFormation);
            try (ResultSet res = pst.executeQuery()) {
                if (res.next()) {
                    return new Formation(
                            res.getInt("id"),
                            res.getInt("idEnseignant")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean add(Formation formation) {
        return false;
    }
}
