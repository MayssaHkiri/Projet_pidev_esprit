package Services;
import Entities.Formation;
import Utils.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FormationService implements Iservice<Formation> {
    private Connection cnx = DataSource.getInstance().getCon();
    private Statement ste;

    public FormationService() {
        try {
            ste = cnx.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public ArrayList<Formation> getAll() throws SQLException {
        ArrayList<Formation> formations = new ArrayList<>();
        String req = "SELECT id, titre, descrip, idEnseignant FROM formation";

        try (ResultSet res = ste.executeQuery(req)) {
            while (res.next()) {
                Formation formation = new Formation(
                        res.getString("titre"),
                        res.getString("descrip"),
                        res.getInt("idEnseignant")
                );
                formations.add(formation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return formations;
    }

    @Override
    public boolean add(Formation f) throws SQLException {
        String req = "INSERT INTO formation (titre, descrip, idEnseignant) VALUES (?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, f.getTitre());
            pst.setString(2, f.getDescription());
            pst.setInt(3, f.getIdEnseignant());
            int result = pst.executeUpdate();
            if (result > 0) {
                System.out.println("Ajout réussi");
                return true;
            } else {
                System.out.println("Échec de l'ajout");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String req = "DELETE FROM formation WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, id);
            int deletedRows = pst.executeUpdate();
            return deletedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Formation f) throws SQLException {
        String req = "UPDATE formation SET titre = ?, descrip = ?, idEnseignant = ? WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, f.getTitre());
            pst.setString(2, f.getDescription());
            pst.setInt(3, f.getIdEnseignant());
            pst.setInt(4, f.getId());
            int updatedRows = pst.executeUpdate();
            return updatedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Formation> search(String searchTerm) throws SQLException {
        List<Formation> formations = new ArrayList<>();
        String req = "SELECT * FROM formation WHERE titre LIKE ? OR descrip LIKE ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, "%" + searchTerm + "%");
            pst.setString(2, "%" + searchTerm + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Formation formation = new Formation(
                            rs.getString("titre"),
                            rs.getString("descrip"),
                            rs.getInt("idEnseignant")
                    );
                    formations.add(formation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return formations;
    }
    public Formation getFormationById(int idFormation) throws SQLException {
        String req = "SELECT id, titre, descrip, idEnseignant FROM formation WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, idFormation);
            try (ResultSet res = pst.executeQuery()) {
                if (res.next()) {
                    return new Formation(
                            res.getInt("id"),
                            res.getString("titre"),
                            res.getString("descrip"),
                            res.getInt("idEnseignant")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
