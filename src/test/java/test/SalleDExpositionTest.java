package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import musee.Artefact;
import musee.SalleDExposition;

public class SalleDExpositionTest {

    private SalleDExposition salle;

    @Before
    public void setUp() {
        salle = new SalleDExposition("Galerie 1");
    }

    @Test
    public void testNom() {
        assertEquals("Galerie 1", salle.getNom());
    }

    @Test
    public void testInitObjets() {
        assertNotNull(salle.getObjets());
        assertEquals(0, salle.getObjets().length);
    }

    @Test
    public void testAjouterArtefact() {
        salle.ajouterArtefact(Artefact.PEINTURE);
        assertEquals(1, salle.getObjets().length);
        assertEquals(Artefact.PEINTURE, salle.getObjets()[0]);

        salle.ajouterArtefact(Artefact.SCULPTURE);
        assertEquals(2, salle.getObjets().length);
        assertEquals(Artefact.SCULPTURE, salle.getObjets()[1]);
    }
}
