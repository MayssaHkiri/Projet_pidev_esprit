package Services;
import Entities.Formation;

import java.util.ArrayList;
import java.util.List;

public class FormationService {
    private List<Formation> formations = new ArrayList<>();

    public void ajouterFormation(Formation formation) {
        formations.add(formation);
    }

    public List<Formation> obtenirToutesLesFormations() {
        return formations;
    }

    public Formation trouverFormationParId(int id) {
        return formations.stream().filter(formation -> formation.getId() == id).findFirst().orElse(null);
    }

    public void supprimerFormation(int id) {
        formations.removeIf(formation -> formation.getId() == id);
    }
}
