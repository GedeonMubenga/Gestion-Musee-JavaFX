package test;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import musee.Artefact;
import musee.Musee;
import musee.MuseeFichier;
import musee.Salle;
import musee.SalleDExposition;

public class MuseeFichierTest {

    private File tempFile;
    private Musee museeFichier;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("museetest", ".txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("SALLE salle1\n");
            writer.write("SALLE salle2\n");
            writer.write("ARTEFACT salle1 PEINTURE\n");
            writer.write("ARTEFACT salle2 VASE\n");
            writer.write("LIEN salle1 salle2\n");
            // Connect to entree and sortie to make a valid graph
            writer.write("LIEN entree salle1\n");
            writer.write("LIEN salle2 sortie\n");
        }
        museeFichier = new MuseeFichier(tempFile.getAbsolutePath());
    }

    @After
    public void tearDown() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    public void testConstruireMuseeFichier() {
        museeFichier.construire();
        
        Salle salle1 = museeFichier.getSalle("salle1");
        assertNotNull(salle1);
        assertTrue(salle1 instanceof SalleDExposition);
        
        Salle salle2 = museeFichier.getSalle("salle2");
        assertNotNull(salle2);
        assertTrue(salle2 instanceof SalleDExposition);
        
        // Test objets
        Artefact[] objets1 = salle1.getObjets();
        assertEquals(1, objets1.length);
        assertEquals(Artefact.PEINTURE, objets1[0]);
        
        Artefact[] objets2 = salle2.getObjets();
        assertEquals(1, objets2.length);
        assertEquals(Artefact.VASE, objets2[0]);
        
        // Test voisins
        assertTrue(salle1.estVoisin(salle2));
        assertTrue(salle2.estVoisin(salle1));

        Salle entree = museeFichier.getEntree();
        assertTrue(entree.estVoisin(salle1));
        
        Salle sortie = museeFichier.getSortie();
        assertTrue(sortie.estVoisin(salle2));
    }
}
