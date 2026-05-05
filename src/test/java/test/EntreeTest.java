package test;

import static org.junit.Assert.*;
import org.junit.Test;
import musee.Entree;

public class EntreeTest {

    @Test
    public void testEntreeNom() {
        Entree entree = new Entree();
        assertEquals("entree", entree.getNom());
    }

    @Test
    public void testEntreeObjetsVides() {
        Entree entree = new Entree();
        assertNotNull(entree.getObjets());
        assertEquals(0, entree.getObjets().length);
    }
}
