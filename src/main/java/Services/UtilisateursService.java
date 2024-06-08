package Services;

import Entities.Utilisateurs;
import Utils.DataSource;

import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateursService implements Iservice<Utilisateurs> {
    private Connection cnx = DataSource.getInstance().getCon();
    private Statement ste;

    public UtilisateursService() {
        try {
            ste = cnx.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    private String generateRandomPassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }

        return password.toString();
    }

    @Override
    public ArrayList<Utilisateurs> getAll() throws SQLException {
        ArrayList<Utilisateurs> utilisateurs = new ArrayList<>();
        String req = "SELECT id, nom, prenom, email, mdp, role FROM utilisateurs";

        try (ResultSet res = ste.executeQuery(req)) {
            while (res.next()) {
                Utilisateurs utilisateur = new Utilisateurs(
                        res.getInt("id"),
                        res.getString("nom"),
                        res.getString("prenom"),
                        res.getString("email"),
                        res.getString("mdp"),
                        res.getString("role")
                );
                utilisateurs.add(utilisateur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return utilisateurs;
    }

    @Override
    public boolean add(Utilisateurs u) throws SQLException {
        if (emailExists(u.getEmail())) {
            return false;
        }

        String randomPassword = generateRandomPassword(8);
        u.setMdp(randomPassword);

        String req = "INSERT INTO utilisateurs (nom, prenom, email, mdp, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, u.getNom());
            pst.setString(2, u.getPrenom());
            pst.setString(3, u.getEmail());
            pst.setString(4, u.getMdp());
            pst.setString(5, u.getRole());
            int result = pst.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String req = "DELETE FROM utilisateurs WHERE id = ?";
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
    public boolean update(Utilisateurs u) throws SQLException {
        String req = "UPDATE utilisateurs SET nom = ?, prenom = ?, email = ?, mdp = ?, role = ? WHERE id = ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, u.getNom());
            pst.setString(2, u.getPrenom());
            pst.setString(3, u.getEmail());
            pst.setString(4, u.getMdp());
            pst.setString(5, u.getRole());
            pst.setInt(6, u.getId());
            int updatedRows = pst.executeUpdate();
            return updatedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean emailExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM utilisateurs WHERE email = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Utilisateurs authenticate(String email, String password) throws SQLException {
        String query = "SELECT id, nom, prenom, role, mdp FROM utilisateurs WHERE email = ?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String role = rs.getString("role");
                    String storedPassword = rs.getString("mdp");
                    if (password.equals(storedPassword)) {
                        return new Utilisateurs(userId, nom, prenom, email, storedPassword, role);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Utilisateurs> search(String searchTerm) throws SQLException {
        List<Utilisateurs> utilisateurs = new ArrayList<>();
        String req = "SELECT * FROM utilisateurs WHERE nom LIKE ? OR prenom LIKE ? OR email LIKE ? OR role LIKE ?";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, "%" + searchTerm + "%");
            pst.setString(2, "%" + searchTerm + "%");
            pst.setString(3, "%" + searchTerm + "%");
            pst.setString(4, "%" + searchTerm + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Utilisateurs utilisateur = new Utilisateurs(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("mdp"),
                            rs.getString("role")
                    );
                    utilisateurs.add(utilisateur);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utilisateurs;
    }
}
