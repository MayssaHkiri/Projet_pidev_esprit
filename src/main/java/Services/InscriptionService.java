package Services;

import Entities.Inscription;
import Utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscriptionService implements Iservice<Inscription> {
    private Connection cnx = DataSource.getInstance().getCon();
    private Statement ste;

    public InscriptionService() {
        try {
            ste = cnx.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public ArrayList<Inscription> getAll() throws SQLException {
        ArrayList<Inscription> inscriptions = new ArrayList<>();
        String req = "SELECT id, idFormation, idEtudiant, dateD, dateF FROM inscription";

        try (ResultSet res = ste.executeQuery(req)) {
            while (res.next()) {
                Inscription inscription = new Inscription(
                        res.getInt("id"),
                        res.getInt("idFormation"),
                        res.getInt("idEtudiant"),
                        res.getTimestamp("dateD"),
                        res.getTimestamp("dateF")
                );
                inscriptions.add(inscription);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inscriptions;
    }

    @Override
    public boolean add(Inscription i) throws SQLException {
        String req = "INSERT INTO inscription (idFormation, idEtudiant, dateD, dateF) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, i.getIdFormation());
            pst.setInt(2, i.getIdEtudiant());
            pst.setTimestamp(3, i.getDateD());
            pst.setTimestamp(4, i.getDateF());
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
        String req = "DELETE FROM inscription WHERE id = ?";
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
    public boolean update(Inscription i) throws SQLException {
        String req = "UPDATE inscription SET idFormation = ?, idEtudiant = ?, dateD = ?, dateF = ? WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setInt(1, i.getIdFormation());
            pst.setInt(2, i.getIdEtudiant());
            pst.setTimestamp(3, i.getDateD());
            pst.setTimestamp(4, i.getDateF());
            pst.setInt(5, i.getId());
            int updatedRows = pst.executeUpdate();
            return updatedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Inscription> search(String searchTerm) throws SQLException {
        List<Inscription> inscriptions = new ArrayList<>();
        String req = "SELECT * FROM inscription WHERE idFormation LIKE ? OR idEtudiant LIKE ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, "%" + searchTerm + "%");
            pst.setString(2, "%" + searchTerm + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Inscription inscription = new Inscription(
                            rs.getInt("id"),
                            rs.getInt("idFormation"),
                            rs.getInt("idEtudiant"),
                            rs.getTimestamp("dateD"),
                            rs.getTimestamp("dateF")
                    );
                    inscriptions.add(inscription);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inscriptions;
    }
}
