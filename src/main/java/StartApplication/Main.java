package StartApplication;

import Entities.Offre;
import Services.ServiceOffre;
import Utils.DataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        DataSource ds = DataSource.getInstance();

        // Obtention de la connexion
        Connection con = ds.getCon();

        // Utilisation de la connexion pour effectuer une opération sur la base de données
        try {
            Statement stmt = con.createStatement();
            // Exécutez votre requête SQL ici
            // Par exemple : ResultSet rs = stmt.executeQuery("SELECT * FROM your_table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ServiceOffre serviceOffre = new ServiceOffre();

        Offre offre = new Offre(1,"offre1","description offre1","1er année","3 mois","2023-06-04","entreprise1","2023-06-04");
        Offre offre2 = new Offre(4,"offre2","description offre2","1er année","3 mois","2023-06-04","entreprise2","2023-06-04");

        //ajouter un offre
       /* try {
            serviceOffre.ajouter(offre);
            System.out.println("offre ajoutée avec succés");
        }catch (Exception e) {
            e.printStackTrace();
        }*/

        //supprimer un offre
       /* try{
            serviceOffre.supprimer(offre);
        }catch (Exception e) {
            e.printStackTrace();
        }*/

        //delete all rows
        /*try{
            serviceOffre.supprimerAll();
        }catch (Exception e) {
            e.printStackTrace();
        }*/
        //find an offre by id
        try{
            System.out.println(serviceOffre.findById(11));
        }catch (Exception e) {
            e.printStackTrace();
        }
        //list all the offre rows
        /*try{
            System.out.println(serviceOffre.findAll());
        }catch (Exception e) {
            e.printStackTrace();
        }
        */

        //update a row
       /*  try{
            serviceOffre.update(offre2);
        }catch (Exception e) {
            e.printStackTrace();
        }*/

    }


    }
