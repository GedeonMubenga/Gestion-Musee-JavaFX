package musee;


/**
 * Représente la sortie unique du musée.
 * Classe Singleton.
 */
public class Sortie extends Salle {

    /**
     * Constructeur pour créer une sortie.
     */
    public Sortie() {
        super("sortie");
    }

    @Override
    public Artefact[] getObjets() {
        return new Artefact[0]; // La sortie n'a pas d'artefacts
    }
}
