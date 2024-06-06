package Entities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;

public class InscriptionTest {

    @Test
    public void testCreationInscription() {
        // Arrange
        int id = 1;
        int idFormation = 100;
        int idEtudiant = 200;
        Timestamp dateD = new Timestamp(System.currentTimeMillis());
        Timestamp dateF = new Timestamp(System.currentTimeMillis());

        // Act
        Inscription inscription = new Inscription(id, idFormation, idEtudiant, dateD, dateF);

        // Assert
        Assertions.assertEquals(id, inscription.getId());
        Assertions.assertEquals(idFormation, inscription.getIdFormation());
        Assertions.assertEquals(idEtudiant, inscription.getIdEtudiant());
        Assertions.assertEquals(dateD, inscription.getDateD());
        Assertions.assertEquals(dateF, inscription.getDateF());
    }

    @Test
    public void testModificationInscription() {
        // Arrange
        Inscription inscription = new Inscription(1, 100, 200, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

        // Act
        inscription.setIdFormation(101);
        inscription.setIdEtudiant(201);
        inscription.setDateD(new Timestamp(System.currentTimeMillis()));
        inscription.setDateF(new Timestamp(System.currentTimeMillis()));

        // Assert
        Assertions.assertEquals(101, inscription.getIdFormation());
        Assertions.assertEquals(201, inscription.getIdEtudiant());
        // Vérifier les dates est un peu plus complexe, il faudrait tester si elles sont différentes de la date courante
        Assertions.assertNotEquals(new Timestamp(System.currentTimeMillis()), inscription.getDateD());
        Assertions.assertNotEquals(new Timestamp(System.currentTimeMillis()), inscription.getDateF());
    }
}
