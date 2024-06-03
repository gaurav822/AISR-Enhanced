package com.gauravdahal.ais.r.initial;

import database.DatabaseHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import static javafx.application.Application.launch;
import server.MultiThreadedServer;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;

   

    @Override
    public void start(Stage stage) throws IOException {
         primaryStage = stage;
        // Load the FXML file and get the root node
        Parent root = loadFXML("login");

        // Create the scene with the loaded root node
        scene = new Scene(root);

        // Set the scene to the stage
        stage.setScene(scene);

        // Adjust the stage size based on the root node's preferred size
        stage.setWidth(root.prefWidth(-1));
        stage.setHeight(root.prefHeight(-1));

        // Show the stage
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
                Parent root = loadFXML(fxml);

        scene.setRoot(root);
        // Adjust the stage size based on the new root node's preferred size
        primaryStage.setWidth(root.prefWidth(-1));
        primaryStage.setHeight(root.prefHeight(-1));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        
        new DatabaseHelper();
        // Start the server in a new thread
        new Thread(() -> {
            MultiThreadedServer server = new MultiThreadedServer();
            server.start();
        }).start();
        
        
        
        
        launch();
    }

}