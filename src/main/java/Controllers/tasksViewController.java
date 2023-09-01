package main.java.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.java.Classes.*;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class tasksViewController implements Initializable {

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

        for (Jour jour : user.getCalendrier().getJours()) {
            if (!jour.getCrenaux().isEmpty()) {
                for (Creneau creneau : jour.getCrenaux()) {
                    if (creneau.getEtatCrenau() == EtatCrenau.OCCUPE) {
                        String taskName = creneau.getTacheAllouee().getNom(); // Get the task name
                        if (!taskNames.contains(taskName)) { // Check if the task name already exists
                            VBox vBoxTach = afficheTache(creneau);
                            vBoxTach.setMinWidth(200); // Set the minimum width for the VBox
                            tasksHbox.getChildren().add(vBoxTach);
                            taskNames.add(taskName); // Add the task name to the HashSet
                        }
                    }
                }
            }
        }
    }



    private VBox afficheTache(Creneau creneau) {
        VBox taskBox = new VBox();
        Task task = creneau.getTacheAllouee();
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
        TextField statusTextField = new TextField(task.getEtatTask().toString());// task status label and text field
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

    }
