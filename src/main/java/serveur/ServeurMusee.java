package serveur;

import java.io.*;
import java.net.*;
import java.util.*;
import musee.*;
import tourisme.PlanDeVisite;

public class ServeurMusee {
    public static void main(String[] args) {
        int port = 8080;
        String configPath = "pointe_a_calliere.txt";

        if (args.length > 0 && !args[0].isEmpty())
            configPath = args[0];
        if (args.length > 1)
            port = Integer.parseInt(args[1]);

        MuseeFichier musee = new MuseeFichier(configPath);
        musee.construire();

        System.out.println("== Web Serveur Musee started on port " + port + " ==");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Nouveau client connecté : " + client.getInetAddress());
                new Thread(new ClientHandler(client, musee)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private Musee musee;

        public ClientHandler(Socket socket, Musee musee) {
            this.socket = socket;
            this.musee = musee;
        }

        @Override
        public void run() {
            try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

                String requete = (String) ois.readObject();

                if ("GET_MAP".equals(requete)) {
                    Map<String, ArrayList<String>> plan = musee.getAdjacencyList();
                    oos.writeObject(plan);
                    System.out.println("Adjacency map sent.");
                } else if ("GET_MUSEE".equals(requete)) {
                    oos.writeObject(musee);
                    System.out.println("Full Musee object sent.");
                } else if ("GET_ARTEFACTS".equals(requete)) {
                    // Créer un plan de visite à partir du plan du client.
                    // Valider le plan de visite proposé.
                    // Si le plan est valide, récupérer les artéfacts correspondants.
                    // Renvoyer la liste des artéfacts au client (vide si le plan est invalide).
                    @SuppressWarnings("unchecked")
                    List<String> userPlan = (List<String>) ois.readObject();
                    PlanDeVisite pdv = new PlanDeVisite();
                    for (String step : userPlan) {
                        pdv.ajouterEtape(step);
                    }
                    ArrayList<String> artifacts = new ArrayList<>();
                    if (pdv.valider(musee)) {
                        for (String step : userPlan) {
                            Salle s = musee.getSalle(step);
                            for (Artefact a : s.getObjets()) {
                                artifacts.add(a.name() + " in " + s.getNom());
                            }
                        }
                    } else {
                        System.out.println("Invalid plan received.");
                    }
                    oos.writeObject(artifacts);
                    System.out.println("Artifacts sent: " + artifacts.size());
                }

            } catch (Exception e) {
                System.err.println("Client handler exception: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
