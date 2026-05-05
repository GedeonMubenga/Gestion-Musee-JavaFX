package musee;


/**
 * Représente une salle d'exposition standard contenant des artefacts.
 */
public class SalleDExposition extends Salle {
    private Artefact[] objets;

    /**
     * Constructeur.
     * 
     * @param nom Le nom de la salle.
     */
    public SalleDExposition(String nom) {
        super(nom);
        this.objets = new Artefact[0];
    }

    /**
     * Ajoute un objet (artefact) à la collection de la salle.
     * Redimensionne le tableau d'objets manuellement.
     * 
     * @param a L'artefact à ajouter.
     */
    public void ajouterArtefact(Artefact a) {
        // Créer un nouveau tableau plus grand
        Artefact[] nouveauxObjets = new Artefact[this.objets.length + 1];

        // Copier les éléments existants
        for (int i = 0; i < this.objets.length; i++) {
            nouveauxObjets[i] = this.objets[i];
        }

        // Ajouter le nouvel élément
        nouveauxObjets[this.objets.length] = a;

        // Remplacer l'ancien tableau
        this.objets = nouveauxObjets;
    }

    @Override
    public Artefact[] getObjets() {
        return objets;
    }
}
