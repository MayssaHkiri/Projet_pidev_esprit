package Entities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FormationTest {

    @Test
    public void testCreationFormation() {
        // Arrange
        int id = 1;
        String titre = "Titre de la formation";
        String description = "Description de la formation";
        int idEnseignant = 2;

        // Act
        Formation formation = new Formation(id, titre, description);

        // Assert
        Assertions.assertEquals(id, formation.getId());
        Assertions.assertEquals(titre, formation.getTitre());
        Assertions.assertEquals(description, formation.getDescription());
        Assertions.assertEquals(idEnseignant, formation.getIdEnseignant());
    }

    @Test
    public void testModificationFormation() {
        // Arrange
        Formation formation = new Formation(1, "Titre initial", "Description initiale");

        // Act
        formation.setTitre("Nouveau titre"); // Correction ici
        formation.setDescription("Nouvelle description"); // Correction ici
        formation.setIdEnseignant(3);

        // Assert
        Assertions.assertEquals("Nouveau titre", formation.getTitre());
        Assertions.assertEquals("Nouvelle description", formation.getDescription());
        Assertions.assertEquals(3, formation.getIdEnseignant());
    }
}
