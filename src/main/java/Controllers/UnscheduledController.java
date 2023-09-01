package main.java.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import main.java.Classes.*;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class UnscheduledController implements Initializable {
    @FXML
    private Pane panePage;

    @FXML
    private HBox tasksHbox;

    protected Utilisateur user = new Utilisateur();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = UserData.getUser();
        createTaskForms();
    }

    public void createTaskForms() {
        tasksHbox.getChildren().clear();
        tasksHbox.setSpacing(10);
        HashSet<String> taskNames = new HashSet<>(); // HashSet to store unique task names
        if (!user.getCalendrier().getTachesNonProg().isEmpty()) {
        for (Task task :user.getCalendrier().getTachesNonProg()) {
            String taskName = task.getNom(); // Get the task name
            if (!taskNames.contains(taskName)) { // Check if the task name already exists
                VBox vBoxTach = afficheTache(task);
                vBoxTach.setMinWidth(200); // Set the minimum width for the VBox
                tasksHbox.getChildren().add(vBoxTach);
                taskNames.add(taskName); // Add the task name to the HashSet
                        }
                    }
                }
    }





    private VBox afficheTache(Task task) {
        VBox taskBox = new VBox();
        taskBox.setStyle("-fx-border-color: white;-fx-background-color:  #fae6f3;-fx-border-radius: 15px;-fx-background-radius: 15px; -fx-padding: 10px");
        // Name label and text field
        Label nameLabel = new Label("Name: ");
        TextField nameTextField = new TextField(task.getNom());
        nameTextField.setEditable(false);

        // Priority label and text field
        Label priorityLabel = new Label("Priority : ");
        TextField priorityTextField = new TextField(task.getPriorite().toString());
        priorityTextField.setEditable(false);

        // Deadline label and text field
        Label deadlineLabel = new Label("Deadline: ");
        TextField deadlineTextField = new TextField(task.getDeadline().toString());
        deadlineTextField.setEditable(false);

        // task status label and text field
        Label statusLabel = new Label("Task status: ");
        TextField statusTextField = new TextField("Unscheduled");// task status label and text field
        statusTextField.setEditable(false);
        // Duree min label and text field
        Label dureeLabel = new Label("Minimum duration: ");
        TextField dureeTextField = new TextField(task.getDureeTache().toString());
        dureeTextField.setEditable(false);



        // Add all components to the taskBox
        taskBox.getChildren().addAll(
                nameLabel, nameTextField,
                deadlineLabel, deadlineTextField,
                priorityLabel, priorityTextField,
                dureeLabel,dureeTextField,
                statusLabel,statusTextField
                // Add more fields as needed
        );

        if (task instanceof TaskSimple)
        {
            if (((TaskSimple) task).getPeriodicite()!=0){
                Label periodique = new Label("periodic task with a frequency: ");
                TextField frquencyText = new TextField(Integer.toString(((TaskSimple) task).getPeriodicite()));
                taskBox.getChildren().addAll(periodique,frquencyText);

            }
        }
        return taskBox;
    }

    @FXML
    void clearTasks(ActionEvent event) {
        user.getCalendrier().getTachesNonProg().clear();
        showAlert("Unscheduled tasks have been deleted.");
        UserData.setUser(user);
        createTaskForms();
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
