package tourisme;
import musee.*;


/**
 * Interface définissant les actions d'un visiteur.
 */
public interface Visiteur {
    /**
     * Permet d'entrer dans un musée.
     * 
     * @param m Le musée à visiter.
     */
    void entrer(Musee m);

    /**
     * Permet de se déplacer vers une autre salle.
     * 
     * @param s La salle destination.
     */
    void seDeplacer(Salle s);

    /**
     * Affiche les artefacts présents dans la salle actuelle.
     */
    void voirArtefacts();

    /**
     * Termine la visite en sortant du musée.
     */
    void sortir();

    /**
     * Effectue une visite en suivant un plan.
     * 
     * @param plan Le plan de visite.
     * @param m    Le musée.
     * @return true si la visite se termine par une sortie du musée, false sinon.
     */
    boolean visiter(PlanDeVisite plan, Musee m);
}
