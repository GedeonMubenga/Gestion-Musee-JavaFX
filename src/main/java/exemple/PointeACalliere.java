package exemple;
import musee.*;


/**
 * Une implémentation concrète du musée Pointe-à-Callière.
 * Salles :
 * - "eperon" : L'Éperon
 * - "crypte" : Crypte archéologique
 * - "douane" : Ancienne-Douane
 * - "station" : Station de pompage D'Youville
 * - "marins" : Maison des-Marins
 */
public class PointeACalliere extends Musee {

    /**
     * Construit le musée avec les pavillons spécifiques.
     */
    @Override
    public void construire() {
        // Création des salles spécifiques
        SalleDExposition eperon = new SalleDExposition("eperon");
        eperon.ajouterArtefact(Artefact.SCULPTURE);
        eperon.ajouterArtefact(Artefact.VETEMENT);

        SalleDExposition crypte = new SalleDExposition("crypte");
        crypte.ajouterArtefact(Artefact.VASE);

        SalleDExposition douane = new SalleDExposition("douane");
        douane.ajouterArtefact(Artefact.PEINTURE);
        douane.ajouterArtefact(Artefact.LIVRE);

        SalleDExposition station = new SalleDExposition("station");

        SalleDExposition marins = new SalleDExposition("marins");
        marins.ajouterArtefact(Artefact.SCULPTURE);

        // Ajout des salles au musée
        ajouterSalle(eperon);
        ajouterSalle(crypte);
        ajouterSalle(douane);
        ajouterSalle(station);
        ajouterSalle(marins);

        // Connexions logiques (Hypothèse de structure, car je n'ai pas le plan réel
        // sous les yeux)
        // Entrée -> Éperon
        this.entree.ajouterVoisin(eperon);

        // Éperon <-> Crypte
        eperon.ajouterVoisin(crypte);

        // Crypte <-> Douane
        crypte.ajouterVoisin(douane);

        // Douane <-> Station
        douane.ajouterVoisin(station);

        // Station <-> Marins
        station.ajouterVoisin(marins);

        // Marins <-> Sortie
        marins.ajouterVoisin(this.sortie);

        // Connexions supplémentaires pour un graphe plus intéressant
        // Éperon <-> Douane (passerelle)
        eperon.ajouterVoisin(douane);

        // Crypte <-> Station (souterrain)
        crypte.ajouterVoisin(station);
    }
}
