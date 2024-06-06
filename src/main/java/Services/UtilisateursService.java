package Services;
import Entities.Utilisateurs;

import java.util.ArrayList;
import java.util.List;

public class UtilisateursService {
    private List<Utilisateurs> utilisateursList;

    public UtilisateursService() {
        this.utilisateursList = new ArrayList<>();
    }

    public void ajouterUtilisateur(Utilisateurs utilisateur) {
        utilisateursList.add(utilisateur);
    }

    public void supprimerUtilisateur(Utilisateurs utilisateur) {
        utilisateursList.remove(utilisateur);
    }

    public void modifierUtilisateur(int id, String nom, String prenom, String email, String mdp, String role) {
        for (Utilisateurs utilisateur : utilisateursList) {
            if (utilisateur.getId() == id) {
                utilisateur.setNom(nom);
                utilisateur.setPrenom(prenom);
                utilisateur.setEmail(email);
                utilisateur.setMdp(mdp);
                utilisateur.setRole(role);
                return;
            }
        }
        // Si l'utilisateur avec l'ID spécifié n'est pas trouvé, tu peux lever une exception ou faire une autre action appropriée.
        throw new IllegalArgumentException("Utilisateur avec l'ID spécifié non trouvé.");
    }

    public Utilisateurs trouverUtilisateurParId(int id) {
        for (Utilisateurs utilisateur : utilisateursList) {
            if (utilisateur.getId() == id) {
                return utilisateur;
            }
        }
        // Si l'utilisateur avec l'ID spécifié n'est pas trouvé, tu peux renvoyer null ou lever une exception.
        return null;
    }
}
