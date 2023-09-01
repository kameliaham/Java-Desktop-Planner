package main.java.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import main.java.Classes.*;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private ListView<String> projecctList;

    @FXML
    private Button saveCatbtn;

    protected Utilisateur user = new Utilisateur();

    @FXML
    private Pane paneCalendar;

    @FXML
    private Pane addCatpane;

    @FXML
    private ColorPicker catColor;

    @FXML
    private TextField catName;
    private boolean selectAddcat = false;
    @FXML
    private ListView<Category> categoriesList;

    @FXML
    private Pane paneTasks;



    public void showCalendar(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            AnchorPane root = loader.load();
            paneCalendar.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = UserData.getUser();
        showCalendar("../FXML/Calendar.fxml");
        Category.showCategories(categoriesList);
        if (!user.getProjets().isEmpty()) {
            ObservableList<String> observableList = FXCollections.observableArrayList(user.projectsName());
            // Set the items in the ChoiceBox
            projecctList.setItems(observableList);
        }
    }


    @FXML
    void addCategories(ActionEvent event) {
        if (selectAddcat==false)
        {
            addCatpane.setVisible(true);
            selectAddcat = true;
        }else {
            addCatpane.setVisible(false);
            selectAddcat = false ;
        }

    }
    @FXML
    void saveCatOnAction(ActionEvent event) {
        String nameCat = catName.getText();
        String selectedColor = catColor.getValue().toString();
        Category category = new Category(nameCat,selectedColor);
        Category.addCategory(category);
        Category.showCategories(categoriesList);
        addCatpane.setVisible(false);
        selectAddcat = false;
    }

    @FXML
    void addTaksOnaction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/NewSetOftasks.fxml"));
            Pane root = loader.load();
            paneTasks.getChildren().setAll(root);
            paneTasks.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void addTaskurgete(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/taskPriority.fxml"));
            Pane root = loader.load();
            paneTasks.getChildren().setAll(root);
            paneTasks.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
