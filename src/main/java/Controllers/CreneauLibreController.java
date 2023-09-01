package main.java.Controllers;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
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

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CreneauLibreController implements Initializable {

    protected Utilisateur user = new Utilisateur();
    private LocalDate date ;
    private Jour jour ;
    private MyDesktopPlanner myDesktopPlanner = new MyDesktopPlanner();

    boolean rep = false;

    private List<LocalDate> jours = new ArrayList<>();

    @FXML
    private Pane addCrenPane;

    @FXML
    private ChoiceBox<String> choiceCreneau;

    @FXML
    private Label dayText;

    @FXML
    private Label monthText;

    @FXML
    private Pane repetitsPane;

    @FXML
    private FlowPane flowpanrepeat;

    @FXML
    private ChoiceBox<String> endTimechoice;
    @FXML
    private ChoiceBox<String> startTimechoice;
    @FXML
    void addCreneau(ActionEvent event) {
        addCrenPane.setVisible(true);
    }

    @FXML
    void nextDay(ActionEvent event) {
        if (rep == false){
            user.getCalendrier().getJours().add(jour);}
        else {rep = false;}
        if (!jours.isEmpty()) {
                jours.remove(0);
        }
        if (!jours.isEmpty()) {
            date = jours.get(0); // Update the date object with the new date
            jour = new Jour(date);
            monthText.setText(date.getMonth().toString());
            dayText.setText(String.valueOf(date.getDayOfMonth()));
            choiceCreneau.getItems().clear();
        } else {
            for (Jour jour2:user.getCalendrier().getJours())
            {
                System.out.println("jour :"+jour2.getDate()+"creneaux");
                for (Creneau creneau:jour2.getCrenaux())
                {
                    System.out.println(creneau.getStartTime());
                    System.out.println(creneau.getEndTime());
                }
            }
            showAlert("All time slots for the specified period have been entered successfully.");
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
            try {
                UserData.setUser(user);
                Parent page = null;
                page = FXMLLoader.load(Controller.class.getResource("../FXML/Sidebar.fxml"));
                // Replace the main content area with the page
                Scene scene = new Scene(page);
                Stage stage = new Stage();
                stage.setOnCloseRequest(event2 -> {
                    // Create a confirmation dialog
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation");
                    alert.setHeaderText("Are you sure you want to close the scene?");
                    alert.setContentText("Any unsaved changes will be lost.");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        myDesktopPlanner.SauvegarderUilisateurs();
                    } else {
                        event2.consume();
                    }
                });
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void periodiqueCreneau(ActionEvent event) {
        repetitsPane.setVisible(true);
        flowpanrepeat.setPadding(new Insets(10));
        flowpanrepeat.setAlignment(Pos.CENTER);
        flowpanrepeat.setHgap(10); // Set horizontal gap between checkboxes
        flowpanrepeat.setVgap(10); // Set vertical gap between checkboxes
        flowpanrepeat.getChildren().clear();
        for (LocalDate jour : jours)
        {
            if (jour != date) {
                CheckBox checkBox = new CheckBox(jour.toString());
                flowpanrepeat.getChildren().add(checkBox);
            }
        }
    }

    @FXML
    void saverepeating(ActionEvent event) {
        user.getCalendrier().getJours().add(jour);
        rep = true;
        List<CheckBox> checkboxes = selectedCheckboxes();
        List<LocalDate> datesToRemove = new ArrayList<>();
        for (CheckBox checkBox : checkboxes) {
            String selectedDateStr = checkBox.getText();
            LocalDate selectedDate = LocalDate.parse(selectedDateStr);
            datesToRemove.add(selectedDate);
            Jour jour1 = new Jour(selectedDate);
            ArrayList<Creneau> creneaux = new ArrayList<>();
            for (Creneau creneau: jour.getCrenaux())
            {
                Creneau creneau1 = new Creneau(creneau.getStartTime(),creneau.getEndTime(),jour1.getDate());
                creneaux.add(creneau1);
            }
            jour1.setCrenaux(creneaux);
            user.getCalendrier().getJours().add(jour1);
        }
        // Remove the selected dates from the jours list
        jours.remove(0);
        jours.removeAll(datesToRemove);
        repetitsPane.setVisible(false);
        showAlert("The time slot has been successfully added!");
        if (!jours.isEmpty()) {
            date = jours.get(0); // Update the date object with the new date
            jour = new Jour(date);
            monthText.setText(date.getMonth().toString());
            dayText.setText(String.valueOf(date.getDayOfMonth()));
            choiceCreneau.getItems().clear();
        } else {
            showAlert("All time slots for the specified period have been entered successfully.");
        }
    }


    @FXML
    void saveuserTime(ActionEvent event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String startT = startTimechoice.getValue();
        String finalT = endTimechoice.getValue();
        LocalTime startTime = LocalTime.parse(startT, formatter);
        LocalTime finalTime = LocalTime.parse(finalT, formatter);
        Creneau creneau = new Creneau(startTime,finalTime,jour.getDate());
        System.out.println(jour.getDate());
        if (creneau.testCreneau(user.getDureeMinCrenau())) {
            boolean verif = jour.ajouterCreneau(startTime, finalTime);
            if (verif == false)
            {
                showAlert("A conflicting time slot already exists.");
                addCrenPane.setVisible(true);
                return;
            }
            addCrenPane.setVisible(false);
            String time = String.format(startT + "--" + finalT);
            choiceCreneau.getItems().add(time);
            choiceCreneau.getSelectionModel().selectLast();
            showAlert("The time slot has been successfully added!");
        }else {
            showAlert("Invalid time slot. Please make sure the end time is after the start time and the difference is at least "+user.getDureeMinCrenau()+" minutes");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = UserData.getUser();
        monthText.setText(user.getCalendrier().getPeriode().getDebut().getMonth().toString());
        dayText.setText(String.valueOf(user.getCalendrier().getPeriode().getDebut().getDayOfMonth()));
        date = user.getCalendrier().getPeriode().getDebut() ;
        jour = new Jour (date);
        jours = user.getCalendrier().getPeriode().generateDateRange();
        /**************** Add the time to the choice box ********************/
        // Populate the choice box with time options for a 24-hour format with 30-minute intervals
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                String time = String.format("%02d:%02d", hour, minute);
                endTimechoice.getItems().add(time);
                startTimechoice.getItems().add(time);
            }
        }
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


    private List<CheckBox> selectedCheckboxes() {
        List<CheckBox> checkboxes = new ArrayList<>();
        for (int i = 0; i < flowpanrepeat.getChildren().size(); i++) {
                if (flowpanrepeat.getChildren().get(i) instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) flowpanrepeat.getChildren().get(i);
                        if(checkBox.isSelected()) {
                        checkboxes.add(checkBox);
                    }
                }
        }
        return checkboxes;
    }
}
