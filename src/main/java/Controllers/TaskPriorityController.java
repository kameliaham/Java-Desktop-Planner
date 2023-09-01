package main.java.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import main.java.Classes.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class TaskPriorityController implements Initializable {

    @FXML
    private CheckBox blokedchec;

    @FXML
    private DatePicker deadlineTask;

    @FXML
    private Pane newTaskpane;


    @FXML
    private TextField taskDuration;

    @FXML
    private TextField taskName;


    @FXML
    private ChoiceBox<String> categorylist;


    @FXML
    private ChoiceBox<Priorite> prioritylist;



    protected Utilisateur user = new Utilisateur();

    @FXML
    private ChoiceBox<String> projectList;



    @FXML
    void closeNewtask(ActionEvent event) {
        newTaskpane.setVisible(false);
    }


    @FXML
    void saveTaskPriority(ActionEvent event) {
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
        LocalDate selectedDate = deadlineTask.getValue();
        Task task = new TaskSimple(nameTask, localTime, priorite,selectedDate, category, EtatRealisation.IN_PROGRESS, isBlocked);
        user.getCalendrier().PlanifierTacheUrgente(task,user.getDureeMinCrenau());
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

}
