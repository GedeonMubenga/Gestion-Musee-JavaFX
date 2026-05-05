package client;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

import musee.Musee;

public class PlannerView {

    private BorderPane root;
    private ClientGUI mainApp;
    private Properties labels;
    private NetworkClient networkClient;

    private Map<String, ArrayList<String>> museumMap;
    private ArrayList<String> currentPlan = new ArrayList<>();

    private ComboBox<String> comboNeighbors;
    private ListView<String> listPlan;
    private ListView<String> listArtifacts;
    private ImageView imageViewMap;
    private String currentDotCode;
    private CheckBox chkFreeform;
    private Button btnVirtualTour;
    private Musee museeComplet;

    public PlannerView(ClientGUI mainApp, NetworkClient networkClient) {
        this.mainApp = mainApp;
        this.labels = mainApp.getLabels();
        this.networkClient = networkClient;

        root = new BorderPane();
        buildUI();
        loadMap();
    }

    private void buildUI() {
        imageViewMap = new ImageView();
        imageViewMap.setFitWidth(400);
        imageViewMap.setFitHeight(400);
        imageViewMap.setPreserveRatio(true);

        comboNeighbors = new ComboBox<>();
        Button btnAdd = new Button(labels.getProperty("btn.addStep", "Add Step"));
        Button btnClear = new Button(labels.getProperty("btn.clearPlan", "Clear Plan"));
        Button btnValidate = new Button(labels.getProperty("btn.validatePlan", "Validate Plan"));
        Button btnSave = new Button(labels.getProperty("btn.savePlan", "Save Plan to File"));
        TextField ipServerField = new TextField("localhost");
        TextField portServerField = new TextField("8080");
        Button btnConnect = new Button(labels.getProperty("btn.connect","Connect to Server"));

        btnVirtualTour = new Button(labels.getProperty("btn.virtualTour", "Virtual Tour"));
        btnVirtualTour.setDisable(true);

        listPlan = new ListView<>();
        listPlan.setPrefHeight(150);
        listArtifacts = new ListView<>();
        listArtifacts.setPrefHeight(150);

        // Layouts
        comboNeighbors.setMinWidth(150);
        btnAdd.setMinWidth(120);
        btnClear.setMinWidth(120);

        Button btnSaveDot = new Button(labels.getProperty("btn.saveDot", "Save DOT File"));
        btnSaveDot.setMinWidth(120);

        chkFreeform = new CheckBox(labels.getProperty("chk.freeform", "Freeform Planning"));
        chkFreeform.setOnAction(e -> updatePlanView());
        
        HBox serverControls = new HBox(10,
                new Label(labels.getProperty("label.serverIP", "Server IP")), ipServerField,
                new Label(labels.getProperty("label.serverPort", "Port")), portServerField,
                btnConnect);
        serverControls.setPadding(new Insets(0, 0, 10, 0));

        VBox leftPane = new VBox(10, new Label(labels.getProperty("label.museumMap", "Museum Map")), imageViewMap,
                btnSaveDot);
        leftPane.setPadding(new Insets(10));

        HBox controls = new HBox(10, comboNeighbors, btnAdd, chkFreeform);
        HBox planButtons = new HBox(10, btnClear, btnSave);
        HBox actionButtons = new HBox(10, btnValidate, btnVirtualTour);
        VBox centerPane = new VBox(10,
                serverControls,
                new Label(labels.getProperty("label.buildPlan", "Build Visit Plan:")), controls, planButtons, listPlan,
                actionButtons,
                new Label(labels.getProperty("label.artifactsView", "Artifacts to View:")), listArtifacts);
        centerPane.setPadding(new Insets(10));

        root.setLeft(leftPane);
        root.setCenter(centerPane);

        // Event Handlers
        btnAdd.setOnAction(e -> {
            String selected = comboNeighbors.getValue();
            if (selected != null) {
                currentPlan.add(selected);
                updatePlanView();
            }
        });

        btnClear.setOnAction(e -> {
            currentPlan.clear();
            listArtifacts.getItems().clear();
            btnVirtualTour.setDisable(true);
            updatePlanView();
        });

        btnValidate.setOnAction(e -> {
            // TODO: compléter cet event handler
        	new Thread(() -> {
        	validatePlanAndFetchArtifacts();
        }).start();
        
        }); 
        
        // ajputer ici 
       btnConnect.setOnAction(e -> {
    	   String ip = ipServerField.getText();
    	   String portTexte = portServerField.getText();
    	   
    	   new Thread(() -> {
    		   try {
    			   int port = Integer.parseInt(portTexte);
    			   
    			   networkClient.setHost(ip);
    			   networkClient.setPort(port);
    			   
    			   Map<String, ArrayList<String>> map = networkClient.getMap();
    			   
    			   Platform.runLater(() -> {
    				   museumMap = map;
    				   updatePlanView();
    				   btnConnect.setDisable(true);
    			   });
    			   
    		   } catch (Exception ex) {
    			   Platform.runLater(() -> {
    				   mainApp.showAlert("Erreur de connexion","Impossible de se connecter au serveur" + ip + ":" + portTexte);
    			   });
    			   
    		   }
    	   
       }).start();
        	
   });    	
        btnSave.setOnAction(e -> savePlan());
        btnSaveDot.setOnAction(e -> saveDotFile());
        btnVirtualTour.setOnAction(e -> {
            if (museeComplet != null) {
                mainApp.showVirtualTourView(museeComplet, currentPlan, imageViewMap);
            } else {
                mainApp.showAlert(labels.getProperty("alert.loading.title", "Loading"),
                        labels.getProperty("alert.loading.content", "Musee data is still loading from server..."));
            }
        });
    }

