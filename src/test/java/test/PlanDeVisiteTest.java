package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import musee.*;
import tourisme.*;
import exemple.PointeACalliere;

public class PlanDeVisiteTest {

    private Musee musee;
    private PlanDeVisite plan;

    @Before
    public void setUp() {
        musee = new PointeACalliere();
        musee.construire();
        plan = new PlanDeVisite();
    }

    @Test
    public void testValiderPlanValide() {
        plan.ajouterEtape("entree");
        plan.ajouterEtape("eperon");
        plan.ajouterEtape("crypte");

        assertTrue("Le plan valide devrait être accepté", plan.valider(musee));
    }

    @Test
    public void testValiderPlanInvalideSaut() {
        plan.ajouterEtape("entree");
        plan.ajouterEtape("douane"); // Pas connecté directement

        assertFalse("Le plan avec saut impossible devrait être rejeté", plan.valider(musee));
    }

    @Test
    public void testValiderPlanInvalideSalleInexistante() {
        plan.ajouterEtape("entree");
        plan.ajouterEtape("Narnia");

        assertFalse("Le plan avec salle inconnue devrait être rejeté", plan.valider(musee));
    }

    @Test
    public void testPlanVide() {
        assertTrue("Un plan vide est trivialement valide (ou selon spécs)", plan.valider(musee));
    }
}
