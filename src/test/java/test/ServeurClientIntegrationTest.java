package test;

import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import NetworkClient;
import musee.Musee;
import serveur.ServeurMusee;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class ServeurClientIntegrationTest {

    private static Thread serverThread;
    private static File tempConfig;
    private static final int TEST_PORT = 8181;

    @BeforeClass
    public static void setUpServer() throws Exception {
        // Create a dummy config file
        tempConfig = File.createTempFile("musee_integration", ".txt");
        try (FileWriter writer = new FileWriter(tempConfig)) {
            writer.write("SALLE s1\n");
            writer.write("SALLE s2\n");
            writer.write("ARTEFACT s1 PEINTURE\n");
            writer.write("LIEN s1 s2\n");
            writer.write("LIEN entree s1\n");
            writer.write("LIEN s2 sortie\n");
        }

        // Run server in a separate thread
        serverThread = new Thread(() -> {
            ServeurMusee.main(new String[] { tempConfig.getAbsolutePath(), String.valueOf(TEST_PORT) });
        });
        serverThread.setDaemon(true);
        serverThread.start();

        // Wait a bit for server to start
        Thread.sleep(1000);
    }

    @AfterClass
    public static void tearDownServer() {
        if (serverThread != null) {
            serverThread.interrupt();
        }
        if (tempConfig != null && tempConfig.exists()) {
            tempConfig.delete();
        }
    }

    @Test
    public void testGetMap() throws Exception {
        NetworkClient client = new NetworkClient("localhost", TEST_PORT);
        Map<String, ArrayList<String>> map = client.getMap();
        assertNotNull(map);
        assertTrue(map.containsKey("entree"));
        assertTrue(map.containsKey("s1"));
        assertTrue(map.containsKey("s2"));
    }

    @Test
    public void testGetMusee() throws Exception {
        NetworkClient client = new NetworkClient("localhost", TEST_PORT);
        Musee musee = client.getMusee();
        assertNotNull(musee);
        assertNotNull(musee.getEntree());
        assertNotNull(musee.getSalle("s1"));
    }

    @Test
    public void testGetArtifactsVaidPlan() throws Exception {
        NetworkClient client = new NetworkClient("localhost", TEST_PORT);
        ArrayList<String> plan = new ArrayList<>(Arrays.asList("entree", "s1", "s2", "sortie"));
        ArrayList<String> artifacts = client.getArtifacts(plan);
        assertNotNull(artifacts);
        assertEquals(1, artifacts.size()); // PEINTURE in s1
        assertTrue(artifacts.get(0).contains("PEINTURE"));
    }

    @Test
    public void testGetArtifactsInvalidPlan() throws Exception {
        NetworkClient client = new NetworkClient("localhost", TEST_PORT);
        // "s2" to "entree" is invalid as they are not neighbors (entree -> s1 -> s2 ->
        // sortie)
        ArrayList<String> plan = new ArrayList<>(Arrays.asList("entree", "s2"));
        ArrayList<String> artifacts = client.getArtifacts(plan);
        assertNotNull(artifacts);
        assertEquals(0, artifacts.size()); // should return empty for invalid plan
    }
}
