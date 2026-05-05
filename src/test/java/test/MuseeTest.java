package test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.ArrayList;

import musee.Musee;
import musee.Salle;
import musee.SalleDExposition;
import musee.Entree;
import musee.Sortie;

public class MuseeTest {

    // Simple implementation form testing the abstract class Musee
    private class TestMusee extends Musee {
        @Override
        public void construire() {
            SalleDExposition testSalle = new SalleDExposition("testSalle");
            ajouterSalle(testSalle);
            getEntree().ajouterVoisin(testSalle);
            testSalle.ajouterVoisin(getSortie());
        }
    }

    private TestMusee musee;

    @Before
    public void setUp() {
        musee = new TestMusee();
        musee.construire();
    }

    @Test
    public void testInitialisation() {
        assertNotNull(musee.getEntree());
        assertNotNull(musee.getSortie());
        assertTrue(musee.getEntree() instanceof Entree);
        assertTrue(musee.getSortie() instanceof Sortie);
    }

    @Test
    public void testGetSalle() {
        assertNotNull(musee.getSalle("entree"));
        assertNotNull(musee.getSalle("sortie"));
        
        Salle salleRef = musee.getSalle("testSalle");
        assertNotNull(salleRef);
        assertEquals("testSalle", salleRef.getNom());
        
        assertNull(musee.getSalle("inexistante"));
    }

    @Test
    public void testAdjacencyList() {
        Map<String, ArrayList<String>> adjList = musee.getAdjacencyList();
        assertNotNull(adjList);
        
        assertTrue(adjList.containsKey("entree"));
        assertTrue(adjList.containsKey("sortie"));
        assertTrue(adjList.containsKey("testSalle"));
        
        ArrayList<String> entreeVoisins = adjList.get("entree");
        assertTrue(entreeVoisins.contains("testSalle"));
        
        ArrayList<String> testSalleVoisins = adjList.get("testSalle");
        assertTrue(testSalleVoisins.contains("entree"));
        assertTrue(testSalleVoisins.contains("sortie"));
    }
}
