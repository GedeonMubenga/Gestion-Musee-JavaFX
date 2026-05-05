package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import musee.*;
import tourisme.*;
import exemple.PointeACalliere;

public class TouristeTest {

    private Musee musee;
    private Touriste touriste;

    @Before
    public void setUp() {
        musee = new PointeACalliere();
        musee.construire();
        touriste = new Touriste("Testeur");
    }

    @Test
    public void testSeDeplacerValide() {
        touriste.entrer(musee); // Doit être dans "entree"
        Salle avant = touriste.getSalleCourante();
        assertEquals("entree", avant.getNom());

        Salle destination = musee.getSalle("eperon"); // Voisin de entree
        touriste.seDeplacer(destination);

        assertEquals("Touriste devrait être dans Eperon", "eperon", touriste.getSalleCourante().getNom());
    }

    @Test
    public void testSeDeplacerInvalide() {
        touriste.entrer(musee);
        Salle destinationLointaine = musee.getSalle("douane"); // Pas voisin de entree

        touriste.seDeplacer(destinationLointaine);

        assertEquals("Touriste ne devrait pas avoir bougé", "entree", touriste.getSalleCourante().getNom());
    }

    @Test
    public void testVisiterReussite() {
        PlanDeVisite plan = new PlanDeVisite();
        plan.ajouterEtape("entree");
        plan.ajouterEtape("eperon");
        plan.ajouterEtape("crypte");
        plan.ajouterEtape("station");
        plan.ajouterEtape("marins");
        plan.ajouterEtape("sortie");

        assertTrue("La visite complète devrait retourner true (sortie)", touriste.visiter(plan, musee));
        assertNull("Le touriste devrait être sorti (salleCourante null)", touriste.getSalleCourante());
    }

    @Test
    public void testVisiterEchec() {
        PlanDeVisite plan = new PlanDeVisite();
        plan.ajouterEtape("entree");
        plan.ajouterEtape("eperon");
        // S'arrête sans sortir

        assertFalse("La visite incomplète devrait retourner false", touriste.visiter(plan, musee));
        assertNotNull("Le touriste devrait encore être dans le musée", touriste.getSalleCourante());
    }
}
