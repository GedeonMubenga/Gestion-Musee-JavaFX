package tourisme;
import musee.*;


/**
 * Implémentation d'un touriste qui parcourt le musée.
 */
public class Touriste implements Visiteur {
    private String nom;
    private Salle salleCourante;

    /**
     * Constructeur.
     * 
     * @param nom Nom du touriste.
     */
    public Touriste(String nom) {
        this.nom = nom;
    }

    @Override
    public void entrer(Musee m) {
        this.salleCourante = m.getEntree();
        System.out.println(nom + " entre dans le musée par l'" + salleCourante.getNom() + ".");
        voirArtefacts();
    }

    @Override
    public void seDeplacer(Salle s) {
        if (salleCourante == null) {
            System.out.println(nom + " n'est pas dans le musée.");
            return;
        }

        // Vérifier que la salle destination est bien un voisin
        boolean estVoisin = false;
        Salle[] voisins = salleCourante.getVoisins();
        for (int i = 0; i < voisins.length; i++) {
            if (voisins[i] == s) {
                estVoisin = true;
                break;
            }
        }

        if (estVoisin) {
            salleCourante = s;
            System.out.println(nom + " se déplace vers : " + salleCourante.getNom());
            voirArtefacts();
            if (salleCourante instanceof Sortie) {
                sortir();
            }
        } else {
            System.out.println("Impossible d'aller à " + s.getNom() + " depuis " + salleCourante.getNom());
        }
    }

    @Override
    public void voirArtefacts() {
        if (salleCourante == null)
            return;

        Artefact[] objets = salleCourante.getObjets();
        if (objets.length > 0) {
            System.out.print("  Artéfacts visibles : ");
            for (int i = 0; i < objets.length; i++) {
                System.out.print(objets[i] + (i < objets.length - 1 ? ", " : ""));
            }
            System.out.println();
        } else {
            System.out.println("  Aucun artéfact dans cette salle.");
        }

        // Afficher les directions possibles
        Salle[] voisins = salleCourante.getVoisins();
        if (voisins.length > 0) {
            System.out.print("  Chemins possibles : ");
            for (int i = 0; i < voisins.length; i++) {
                System.out.print(voisins[i].getNom() + (i < voisins.length - 1 ? ", " : ""));
            }
            System.out.println();
        }
    }

    @Override
    public void sortir() {
        if (salleCourante instanceof Sortie) {
            System.out.println(nom + " sort du musée. Visite terminée.");
            salleCourante = null;
        } else {
            System.out.println(nom + " ne peut pas sortir d'ici. Il faut aller à la Sortie.");
        }
    }

    /**
     * Récupère la salle courante pour que le Main puisse guider le visiteur.
     * 
     * @return La salle actuelle.
     */
    public Salle getSalleCourante() {
        return salleCourante;
    }

    @Override
    public boolean visiter(PlanDeVisite plan, Musee m) {
        if (!plan.valider(m)) {
            System.out.println("Le plan est invalide. Visite annulée.");
            return false;
        }

        String[] etapes = plan.getEtapes();
        if (etapes.length == 0) {
            System.out.println("Le plan est vide.");
            return false;
        }

        // On assume que la première étape est l'entrée ou que l'on commence par entrer.
        // Si la première étape n'est pas l'entrée, on doit d'abord entrer.
        if (salleCourante == null) {
            this.entrer(m);
        }

        // Si la première étape du plan est "entree" et qu'on y est déjà, on continue
        // Si le plan ne commence pas par où on est, il faudra gérer le déplacement
        // initial,
        // mais pour simplifier on va juste essayer de suivre le plan étape par étape.

        System.out.println("Début de la visite planifiée...");
        for (int i = 0; i < etapes.length; i++) {
            String nomEtape = etapes[i];
            Salle prochaine = m.getSalle(nomEtape);

            // Si on est déjà dans la salle (ex: première étape = entree), on ne bouge pas
            // mais on observe
            if (salleCourante == prochaine) {
                // Déjà sur place
            } else {
                this.seDeplacer(prochaine);
                // Si le déplacement a échoué (ex: pas voisin), on arrête
                if (salleCourante != prochaine && salleCourante != null) {
                    System.out.println("Arrêt imprévu de la visite planifiée.");
                    break;
                }
            }
        }
        System.out.println("Fin de la visite planifiée.");

        // Si on n'est plus dans le musée (salleCourante == null) après la boucle, c'est
        // une réussite complète (sortie).
        return salleCourante == null;
    }
}
