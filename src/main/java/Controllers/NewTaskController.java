package main.java.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import main.java.Classes.Category;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import main.java.Classes.*;

public class NewTaskController implements Initializable {
    @FXML
    private CheckBox blokedchec;

    @FXML
    private ChoiceBox<String> categorylist;

    @FXML
    private Pane newTaskpane;

    @FXML
    private ChoiceBox<Priorite> prioritylist;

    @FXML
    private TextField taskDuration;

    @FXML
    private TextField taskName;

    protected Utilisateur user = new Utilisateur();

    private Creneau creneauSelected;

    @FXML
    private ChoiceBox<String> projectList;


    @FXML
    void closeNewtask(ActionEvent event) {
        newTaskpane.setVisible(false);

    }

    @FXML
    void saveTacheManuelle(ActionEvent event) { //C'est une tache simple
        String nameTask = taskName.getText();
        int duration = Integer.parseInt(taskDuration.getText());
        int minutes = duration % 60;
        int hours = duration / 60;
        LocalTime localTime = LocalTime.of(hours, minutes);
        String categoryName = categorylist.getSelectionModel().getSelectedItem().toString();
        Category category = null;
        if (categoryName != null) {
            category = Category.findCategoryByName(categoryName);
        }
        boolean isBlocked = blokedchec.isSelected();
        Priorite priorite = prioritylist.getSelectionModel().getSelectedItem();
        Task task = new TaskSimple(nameTask, localTime, priorite,user.getCalendrier().getPeriode().getFin(), category, EtatRealisation.IN_PROGRESS, isBlocked);
        boolean planifier = user.getCalendrier().planTacheManuel(user.getDureeMinCrenau(), task, creneauSelected);
        if (!planifier) {
            user.getCalendrier().getTachesNonProg().add(task);
            showAlert("The task was not scheduled and has been added to the list of unscheduled tasks.");
        }
        if (projectList.getSelectionModel().getSelectedItem() != null) {
            String projectName = projectList.getSelectionModel().getSelectedItem().toString();
                for (Projet projet1 : user.getProjets()) {
                    if (projet1.getNom() == projectName) {
                        projet1.addTask(task);
                    }
                }
            }
            UserData.setUser(user);
            newTaskpane.setVisible(false);
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = UserData.getUser();
        Category.showCategories(categorylist);
        // Convert the ArrayList to an ObservableList
        ObservableList<String> observableList = FXCollections.observableArrayList(user.projectsName());
        // Set the items in the ChoiceBox
        projectList.setItems(observableList);
        prioritylist.getItems().setAll(Priorite.values());
        creneauSelected=UserData.getCreneau();
    }

    void showAlert(String message) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        Label label = new Label(message);
        label.setFont(Font.font("Calibri", 16));
        label.setTextFill(Color.WHITE);
        StackPane stackPane = new StackPane(label);
        stackPane.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(5), Insets.EMPTY)));
        stackPane.setPadding(new Insets (10));

        Scene scene = new Scene(stackPane);
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                stage.close();
            }
        });
        stage.setScene(scene);
        stage.show();
        // Close the alert after 2 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), evt -> stage.close()));
        timeline.play();
    }

}
