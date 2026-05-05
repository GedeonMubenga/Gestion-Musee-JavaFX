package musee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MuseeFichier extends Musee {
    private String filepath;

    public MuseeFichier(String filepath) {
        super();
        this.filepath = filepath;
    }

    @Override
    public void construire() {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split("\\s+");
                String command = parts[0];

                switch (command) {
                    case "SALLE":
                        if (parts.length >= 2) {
                            String nom = parts[1];
                            SalleDExposition salle = new SalleDExposition(nom);
                            ajouterSalle(salle);
                        }
                        break;
                    case "ARTEFACT":
                        if (parts.length >= 3) {
                            String nomSalle = parts[1];
                            String nomArtefact = parts[2];
                            Salle s = getSalle(nomSalle);
                            if (s instanceof SalleDExposition) {
                                ((SalleDExposition) s).ajouterArtefact(Artefact.valueOf(nomArtefact));
                            }
                        }
                        break;
                    case "LIEN":
                        if (parts.length >= 3) {
                            String salle1 = parts[1];
                            String salle2 = parts[2];
                            Salle s1 = getSalle(salle1);
                            Salle s2 = getSalle(salle2);
                            if (s1 != null && s2 != null) {
                                s1.ajouterVoisin(s2);
                            }
                        }
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
