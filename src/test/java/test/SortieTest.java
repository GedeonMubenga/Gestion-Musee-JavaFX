package test;

import static org.junit.Assert.*;
import org.junit.Test;
import musee.Sortie;

public class SortieTest {

    @Test
    public void testSortieNom() {
        Sortie sortie = new Sortie();
        assertEquals("sortie", sortie.getNom());
    }

    @Test
    public void testSortieObjetsVides() {
        Sortie sortie = new Sortie();
        assertNotNull(sortie.getObjets());
        assertEquals(0, sortie.getObjets().length);
    }
}
