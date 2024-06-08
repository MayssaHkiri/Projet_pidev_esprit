package StartApplication;

import Entities.Formation;
import Services.FormationService;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
      // testerAjoutFormation(new Formation ("java" , "cours java avancé " , 2));
       testerConsulterFormationParId(2);
    }
    public static void testerConsulterFormationParId (int idFormation)
    {
        try {

            FormationService formationService = new FormationService();
            Formation formation = formationService.getFormationById(idFormation);
            if (formation != null) {
                System.out.println("Formation trouvée :");
                System.out.println("ID : " + formation.getId());
                System.out.println("Titre : " + formation.getTitre());
                System.out.println("Description : " + formation.getDescription());
                System.out.println("ID Enseignant : " + formation.getIdEnseignant());
            } else {
                System.out.println("Aucune formation trouvée avec l'ID : " + idFormation);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public static void testerAjoutFormation (Formation formation ) {
        try {
            FormationService formationService = new FormationService();
            boolean isAdded = formationService.add(formation);
            if (isAdded) {
                System.out.println("La formation a été ajoutée avec succès.");
            } else {
                System.out.println("Échec de l'ajout de la formation.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}