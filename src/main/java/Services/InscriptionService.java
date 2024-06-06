package Services;
import Entities.Inscription;

import java.util.ArrayList;
import java.util.List;

public class InscriptionService {
    private List<Inscription> inscriptions = new ArrayList<>();

    public void ajouterInscription(Inscription inscription) {
        inscriptions.add(inscription);
    }

    public List<Inscription> obtenirToutesLesInscriptions() {
        return inscriptions;
    }

    public Inscription trouverInscriptionParId(int id) {
        return inscriptions.stream().filter(inscription -> inscription.getId() == id).findFirst().orElse(null);
    }

    public void supprimerInscription(int id) {
        inscriptions.removeIf(inscription -> inscription.getId() == id);
    }
}
