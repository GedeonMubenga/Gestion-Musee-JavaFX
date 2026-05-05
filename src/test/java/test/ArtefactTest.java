package test;

import static org.junit.Assert.*;
import org.junit.Test;
import musee.Artefact;

public class ArtefactTest {

    @Test
    public void testArtefactValues() {
        Artefact[] valeurs = Artefact.values();
        assertNotNull(valeurs);
        assertTrue(valeurs.length > 0);
        
        // Tester que quelques valeurs existent bien
        assertEquals(Artefact.PEINTURE, Artefact.valueOf("PEINTURE"));
        assertEquals(Artefact.VASE, Artefact.valueOf("VASE"));
        assertEquals(Artefact.SCULPTURE, Artefact.valueOf("SCULPTURE"));
        assertEquals(Artefact.VETEMENT, Artefact.valueOf("VETEMENT"));
        assertEquals(Artefact.LIVRE, Artefact.valueOf("LIVRE"));
    }
}
