package client;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import musee.Artefact;
import musee.Musee;
import musee.Salle;
import tourisme.Touriste;

import java.util.ArrayList;
import java.util.Properties;

public class VirtualTourView {
    
    private BorderPane root;
    private Properties labels;
    
    public VirtualTourView(ClientGUI mainApp, Musee museeComplet, ArrayList<String> currentPlan, javafx.scene.Node imageViewMap) {
        this.labels = mainApp.getLabels();
        
        root = new BorderPane();

        // Left Pane: Map and Plan
        VBox leftPane = new VBox(10, new Label(labels.getProperty("label.museumMap", "Museum Map")), imageViewMap, new Label(labels.getProperty("label.yourPlan", "Your Plan:")));
        ListView<String> planView = new ListView<>();
        planView.getItems().addAll(currentPlan);
        planView.setPrefHeight(150);
        leftPane.getChildren().add(planView);
        leftPane.setPadding(new Insets(10));

        // Right Pane: Room info and Next button
        Label lblRoom = new Label(labels.getProperty("label.currentRoom", "Current Room: "));
        ListView<String> roomArtifacts = new ListView<>();
        VBox topRight = new VBox(10, lblRoom, new Label(labels.getProperty("label.artifactsVisible", "Artifacts Visible:")), roomArtifacts);

        Button btnNext = new Button(labels.getProperty("btn.nextRoom", "Next Room"));
        Button btnReturn = new Button(labels.getProperty("btn.returnPlanner", "Return to Planner"));
        HBox botRight = new HBox(10, btnNext, btnReturn);

        VBox rightPane = new VBox(15, topRight, botRight);
        rightPane.setPadding(new Insets(10));

        root.setLeft(leftPane);
        root.setCenter(rightPane);

        Touriste touriste = new Touriste("Vous");
        
        // Setup updates
        Runnable updateView = () -> {
            Salle courante = touriste.getSalleCourante();
            if (courante != null) {
                lblRoom.setText(labels.getProperty("label.currentRoom", "Current Room: ") + courante.getNom());
                roomArtifacts.getItems().clear();
                for (Artefact a : courante.getObjets()) {
                    roomArtifacts.getItems().add(a.name());
                }
            } else {
                lblRoom.setText(labels.getProperty("tour.ended", "Visite terminée ou non commencée."));
                roomArtifacts.getItems().clear();
            }
        };

        // Start
        if (museeComplet != null) {
            touriste.entrer(museeComplet);
        }
        
        int[] currentTourIndex = new int[]{0};
        if (!currentPlan.isEmpty() && touriste.getSalleCourante() != null && currentPlan.get(0).equals(touriste.getSalleCourante().getNom())) {
            currentTourIndex[0] = 1; // advance if first step is already entrance
        }
        updateView.run();
        
        if (currentTourIndex[0] >= currentPlan.size()) {
            btnNext.setDisable(true);
        }

        btnNext.setOnAction(e -> {
            if (currentTourIndex[0] < currentPlan.size() && museeComplet != null) {
                String nextRoomName = currentPlan.get(currentTourIndex[0]);
                Salle prochaine = museeComplet.getSalle(nextRoomName);
                if (prochaine != null) {
                    touriste.seDeplacer(prochaine);
                }
                currentTourIndex[0]++;
                updateView.run();
            }
            if (currentTourIndex[0] >= currentPlan.size()) {
                btnNext.setDisable(true);
            }
        });

        btnReturn.setOnAction(e -> {
            // Remove map from leftPane so it can be re-added to main view
            leftPane.getChildren().remove(imageViewMap);
            mainApp.showPlannerView();
        });
    }
    
    public BorderPane getView() {
        return root;
    }
}
