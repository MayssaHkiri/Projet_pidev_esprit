package Services;

import Entities.User;
import Utils.DataSource;

import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements Iservice<User> {
    private Connection cnx= DataSource.getInstance().getCon();
    private Statement ste;

    public UserService() {
        try {
            ste=cnx.createStatement();
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
    public ArrayList<User> getAll() throws SQLException {
        ArrayList<User> users = new ArrayList<>();

        String req = "SELECT id, nom, prenom, email, role FROM user ";
        Statement st;
        try {
            ResultSet res = ste.executeQuery(req);
            while (res.next()) {
                User user = new User();
                user.setId(res.getInt("id"));
                user.setNom(res.getString("nom"));
                user.setPrenom(res.getString("prenom"));
                user.setEmail(res.getString("email"));
                user.setRole(res.getString("role"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public boolean add(User u) throws SQLException {
        // Check if the user already exists
        if (emailExists(u.getEmail())) {
            return false;
        }
        // Generate a random password and set it to the user
        String randomPassword = generateRandomPassword(8);
        u.setPassword(randomPassword);

        String req = "INSERT INTO user "
                + "( nom, prenom, email, pwd, role)\r\n"
                + "VALUES ( ?, ?, ?, ?, ?)";
        PreparedStatement pst = cnx.prepareStatement(req);
        try {
            pst.setString(1, u.getNom());
            pst.setString(2, u.getPrenom());
            pst.setString(3 , u.getEmail()) ;
            pst.setString(4 , u.getPassword()) ;
            pst.setString(5 , u.getRole()) ;
            int result = pst.executeUpdate();
            if (result > 0) {
                System.out.println("Ajout réussi");
                return true;
            } else  {
                System.out.println("Échec de l'ajout");
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false ;
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String req = "DELETE FROM user WHERE id = ?";
        PreparedStatement st = cnx.prepareStatement(req);
        try {
            st.setInt(1, id);
            int deletedRows = st.executeUpdate();
            return deletedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            st.close();
        }
    }

    @Override
    public boolean update(User U) throws SQLException {
        int er = 0;

        System.out.println(U.getId());
        String req = "UPDATE user "
                + "SET nom='" + U.getNom() + "', "
                + "prenom='" + U.getPrenom()+ "', "
                + "email='" + U.getEmail() + "', "
                + "WHERE id=" + U.getId() + "";
        Statement st;

        try {
            st = cnx.createStatement();
            er = st.executeUpdate(req);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return er != 0;
    }
    public boolean emailExists(String email) throws SQLException   {
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public User authenticate(String email, String password) throws SQLException {
        String query = "SELECT id, role , pwd FROM user WHERE email = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    String role = resultSet.getString("role");
                    String storedPassword = resultSet.getString("pwd");
                    if (password.equals(storedPassword)) {
                        return new User(userId, role );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur SQL : " + e.getMessage());
        }
        return null; // Retourne null si l'authentification échoue ou s'il y a une erreur
    }

    public List<User> search(String searchTerm) throws SQLException {
        List<User> users = new ArrayList<>();
        String req = "SELECT * FROM user WHERE nom LIKE ? OR prenom LIKE ? OR email LIKE ? OR role LIKE ? ";
        try (PreparedStatement pst = cnx.prepareStatement(req)) {
            pst.setString(1, "%" + searchTerm + "%");
            pst.setString(2, "%" + searchTerm + "%");
            pst.setString(3, "%" + searchTerm + "%");
            pst.setString(4, "%" + searchTerm + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom"));
                user.setPrenom(rs.getString("prenom"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
