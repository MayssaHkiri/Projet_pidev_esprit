package StartApplication;


import Utils.DatabaseUpdater;

import java.sql.SQLException;

import Entities.User;
import Services.UserService;


import java.util.ArrayList;
import java.util.List;

public class Main {

    private static UserService us = new UserService();
    public static void main(String[] args) throws SQLException {
        DatabaseUpdater updater = new DatabaseUpdater();
        updater.updateDatabase();


        //testerCreationCompte(new User("zogh" , "ferr" , "feriel.zoghlemi@esprit.tn" , "etudiant"));
        //testerAffichage();
        //testerDelete(1);
        //testerUpdate(new User(2, "benAti" , "nour" , "nour.benati@esprit.tn"));
        //testerAuthentification("feriel.zoghlemi@esprit.tn" , "l3v20kox") ;
        //testerSearch("fer");
        //String testEmail = "mayssahkiri2001@gmail.com";
        //String testPassword = "testpassword123";
        //us.sendPasswordByEmail(testEmail, testPassword);
    }

    public static void testerCreationCompte(User user) {
        boolean result = false;
        try {
            result = us.add(new User(user.getNom(), user.getPrenom(), user.getEmail(), user.getRole()));
            System.out.println(result);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void testerAffichage() {
        try {
            ArrayList<User> users = us.getAll();
            for (User u : users) {
                System.out.println("ID:" + u.getId());
                System.out.println("Nom:" + u.getNom());
                System.out.println("Prenom : " + u.getPrenom());
                System.out.println("Email : " + u.getEmail());
                System.out.println("Role : " + u.getRole());
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void testerDelete(int userIdToDelete) {
        try {
            boolean isDeleted = us.delete((userIdToDelete));
            if (isDeleted) {
                System.out.println("utilisateur supprimé avec succès ! ");
            } else {
                System.out.println("Echéc de la suppression de l'utlisateur ! ");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static void testerUpdate(User newUser) {
        try {
            boolean isUpdated = us.update(newUser);
            if (isUpdated) {
                System.out.println("Utilisateur mis à jour avec succès ! ");
            } else {
                System.out.println("Echec de la mise à jour de l'utilisateur ");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void testerAuthentification(String email, String password) {
        try {
            User authentificatedUser = us.authenticate(email, password);
            if (authentificatedUser != null) {
                System.out.println("Authentification réussie ! ");
                System.out.println("ID : " + authentificatedUser.getId());
                System.out.println("Role : " + authentificatedUser.getRole());
            } else {
                System.out.println("Echec de l'authentification ");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void testerSearch(String searchTerm) {
        try {
            List<User> users = us.search(searchTerm);
            for (User user : users) {
                System.out.println("ID : " + user.getId());
                System.out.println(("Nom : " + user.getNom()));
                System.out.println("Prenom : " + user.getPrenom());
                System.out.println("Email : " + user.getEmail());
                System.out.println("Role : " + user.getRole());
                System.out.println("---------------------------------");
            }
        }
        catch (Exception e )  {
            System.out.println(e);
        }
    }


}



