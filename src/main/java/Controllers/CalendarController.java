package main.java.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import main.java.Classes.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CalendarController implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private Text year;


    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;


    @FXML
    private Pane newTaskpane;


    protected Utilisateur user = new Utilisateur();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newTaskpane.setVisible(false);
        user = UserData.getUser();
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void createCalendarAct(List<Creneau> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        VBox calendarActBox = new VBox();

        calendarActBox.setTranslateY((rectangleHeight / calendarActivities.size()) * 0.20);
        calendarActBox.setMaxWidth(rectangleWidth * 0.7);
        calendarActBox.setMaxHeight(rectangleHeight * 0.5);
        calendarActBox.setStyle("-fx-background-color: white; -fx-border-color: black");
        for (int k = 0; k < calendarActivities.size(); k++) {
            Text text = new Text(calendarActivities.get(k).getStartTime() + "-" + calendarActivities.get(k).getEndTime());
            calendarActBox.getChildren().add(text);
            Creneau creneau = calendarActivities.get(k);
            if (creneau.getEtatCrenau() == EtatCrenau.OCCUPE) {
                Color color = creneau.getTacheAllouee().getCategorie().returnColor();
                text.setFill(color);
            }
            text.setOnMouseClicked(mouseEvent -> {
                if (creneau.getEtatCrenau() == EtatCrenau.LIBRE) {
                    try {
                        UserData.setUser(user);
                        UserData.setCreneau(creneau);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/NewTask.fxml"));
                        Pane root = loader.load();
                        newTaskpane.getChildren().setAll(root);
                        newTaskpane.setVisible(true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            });
        }
        stackPane.getChildren().add(calendarActBox);
    }


    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        LocalDate startDate = user.getCalendrier().getPeriode().getDebut();
        LocalDate endDate = user.getCalendrier().getPeriode().getFin();
        //LocalDate startDate = LocalDate.of(2023, 5, 1);  // Example start date
        // LocalDate endDate = LocalDate.of(2023, 5, 15);

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        // List of time slots in a period
        Map<Integer, List<Creneau>> calendarActMap = getCalendarActivitiesMonth();

        int monthMaxDate = dateFocus.getMonth().maxLength();
        // Check for leap year
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();
                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.TRANSPARENT);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);
                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        date.setFont(Font.font("Arial", 14));
                        date.setFill(Color.BLACK);
                        double textTranslationY = -(rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);
                        LocalDate currentDateObj = LocalDate.of(dateFocus.getYear(), dateFocus.getMonthValue(), currentDate);
                        if (currentDateObj.isAfter(startDate.minusDays(1)) && currentDateObj.isBefore(endDate.plusDays(1))) {
                            List<Creneau> calendarActivities = calendarActMap.get(currentDate);
                            if ((calendarActivities != null)) {
                                createCalendarAct(calendarActivities, rectangleHeight, rectangleWidth, stackPane);
                            }
                            if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                                rectangle.setStroke(Color.web("#a30000"));
                            }
                            rectangle.setStroke(Color.web("#fae6f3"));
                        }
                        date.toFront();
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private Map<Integer, List<Creneau>> createCalendarMap(List<Creneau> calendarActivities) {
        Map<Integer, List<Creneau>> CalendarActMap = new HashMap<>();

        for (Creneau activity : calendarActivities) {
            int activityDate = activity.getDate().getDayOfMonth();
            if (!CalendarActMap.containsKey(activityDate)) {
                CalendarActMap.put(activityDate, List.of(activity));
            } else {
                List<Creneau> OldListByDate = CalendarActMap.get(activityDate);

                List<Creneau> newList = new ArrayList<>(OldListByDate);
                newList.add(activity);
                CalendarActMap.put(activityDate, newList);
            }
        }
        return CalendarActMap;
    }

    private Map<Integer, List<Creneau>> getCalendarActivitiesMonth() {
        List<Creneau> calendarActivities = new ArrayList<>();

        LocalDate startDate = user.getCalendrier().getPeriode().getDebut();
        LocalDate endDate = user.getCalendrier().getPeriode().getFin();

        for (Jour jour : user.getCalendrier().getJours()) {
            LocalDate day = jour.getDate(); // Get the date of the day
            // Get the existing time slots (crenaux) for the current day
            ArrayList<Creneau> crenaux = jour.getCrenaux();
            for (Creneau creneau : crenaux) {
                LocalTime startTime = creneau.getStartTime(); // Get the start time
                LocalTime endTime = creneau.getEndTime(); // Get the end time
                // Create a new Creneau object with the correct date
                Creneau updatedCreneau = new Creneau(startTime, day, endTime, creneau.getEtatCrenau(), creneau.getTacheAllouee());
                calendarActivities.add(updatedCreneau);
            }
        }
        return createCalendarMap(calendarActivities);
    }

    @FXML
    void changePeriodeOnaction(ActionEvent event) {
        LocalDate startDate = user.getCalendrier().getPeriode().getDebut();
        LocalDate endDate = user.getCalendrier().getPeriode().getFin();

        DatePicker datePer = new DatePicker();
        datePer.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item.isBefore(endDate)) {
                    setDisable(true); // Disable dates outside the allowed period
                    setStyle("-fx-background-color: #a30000;"); // Optionally, apply a different style to disabled dates
                }
            }
        });

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Change the end date of your Periode");
        alert.getDialogPane().setContent(datePer);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setResizable(true);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            LocalDate newEndDate = datePer.getValue();
            user.getCalendrier().getPeriode().setFin(newEndDate);
            LocalDate currentDate = endDate.plusDays(1); // Start from the day after the previous end date

            while (currentDate.isBefore(newEndDate) || currentDate.isEqual(newEndDate)) {
                // Create choice boxes for each day
                ChoiceBox<String> startTimeChoiceBox = new ChoiceBox<>();
                ChoiceBox<String> endTimeChoiceBox = new ChoiceBox<>();

                // Populate the choice boxes with time options for a 24-hour format with 30-minute intervals
                for (int hour = 0; hour < 24; hour++) {
                    for (int minute = 0; minute < 60; minute += 30) {
                        String time = String.format("%02d:%02d", hour, minute);
                        endTimeChoiceBox.getItems().add(time);
                        startTimeChoiceBox.getItems().add(time);
                    }
                }

                VBox content = new VBox(startTimeChoiceBox, endTimeChoiceBox);
                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Time Slots");
                alert1.setHeaderText("Set Your Available Time Slots of: " + currentDate);
                alert1.getDialogPane().setContent(content);
                Stage stage2 = (Stage) alert1.getDialogPane().getScene().getWindow();
                stage2.setResizable(true);
                Optional<ButtonType> result1 = alert1.showAndWait();
                if (result1.isPresent() && result1.get() == ButtonType.OK) {
                    Jour jour = new Jour(currentDate);
                    String startTime = startTimeChoiceBox.getValue();
                    String endTime = endTimeChoiceBox.getValue();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    LocalTime startTimeget = LocalTime.parse(startTime, formatter);
                    LocalTime finalTimeget = LocalTime.parse(endTime, formatter);
                    Creneau creneau = new Creneau(startTimeget,finalTimeget,jour.getDate());
                    if (creneau.testCreneau(user.getDureeMinCrenau())) {
                        jour.getCrenaux().add(creneau);
                    }
                    user.getCalendrier().getJours().add(jour);
                }
                // Move to the next day
                currentDate = currentDate.plusDays(1);
            }
        }
        calendar.getChildren().clear();
        drawCalendar();
        UserData.setUser(user);
    }

}

