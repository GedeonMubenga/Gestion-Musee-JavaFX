package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import musee.*;

public class SalleTest {

    private Salle salleA;
    private Salle salleB;
    private SalleDExposition salleExpo;

    @Before
    public void setUp() {
        // Create concrete implementation of Salle for testing logic
        salleA = new SalleDExposition("Salle A");
        salleB = new SalleDExposition("Salle B");
        salleExpo = new SalleDExposition("Exposition");
    }

    @Test
    public void testAjouterVoisin() {
        salleA.ajouterVoisin(salleB);

        // Check bidirectional connection
        assertTrue("Salle A devrait avoir Salle B comme voisin", salleA.estVoisin(salleB));
        assertTrue("Salle B devrait avoir Salle A comme voisin", salleB.estVoisin(salleA));
    }

    @Test
    public void testAjouterVoisinDoublon() {
        salleA.ajouterVoisin(salleB);
        salleA.ajouterVoisin(salleB); // Ajout répété

        // Check no duplication (using raw array access if needed, or assumtion on
        // behavior)
        // With current implementation, we can check array length if we want, but
        // 'estVoisin' is safer API.
        // Let's count neighbors manually
        int count = 0;
        for (Salle s : salleA.getVoisins()) {
            if (s == salleB)
                count++;
        }
        assertEquals("Il ne devrait y avoir qu'une seule instance de Salle B", 1, count);
    }

    @Test
    public void testEstVoisin() {
        assertFalse(salleA.estVoisin(salleB));
        salleA.ajouterVoisin(salleB);
        assertTrue(salleA.estVoisin(salleB));
    }

    @Test
    public void testAjouterArtefact() {
        Artefact art = Artefact.PEINTURE;
        salleExpo.ajouterArtefact(art);

        Artefact[] objets = salleExpo.getObjets();
        assertEquals("La salle devrait contenir 1 artefact", 1, objets.length);
        assertEquals("L'artefact devrait être PEINTURE", Artefact.PEINTURE, objets[0]);
    }
}
