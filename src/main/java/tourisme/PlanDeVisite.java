package tourisme;
import musee.*;


/**
 * Représente un plan de visite planifié par un touriste.
 * Contient une séquence de noms de salles à visiter.
 */
public class PlanDeVisite {
    private String[] etapes;

    /**
     * Constructeur.
     */
    public PlanDeVisite() {
        this.etapes = new String[0];
    }

    /**
     * Ajoute une étape (nom de salle) au plan.
     * 
     * @param nom Le nom de la salle à ajouter.
     */
    public void ajouterEtape(String nom) {
        String[] nouvellesEtapes = new String[this.etapes.length + 1];
        for (int i = 0; i < this.etapes.length; i++) {
            nouvellesEtapes[i] = this.etapes[i];
        }
        nouvellesEtapes[this.etapes.length] = nom;
        this.etapes = nouvellesEtapes;
    }

    /**
     * Retourne les étapes du plan.
     * 
     * @return Le tableau des noms de salles.
     */
    public String[] getEtapes() {
        return etapes;
    }

    /**
     * Valide le plan de visite.
     * Vérifie que chaque étape consécutive est reliée par une connexion (voisin)
     * dans le musée donné.
     * 
     * @param m Le musée dans lequel valider le plan.
     * @return true si le plan est valide, false sinon.
     */
    public boolean valider(Musee m) {
        if (this.etapes.length <= 1) {
            return true; // 0 ou 1 étape est toujours valide
        }

        for (int i = 0; i < this.etapes.length - 1; i++) {
            String nomActuelle = this.etapes[i];
            String nomSuivante = this.etapes[i + 1];

            Salle actuelle = m.getSalle(nomActuelle);
            Salle suivante = m.getSalle(nomSuivante);

            if (actuelle == null) {
                System.out.println("Plan invalide : La salle '" + nomActuelle + "' n'existe pas dans ce musée.");
                return false;
            }
            if (suivante == null) {
                System.out.println("Plan invalide : La salle '" + nomSuivante + "' n'existe pas dans ce musée.");
                return false;
            }

            // Vérifier si suivante est un voisin de actuelle
            boolean lienExiste = false;
            for (int k = 0; k < actuelle.getVoisins().length; k++) {
                Salle voisin = actuelle.getVoisins()[k];
                if (voisin == suivante) {
                    lienExiste = true;
                    break;
                }
            }

            if (!lienExiste) {
                System.out.println("Plan invalide : Pas de connexion entre " + nomActuelle + " et " + nomSuivante);
                return false;
            }
        }
        return true;
    }
}
