package StartApplication;

import Entities.Formation;
import Services.FormationService;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        // Tester l'ajout d'une formation (décommentez pour tester)
        // testerAjoutFormation(new Formation(0, "Java", "Cours Java avancé", 2));

        // Tester la consultation d'une formation par ID
        testerConsulterFormationParId(2);
    }

    public static void testerConsulterFormationParId(int id) throws SQLException {
        FormationService formationService = new FormationService();
        Formation formation = formationService.getFormationById(id);
        if (formation != null) {
            System.out.println("Formation trouvée :");
            System.out.println("ID : " + formation.getId());
            System.out.println("Titre : " + formation.getTitre());
            System.out.println("Description : " + formation.getDescription());
            System.out.println("ID Enseignant : " + formation.getIdEnseignant());
        } else {
            System.out.println("Aucune formation trouvée avec l'ID : " + id);
        }
    }

    public static void testerAjoutFormation(Formation formation) throws SQLException {
        FormationService formationService = new FormationService();
        boolean isAdded = formationService.add(formation);
        if (isAdded) {
            System.out.println("La formation a été ajoutée avec succès.");
        } else {
            System.out.println("Échec de l'ajout de la formation.");
        }
    }

}
