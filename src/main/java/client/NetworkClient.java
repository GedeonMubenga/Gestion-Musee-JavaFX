package client;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import musee.Musee;

public class NetworkClient {
    private String host;
    private int port;

    public NetworkClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
    
    // difference ici 
    public void setHost(String host) {
    	this.host = host;
    }
    public void setPort(int port) {
    	this.port = port;
    }
    @SuppressWarnings("unchecked")
    public Map<String, ArrayList<String>> getMap() throws Exception {
        try (Socket socket = new Socket(host, port);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            oos.writeObject("GET_MAP");
            return (Map<String, ArrayList<String>>) ois.readObject();
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<String> getArtifacts(List<String> currentPlan) throws Exception {
        // TODO: implémenter la méthode getArtifacts
    	try (Socket socket = new Socket(host, port);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {

            oos.writeObject("GET_ARTEFACTS");
            oos.writeObject(currentPlan);
            return (ArrayList<String>) ois.readObject();
    }
    }	

    public Musee getMusee() throws Exception {
        try (Socket socket = new Socket(host, port);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
            oos.writeObject("GET_MUSEE");
            return (Musee) ois.readObject();
        }
    }
}

