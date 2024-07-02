package StartApplication;

import Entities.Formation;
import Services.FormationService;
import Utils.DataSource;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        try {
            DataSource ds = DataSource.getInstance();
            Connection con = ds.getCon();

            // Service pour gérer les formations
            FormationService formationService = new FormationService();

            // Création de formations avec images en Blob
            Blob imageBlob1 = createImageBlob("ressources/image1.png");
            Blob imageBlob2 = createImageBlob("ressources/image2.png");

            // Définir les dates de formation
            LocalDate localDateFormation1 = LocalDate.of(2023, 7, 1);
            LocalDate localDateFormation2 = LocalDate.of(2023, 8, 1);
            String dateFormation1 = localDateFormation1.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String dateFormation2 = localDateFormation2.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            // Créer des objets Formation
            Formation formation1 = new Formation(1, "Java", "Cours Java avancé", imageBlob1, dateFormation1);
            Formation formation2 = new Formation(2, "Python", "Cours Python débutant", imageBlob2, dateFormation2);

            // Ajouter une formation
            if (formationService.addFormation(formation1)) {
                System.out.println("Formation Java ajoutée avec succès");
            } else {
                System.out.println("Erreur lors de l'ajout de la formation Java");
            }

            // Supprimer une formation par ID
            if (formationService.SupprimerFormation(1)) {
                System.out.println("Formation Java supprimée avec succès");
            } else {
                System.out.println("Erreur lors de la suppression de la formation Java");
            }

            // Trouver une formation par ID
            Formation foundFormation = formationService.getFormationById(2);
            if (foundFormation != null) {
                System.out.println("Formation trouvée : " + foundFormation);
            } else {
                System.out.println("Formation non trouvée");
            }

            // Lister toutes les formations
            System.out.println("Toutes les formations : " + formationService.getAllFormations());

            // Mettre à jour une formation
            formation2.setDescription("Cours Python intermédiaire");
            if (formationService.ModifierFormation(formation2)) {
                System.out.println("Formation Python mise à jour avec succès");
            } else {
                System.out.println("Erreur lors de la mise à jour de la formation Python");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode utilitaire pour créer un Blob à partir d'une image
    private static Blob createImageBlob(String imagePath) throws SQLException {
        try (InputStream inputStream = new FileInputStream(imagePath)) {
            byte[] imageBytes = inputStream.readAllBytes();
            return new javax.sql.rowset.serial.SerialBlob(imageBytes);
        } catch (Exception e) {
            throw new SQLException("Failed to create Blob from image: " + e.getMessage());
        }
    }
}
