package Services;

import Entities.User;
import Utils.DataSource;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserService implements IService<User> {
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
            System.out.println("le mail existe deja !! ");
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

                // Appel de la méthode pour envoyer le mot de passe par e-mail
                sendPasswordByEmail(u.getEmail(), randomPassword);

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
                + "email='" + U.getEmail() + "' "
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

    @Override
    public User getUserById(int id) throws SQLException {
        String query = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setNom(resultSet.getString("nom"));
                    user.setPrenom(resultSet.getString("prenom"));
                    user.setEmail(resultSet.getString("email"));
                    return user;
                }
            }
        }
        return null;
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
        if (emailExists(email)) {
            System.out.println("j'ai trouvé le mail souhaité ! ");
        }
        else {
            System.out.println("le email ne se trouve pas ! ");
        }
        String query = "SELECT id, role, pwd FROM user WHERE email = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    System.out.println(userId);
                    String role = resultSet.getString("role");
                    System.out.println(role);
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


    public void sendPasswordByEmail(String userEmail, String password) {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Session pour l'envoi d'e-mail
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("may26saa@gmail.com", "nmca euui znmj hjjr");
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("may26saa@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
            message.setSubject("Your New Password");

            String htmlContent = "<p>Dear User,</p>"
                    + "<p>Your new password is: <strong>" + password + "</strong></p>"
                    + "<p>Please keep this password secure and consider changing it after logging in.</p>"
                    + "<p>Best regards,<br/>Your Application Team</p>";

            message.setContent(htmlContent, "text/html; charset=utf-8");


            // Envoyer le message
            Transport.send(message);

            System.out.println("Mot de passe envoyé par e-mail à : " + userEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'envoi du mot de passe par e-mail");
        }
    }


    public boolean resetPassword(int userId, String oldPassword, String newPassword) throws SQLException {
        String userEmail = null;

        // Vérifier si l'ancien mot de passe est correct et récupérer l'email de l'utilisateur
        String query = "SELECT pwd, email FROM user WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("pwd");
                    if (!oldPassword.equals(storedPassword)) {
                        // L'ancien mot de passe n'est pas correct
                        return false;
                    }
                    userEmail = resultSet.getString("email");
                } else {
                    // L'utilisateur n'existe pas
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Mettre à jour le mot de passe de l'utilisateur
        String updateQuery = "UPDATE user SET pwd = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = cnx.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setInt(2, userId);
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows > 0 && userEmail != null) {
                // Envoyer l'email après avoir mis à jour le mot de passe
                sendPasswordChangedEmail(userEmail);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void sendPasswordChangedEmail(String userEmail) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // Session pour l'envoi d'e-mail
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("may26saa@gmail.com", "nmca euui znmj hjjr");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("may26saa@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
            message.setSubject("Votre mot de passe a été changé");

            String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
            String htmlContent = "<p>Bonjour,</p>"
                    + "<p>Votre mot de passe a été changé avec succès le " + currentDate + ".</p>"
                    + "<p>Si vous n'êtes pas à l'origine de ce changement, veuillez contacter notre support immédiatement.</p>"
                    + "<p>Cordialement,<br/>L'équipe de votre application</p>";

            message.setContent(htmlContent, "text/html; charset=utf-8");

            // Envoyer le message
            Transport.send(message);

            System.out.println("Notification de changement de mot de passe envoyée à : " + userEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'envoi de la notification de changement de mot de passe");
        }
    }

}