    private void loadMap() {
        new Thread(() -> {
            try {
                Map<String, ArrayList<String>> map = networkClient.getMap();
                Platform.runLater(() -> {
                    this.museumMap = map;
                    updatePlanView();
                    renderMapImage();
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> mainApp.showAlert(
                        labels.getProperty("alert.connectionError.title", "Connection Error"),
                        labels.getProperty("alert.connectionError.map", "Could not connect to server to load map.")));
            }
        }).start();
    }

    private void validatePlanAndFetchArtifacts() {
        if (currentPlan.isEmpty()) {
            mainApp.showAlert(labels.getProperty("alert.error.title", "Error"),
                    labels.getProperty("alert.error.emptyPlan", "Plan is empty!"));
            return;
        }

        new Thread(() -> {
            try {
                ArrayList<String> artifacts = networkClient.getArtifacts(currentPlan);
                Platform.runLater(() -> {
                    listArtifacts.getItems().clear();
                    if (artifacts.isEmpty()) {
                        listArtifacts.getItems()
                                .add(labels.getProperty("alert.error.invalidPlan", "Plan Invalid or No Artifacts!"));
                        btnVirtualTour.setDisable(true);
                    } else {
                        listArtifacts.getItems().addAll(artifacts);
                        btnVirtualTour.setDisable(false);
                        fetchMuseeComplet(); // prepare musee for Virtual Tour
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> mainApp.showAlert(
                        labels.getProperty("alert.connectionError.title", "Connection Error"),
                        labels.getProperty("alert.connectionError.artifacts", "Could not fetch artifacts.")));
            }
        }).start();
    }

    private void fetchMuseeComplet() {
        new Thread(() -> {
            try {
                Musee m = networkClient.getMusee();
                Platform.runLater(() -> {
                    this.museeComplet = m;
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void savePlan() {
        if (currentPlan.isEmpty())
            return;
        FileChooser fc = new FileChooser();
        fc.setTitle(labels.getProperty("dialog.savePlan.title", "Save Plan de visite"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fc.showSaveDialog(mainApp.getPrimaryStage());
        if (file != null) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                for (String step : currentPlan) {
                    pw.println(step);
                }
            } catch (IOException ex) {
                mainApp.showAlert(labels.getProperty("alert.error.title", "Error"),
                        labels.getProperty("alert.error.savePlanFailed", "Could not save file."));
            }
        }
    }

    private void saveDotFile() {
        if (currentDotCode == null || currentDotCode.isEmpty()) {
            mainApp.showAlert(labels.getProperty("alert.error.title", "Error"),
                    labels.getProperty("alert.error.noGraph", "No graph available to save."));
            return;
        }
        FileChooser fc = new FileChooser();
        fc.setTitle(labels.getProperty("dialog.saveDot.title", "Save DOT File"));
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("DOT Files", "*.dot"));
        File file = fc.showSaveDialog(mainApp.getPrimaryStage());
        if (file != null) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                pw.println(currentDotCode);
            } catch (IOException ex) {
                mainApp.showAlert(labels.getProperty("alert.error.title", "Error"),
                        labels.getProperty("alert.error.saveDotFailed", "Could not save DOT file."));
            }
        }
    }

    public void updatePlanView() {
        listPlan.getItems().clear();
        listPlan.getItems().addAll(currentPlan);

        comboNeighbors.getItems().clear();
        if (museumMap != null) {
            if (currentPlan.isEmpty() || (chkFreeform != null && chkFreeform.isSelected())) {
                comboNeighbors.getItems().addAll(museumMap.keySet());
            } else {
                String lastRoom = currentPlan.get(currentPlan.size() - 1);
                ArrayList<String> neighbors = museumMap.get(lastRoom);
                if (neighbors != null) {
                    comboNeighbors.getItems().addAll(neighbors);
                }
            }
            if (!comboNeighbors.getItems().isEmpty()) {
                comboNeighbors.getSelectionModel().selectFirst();
            }
        }
    }

    private void renderMapImage() {
        try {
            StringBuilder graph = new StringBuilder("graph{");
            Set<String> drawn = new HashSet<>();

            for (String nom : museumMap.keySet()) {
                ArrayList<String> voisins = museumMap.get(nom);
                if (voisins == null || voisins.isEmpty()) {
                    graph.append(nom).append(";");
                } else {
                    for (String v : voisins) {
                        String edge1 = nom + "--" + v;
                        String edge2 = v + "--" + nom;
                        if (!drawn.contains(edge1) && !drawn.contains(edge2)) {
                            graph.append(nom).append("--").append(v).append(";");
                            drawn.add(edge1);
                        }
                    }
                }
            }
            graph.append("}");
            currentDotCode = graph.toString();
            String encoded = URLEncoder.encode(graph.toString(), "UTF-8");
            String url = "https://quickchart.io/graphviz?format=png&graph=" + encoded;
            Image image = new Image(url, true);
            imageViewMap.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BorderPane getView() {
        return root;
    }

    public ImageView getImageViewMap() {
        return imageViewMap;
    }

    public void reattachMap(javafx.scene.Node mapNode) {
        if (mapNode instanceof ImageView) {
            this.imageViewMap = (ImageView) mapNode;
            VBox left = (VBox) root.getLeft();
            if (!left.getChildren().contains(mapNode)) {
                left.getChildren().add(1, mapNode);
            }
        }
    }
}