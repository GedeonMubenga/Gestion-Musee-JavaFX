package musee;

import java.io.Serializable;

/**
 * Classe abstraite représentant une salle générique du musée (un noeud du
 * graphe).
 * Gère le nom et les connexions (voisins).
 */
public abstract class Salle implements Serializable {
    protected String nom;
    protected Salle[] voisins;

    /**
     * Constructeur de base.
     * 
     * @param nom Le nom de la salle.
     */
    public Salle(String nom) {
        this.nom = nom;
        this.voisins = new Salle[0];
    }

    /**
     * Retourne le nom de la salle.
     * 
     * @return Le nom.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Ajoute un voisin (une connexion vers une autre salle).
     * Connecte aussi l'autre salle à celle-ci pour que le graphe soit non orienté.
     * 
     * @param s La salle voisine à ajouter.
     */
    public void ajouterVoisin(Salle s) {
        // Vérifier si le voisin est déjà présent pour éviter les doublons infinis
        if (estVoisin(s)) {
            return;
        }

        // Ajouter s à mes voisins
        Salle[] nouveauxVoisins = new Salle[this.voisins.length + 1];
        for (int i = 0; i < this.voisins.length; i++) {
            nouveauxVoisins[i] = this.voisins[i];
        }
        nouveauxVoisins[this.voisins.length] = s;
        this.voisins = nouveauxVoisins;

        // Ajouter moi-même aux voisins de s (bidirectionnel)
        s.ajouterVoisin(this);
    }

    /**
     * Vérifie si une salle est déjà voisine.
     * 
     * @param s La salle à vérifier.
     * @return Vrai si s est déjà un voisin.
     */
    /**
     * Vérifie si une salle est déjà voisine.
     * 
     * @param s La salle à vérifier.
     * @return Vrai si s est déjà un voisin.
     */
    public boolean estVoisin(Salle s) {
        for (int i = 0; i < this.voisins.length; i++) {
            if (this.voisins[i] == s) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retourne les voisins de la salle.
     * 
     * @return Un tableau de Salles.
     */
    public Salle[] getVoisins() {
        return voisins;
    }

    /**
     * Méthode abstraite pour récupérer les objets de la salle.
     * Les sous-classes doivent définir comment elles stockent/fournissent les
     * objets.
     * 
     * @return Un tableau d'artefacts.
     */
    public abstract Artefact[] getObjets();
}
