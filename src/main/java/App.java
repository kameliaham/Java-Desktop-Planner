package main.java;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.java.Classes.MyDesktopPlanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;
import java.time.LocalDate;

import static javafx.application.Application.launch;


public class App extends Application {
    private MyDesktopPlanner planner;
    public static void main(String[] args) throws Exception {
     launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("FXML/LoginPage.fxml"));
            System.out.println("here");
            Scene scene = new Scene(root);
            System.out.println("here");
            primaryStage.setTitle("Desktop Planner");
            primaryStage.setScene(scene);
            primaryStage.show();
            // Set a window close event handler
        } catch (IOException e) {
            System.out.print("IO EXCEPTION");
        }
    }




}
