package musee;


/**
 * Représente l'entrée unique du musée.
 * Classe Singleton.
 */
public class Entree extends Salle {

    /**
     * Constructeur pour créer une entrée.
     */
    public Entree() {
        super("entree");
    }

    @Override
    public Artefact[] getObjets() {
        return new Artefact[0]; // L'entrée n'a pas d'artefacts
    }
}
