package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Properties;

import musee.Musee;

public class ClientGUI extends Application {

    private Properties labels = new Properties();
    private NetworkClient networkClient;
    private Stage primaryStage;
    
    private PlannerView plannerView;
    private Scene plannerScene;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        
        try (InputStream is = getClass().getResourceAsStream("/ettiquettes_fr.properties")) {
            if (is != null) {
                labels.load(new InputStreamReader(is, StandardCharsets.UTF_8));
            } else {
                System.err.println("ettiquettes_fr.properties not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        primaryStage.setTitle(labels.getProperty("app.title", "Museum Visit Planner"));

        networkClient = new NetworkClient("localhost", 8080);

        showPlannerView();

        primaryStage.show();
    }
    
    public void showPlannerView() {
        if (plannerView == null) {
            plannerView = new PlannerView(this, networkClient);
            plannerScene = new Scene(plannerView.getView(), 900, 500);
        } else {
            // Re-attach the map if it was borrowed by the virtual tour view
            plannerView.reattachMap(plannerView.getImageViewMap());
        }
        
        primaryStage.setScene(plannerScene);
    }
    
    public void showVirtualTourView(Musee museeComplet, ArrayList<String> currentPlan, javafx.scene.Node imageViewMap) {
        VirtualTourView virtualTourView = new VirtualTourView(this, museeComplet, currentPlan, imageViewMap);
        Scene scene = new Scene(virtualTourView.getView(), 900, 500);
        primaryStage.setScene(scene);
    }

    public Properties getLabels() {
        return labels;
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
