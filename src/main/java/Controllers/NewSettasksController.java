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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import main.java.Classes.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewSettasksController implements Initializable {

    protected Utilisateur user = new Utilisateur();

    @FXML
    private CheckBox blokedchec;

    @FXML
    private ChoiceBox<String> categorylist;

    @FXML
    private ChoiceBox<String> projectList;

    @FXML
    private DatePicker deadlineTask;

    @FXML
    private Pane newTaskspane;

    @FXML
    private ChoiceBox<Priorite> prioritylist;


    @FXML
    private TextField taskDuration;

    @FXML
    private TextField taskName;

    private ArrayList<Task> tasks = new ArrayList<>();

    @FXML
    private ChoiceBox<String> typeTask;

    private int frqnc =0 ;
    private boolean getFrqn = false;
    boolean saveTasks = false ;

    @FXML
    void addAnewTask(ActionEvent event) {
        String nameTask = taskName.getText();
        int duration = Integer.parseInt(taskDuration.getText());
        int minutes = duration % 60;
        int hours = duration / 60;
        LocalTime duree = LocalTime.of(hours, minutes);
        String categoryName = categorylist.getSelectionModel().getSelectedItem().toString();
        Category category = null;
        if (categoryName != null) {
            category = Category.findCategoryByName(categoryName);
        }
        boolean isBlocked = blokedchec.isSelected();
        Priorite priorite = prioritylist.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = deadlineTask.getValue();
        String type = typeTask.getSelectionModel().getSelectedItem().toString();
        Task task ;
        if (type.equals("Simple")) {
            task = new TaskSimple(nameTask, duree, priorite, selectedDate, category, EtatRealisation.IN_PROGRESS, isBlocked);
            tasks.add(task);
        } else if (type.equals("Périodique")) {
            while (!getFrqn) {
                // Show a dialog to prompt the user for the frequency
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Frequency");
                dialog.setHeaderText(null);
                dialog.setContentText("Please enter the frequency:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    try {
                        frqnc = Integer.parseInt(result.get());
                        getFrqn = true;
                    } catch (NumberFormatException e) {
                        // Invalid frequency entered, show an error message
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Invalid frequency. Please enter a valid integer value.");
                        alert.showAndWait();
                    }
                } else {
                    // Frequency dialog was canceled, return without saving the task
                    return;
                }
            }
            //periodiquePane.setVisible(true);
            task = new TaskSimple(nameTask, duree, priorite, selectedDate, category, EtatRealisation.IN_PROGRESS, isBlocked, frqnc);
            tasks.add(task);
        } else {
            task = new TaskDecom(nameTask, duree, priorite, selectedDate, category, EtatRealisation.IN_PROGRESS, isBlocked);
            tasks.add(task);
        }

        if (projectList.getSelectionModel().getSelectedItem() != null) {
            String projectName = projectList.getSelectionModel().getSelectedItem().toString();
            for (Projet projet1 : user.getProjets()) {
                if (projet1.getNom() == projectName) {
                    projet1.addTask(task);
                }
            }
        }
        // Reset input fields and choice boxes
        taskName.setText("");
        taskDuration.setText("");
        categorylist.getSelectionModel().clearSelection();
        blokedchec.setSelected(false);
        prioritylist.getSelectionModel().clearSelection();
        deadlineTask.setValue(null);
        // Reset typeTask choice box
        typeTask.setValue(null);
    }

    @FXML
    void closeNewtasks(ActionEvent event) {
        newTaskspane.setVisible(false);
    }

    @FXML
    void saveTasksAuto(ActionEvent event) {
        saveTasks = true;
        String nameTask = taskName.getText();
        int duration = Integer.parseInt(taskDuration.getText());
        int minutes = duration % 60;
        int hours = duration / 60;
        LocalTime duree = LocalTime.of(hours, minutes);
        String categoryName = categorylist.getSelectionModel().getSelectedItem().toString();
        Category category = null;
        if (categoryName != null) {
            category = Category.findCategoryByName(categoryName);
        }
        boolean isBlocked = blokedchec.isSelected();
        Priorite priorite = prioritylist.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = deadlineTask.getValue();
        String type = typeTask.getSelectionModel().getSelectedItem().toString();
        Task task ;
        if (type.equals("Simple")) {
            task = new TaskSimple(nameTask, duree, priorite, selectedDate, category, EtatRealisation.IN_PROGRESS, isBlocked);
        } else if (type.equals("Périodique")) {
            //periodiquePane.setVisible(true);
            while (!getFrqn) {
                // Show a dialog to prompt the user for the frequency
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Frequency");
                dialog.setHeaderText(null);
                dialog.setContentText("Please enter the frequency:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    try {
                        frqnc = Integer.parseInt(result.get());
                        getFrqn = true;
                    } catch (NumberFormatException e) {
                        // Invalid frequency entered, show an error message
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Invalid frequency. Please enter a valid integer value.");
                        alert.showAndWait();
                    }
                } else {
                    // Frequency dialog was canceled, return without saving the task
                    return;
                }
            }
            task = new TaskSimple(nameTask, duree, priorite, selectedDate, category, EtatRealisation.IN_PROGRESS, isBlocked, frqnc);
        } else {
            task = new TaskDecom(nameTask, duree, priorite, selectedDate, category, EtatRealisation.IN_PROGRESS, isBlocked);
            newTaskspane.setVisible(false);
        }

        if (projectList.getSelectionModel().getSelectedItem() != null) {
            String projectName = projectList.getSelectionModel().getSelectedItem().toString();
            for (Projet projet1 : user.getProjets()) {
                if (projet1.getNom() == projectName) {
                    projet1.addTask(task);
                }
            }
        }
        newTaskspane.setVisible(false);
        tasks.add(task);
        boolean planifier = user.getCalendrier().planifierAuto(tasks, user.getDureeMinCrenau());
        if (planifier == false) {
            showAlert("The task was not scheduled and has been added to the list of unscheduled tasks.");
        }
        UserData.setUser(user);
        newTaskspane.setVisible(false);
        UserData.setUser(user);
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
        ObservableList<String> options = FXCollections.observableArrayList("Simple", "Decomposable","Périodique");
        typeTask.setItems(options);
        LocalDate startDate = user.getCalendrier().getPeriode().getDebut();
        LocalDate endDate = user.getCalendrier().getPeriode().getFin();
        deadlineTask.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(startDate) || item.isAfter(endDate)) {
                    setDisable(true); // Disable dates outside the allowed period
                    setStyle("-fx-background-color: #a30000;"); // Optionally, apply a different style to disabled dates
                }
            }
        });

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
