package exemple;
import musee.*;
import tourisme.*;


/**
 * Classe principale pour tester le musée et la validation de plan.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("--- Création du Musée Pointe-à-Callière ---");
        Musee monMusee = new PointeACalliere();
        monMusee.construire();
        monMusee.afficherListeAdjacence();
        monMusee.toQuickchart();

        System.out.println("\n--- Test 1 : Plan Valide ---");
        PlanDeVisite plan1 = new PlanDeVisite();
        plan1.ajouterEtape("entree");
        plan1.ajouterEtape("eperon");
        plan1.ajouterEtape("crypte");

        if (plan1.valider(monMusee)) {
            System.out.println("Le plan 1 est valide.");
        } else {
            System.out.println("Le plan 1 est invalide.");
        }

        System.out.println("\n--- Test 2 : Plan Invalide (Saut impossible) ---");
        PlanDeVisite plan2 = new PlanDeVisite();
        plan2.ajouterEtape("entree");
        plan2.ajouterEtape("douane");
        // Entree n'est PAS connecté à Douane.

        if (plan2.valider(monMusee)) {
            System.out.println("Le plan 2 est valide.");
        } else {
            System.out.println("Le plan 2 est invalide.");
        }

        System.out.println("\n--- Test 3 : Plan Invalide (Salle inexistante) ---");
        PlanDeVisite plan3 = new PlanDeVisite();
        plan3.ajouterEtape("entree");
        plan3.ajouterEtape("Salle Imaginaire");

        if (plan3.valider(monMusee)) {
            System.out.println("Le plan 3 est valide.");
        } else {
            System.out.println("Le plan 3 est invalide.");
        }

        System.out.println("\n--- Test 4 : Simulation de Visite ---");
        Touriste alice = new Touriste("Alice");
        System.out.println("Alice va suivre le plan 1 (Valide).");
        boolean succesAlice = alice.visiter(plan1, monMusee);
        System.out.println("Alice est sortie ? " + succesAlice);

        System.out.println("\nBob essaie de suivre le plan 2 (Invalide).");
        Touriste bob = new Touriste("Bob");
        boolean succesBob = bob.visiter(plan2, monMusee);
        System.out.println("Bob est sorti ? " + succesBob);

        System.out.println("\n--- Test 5 : Visite Complète (avec Sortie) ---");
        PlanDeVisite planSortie = new PlanDeVisite();
        planSortie.ajouterEtape("entree");
        planSortie.ajouterEtape("eperon");
        planSortie.ajouterEtape("crypte");
        planSortie.ajouterEtape("station");
        planSortie.ajouterEtape("marins");
        planSortie.ajouterEtape("sortie");

        Touriste charlie = new Touriste("Charlie");
        boolean succesCharlie = charlie.visiter(planSortie, monMusee);
        System.out.println("Charlie est sorti ? " + succesCharlie);
    }
}
