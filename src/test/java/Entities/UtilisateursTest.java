package Entities;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilisateursTest {

    @Test
    public void testCreationUtilisateur() {
        // Arrange
        int id = 1;
        String nom = "Werteni";
        String prenom = "Mariem";
        String email = "mariem.werteni@esprit.com";
        String mdp = "1234";
        String role = "etudiante";

        // Act
        Utilisateurs utilisateur = new Utilisateurs(id, nom, prenom, email, mdp, role);

        // Assert
        Assertions.assertEquals(id, utilisateur.getId());
        Assertions.assertEquals(nom, utilisateur.getNom());
        Assertions.assertEquals(prenom, utilisateur.getPrenom());
        Assertions.assertEquals(email, utilisateur.getEmail());
        Assertions.assertEquals(mdp, utilisateur.getMdp());
        Assertions.assertEquals(role, utilisateur.getRole());
    }

    @Test
    public void testModificationUtilisateur() {
        // Arrange
        Utilisateurs utilisateur = new Utilisateurs(1, "Werteni", "Mariem", "mariem.werteni@esprit.com", "1234", "etudiante");

        // Act
        utilisateur.setNom("Hkiri");
        utilisateur.setPrenom("Mayssa");
        utilisateur.setEmail("mayssa.hkiri@esprit.com");
        utilisateur.setMdp("12345");
        utilisateur.setRole("admin");

        // Assert
        Assertions.assertEquals("Hkiri", utilisateur.getNom());
        Assertions.assertEquals("Mayssa", utilisateur.getPrenom());
        Assertions.assertEquals("mayssa.hkiri@esprit.com", utilisateur.getEmail());
        Assertions.assertEquals("12345", utilisateur.getMdp());
        Assertions.assertEquals("admin", utilisateur.getRole());
    }
}
