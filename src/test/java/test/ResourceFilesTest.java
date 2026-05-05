package test;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ResourceFilesTest {

    @Test
    public void testEttiquettesPropertiesExists() throws Exception {
        Properties labels = new Properties();
        try (InputStream is = getClass().getResourceAsStream("/ettiquettes_fr.properties")) {
            assertNotNull("The properties file should exist in the resources folder", is);
            labels.load(new InputStreamReader(is, StandardCharsets.UTF_8));
        }
        
        assertFalse("Labels should not be empty", labels.isEmpty());
        // Test arbitrary key if we know it exists, otherwise just check size > 0
        assertTrue("Expected non-zero loaded property size", labels.size() > 0);
    }
}
