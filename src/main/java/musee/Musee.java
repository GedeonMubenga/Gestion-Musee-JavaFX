package musee;


import java.io.Serializable;

/**
 * Classe abstraite représentant un Musée.
 * Contient la structure globale des salles.
 */
public abstract class Musee implements Serializable {
    protected Salle[] salles;
    protected Entree entree;
    protected Sortie sortie;

    /**
     * Constructeur du musée.
     * Initialise l'entrée et la sortie.
     */
    public Musee() {
        this.entree = new Entree();
        this.sortie = new Sortie();
        // Initialiser avec au moins l'entrée et la sortie
        this.salles = new Salle[2];
        this.salles[0] = this.entree;
        this.salles[1] = this.sortie;
    }

    /**
     * Méthode abstraite pour construire la structure interne du musée (les autres
     * salles).
     */
    public abstract void construire();

    /**
     * Retourne l'entrée du musée.
     * 
     * @return L'entrée.
     */
    public Entree getEntree() {
        return entree;
    }

    /**
     * Retourne la sortie du musée.
     * 
     * @return La sortie.
     */
    public Sortie getSortie() {
        return sortie;
    }

    /**
     * Ajoute une salle à la liste des salles du musée.
     * 
     * @param s La salle à ajouter.
     */
    protected void ajouterSalle(Salle s) {
        Salle[] nouvellesSalles = new Salle[this.salles.length + 1];
        for (int i = 0; i < this.salles.length; i++) {
            nouvellesSalles[i] = this.salles[i];
        }
        nouvellesSalles[this.salles.length] = s;
        this.salles = nouvellesSalles;
    }

    /**
     * Recherche une salle par son nom dans le musée.
     * 
     * @param nom Le nom de la salle.
     * @return La salle trouvée ou null.
     */
    public Salle getSalle(String nom) {
        for (int i = 0; i < this.salles.length; i++) {
            Salle s = this.salles[i];
            if (s.getNom().equals(nom)) {
                return s;
            }
        }
        return null;
    }

    /**
     * Affiche la structure du graphe sous forme de liste d'adjacence dans la
     * console.
     */
    public void afficherListeAdjacence() {
        System.out.println("Structure du Musée (Liste d'Adjacence) :");
        for (int i = 0; i < this.salles.length; i++) {
            Salle s = this.salles[i];
            System.out.print("  [" + s.getNom() + "] -> ");
            Salle[] voisins = s.getVoisins();
            if (voisins.length == 0) {
                System.out.print("Aucun voisin");
            } else {
                for (int j = 0; j < voisins.length; j++) {
                    System.out.print(voisins[j].getNom() + (j < voisins.length - 1 ? ", " : ""));
                }
            }
            System.out.println();
        }
    }

    /**
     * Affiche la structure du graphe en format DOT (Graphviz).
     * Peut être visualisé avec des outils en ligne comme :
     * - https://graphview.net/
     * - https://edotor.net/
     */
    public void afficherCodeDot() {
        System.out.println("\n--- Code DOT ---");
        System.out.println("graph Musee {");
        for (int i = 0; i < this.salles.length; i++) {
            Salle s = this.salles[i];
            String nom = s.getNom();
            // Utiliser des guillemets si le nom contient des espaces ou caractères spéciaux
            // (ce qui n'est plus le cas avec les clés simples, mais bonne pratique)
            // Ici les clés sont 'entree', 'eperon', etc. donc ça passe sans quotes, mais
            // soyons prudents pour l'avenir.
            // On va assumer des clés simples pour l'instant comme demandé.
            Salle[] voisins = s.getVoisins();
            if (voisins.length == 0) {
                System.out.println("  " + nom + ";");
            } else {
                for (int j = 0; j < voisins.length; j++) {
                    Salle v = voisins[j];
                    // Pour éviter les doublons A--B et B--A, on pourrait filtrer, mais pour
                    // l'inspection simple
                    // on va tout afficher. Les outils gèrent souvent les doublons.
                    System.out.println("  " + nom + " -- " + v.getNom() + ";");
                }
            }
        }
        System.out.println("}");
        System.out.println("----------------\n");
    }

    /**
     * Génère une URL QuickChart pour visualiser le graphe.
     */
    public void toQuickchart() {
        String graph = "graph{";
        for (int i = 0; i < this.salles.length; i++) {
            Salle s = this.salles[i];
            String nom = s.getNom();
            Salle[] voisins = s.getVoisins();
            if (voisins.length == 0) {
                graph += nom + ";";
            } else {
                for (int j = 0; j < voisins.length; j++) {
                    Salle v = voisins[j];
                    graph += nom + "--" + v.getNom() + ";";
                }
            }
        }
        graph += "}";
        System.out.println("\n--- QuickChart URL ---");
        System.out.println("https://quickchart.io/graphviz?graph=" + graph);
        System.out.println("----------------------\n");
    }

    public java.util.Map<String, java.util.ArrayList<String>> getAdjacencyList() {
        java.util.Map<String, java.util.ArrayList<String>> map = new java.util.HashMap<>();
        for (int i = 0; i < this.salles.length; i++) {
            Salle s = this.salles[i];
            java.util.ArrayList<String> neighbors = new java.util.ArrayList<>();
            for (Salle neighbor : s.getVoisins()) {
                neighbors.add(neighbor.getNom());
            }
            map.put(s.getNom(), neighbors);
        }
        return map;
    }
}
