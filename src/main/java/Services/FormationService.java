package Services;

import Entities.Formation;

import java.util.ArrayList;
import java.util.List;

public class FormationService {
    private final List<Formation> formations = new ArrayList<>();

    public void ajouterFormation(Formation formation) {
        formations.add(formation);
    }

    public List<Formation> obtenirToutesLesFormations() {
        return formations;
    }

    public Formation trouverFormationParId(int id) {
        for (Formation formation : formations) {
            if (formation.getId() == id) {
                return formation;
            }
        }
        return null; // Retourne null si la formation n'est pas trouvée
    }

    public void supprimerFormation(int id) {
        formations.removeIf(formation -> formation.getId() == id);
    }

    public List<Formation> getAllFormations() {
        return formations;
    }

    public Formation addFormation(String titre, String description) {
        // Générer un ID unique pour la nouvelle formation
        int id = formations.size() + 1;
        // Créer une nouvelle formation avec les informations fournies
        Formation nouvelleFormation = new Formation(id, titre, description);
        // Ajouter la nouvelle formation à la liste des formations
        formations.add(nouvelleFormation);
        // Retourner la nouvelle formation ajoutée
        return nouvelleFormation;
    }
}
