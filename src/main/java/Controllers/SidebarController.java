package main.java.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SidebarController implements Initializable {

    @FXML
    private Button historybtn;

    @FXML
    private Button homebtn;

    @FXML
    private Pane panePage;

    @FXML
    private Button plannerbtn;

    @FXML
    private Button statisbtn;


    @FXML
    void historybtnOnaction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/Homepage.fxml"));
            AnchorPane root = loader.load();
            panePage.getChildren().setAll(root);
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
        }
    }


    @FXML
    void homebtnClicked(ActionEvent event) {
        showPage("../FXML/Homepage.fxml");
    }


    @FXML
    void plannerbnOnAction(ActionEvent event) {
        showPage("../FXML/Project.fxml");
    }

    @FXML
    void statisbtnOnaction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/Statistics.fxml"));
            Pane root = loader.load();
            panePage.getChildren().setAll(root);
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
        }
    }

    public void showPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane root = loader.load();
            panePage.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void UnscheduledOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/UnscheduledTasks.fxml"));
            AnchorPane root = loader.load();
            panePage.getChildren().setAll(root);
        } catch (IOException e) {
            System.out.println("IO EXCEPTION");
        }

    }

    @FXML
    void projectsOnaction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/Project.fxml"));
            Pane root = loader.load();
            panePage.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void tasksOnclick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/Planner.fxml"));
            Pane root = loader.load();
            panePage.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        showPage("../FXML/Homepage.fxml");

    }
}
