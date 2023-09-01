package main.java.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.java.Classes.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class StatisticsController implements Initializable {

    protected Utilisateur user = new Utilisateur();

    @FXML
    private TextField excellentTextField;

    @FXML
    private TextField goodTextField;

    @FXML
    private TextArea jourRentableTextArea;

    @FXML
    private Button moyenneRendementButton;

    @FXML
    private TextField nbEncouragementTextField;

    @FXML
    private Button rendementButton;

    @FXML
    private TextArea tasksTextField;

    @FXML
    private Button tempsPasseCategorieButton;

    @FXML
    private TextField veryGoodTextField;

    private ArrayList<Creneau> creneauxNonlib = new ArrayList<>();

    Statistique stat = new Statistique(creneauxNonlib,user.getCalendrier().getPeriode());

    LocalDate startDate ;

    @FXML
    void rendementButtonOnAction(ActionEvent event) {
        System.out.println(creneauxNonlib);
        LocalDate startDate = LocalDate.now();
        System.out.println(startDate);
        double rendement = stat.calculrendementjournalier(startDate);

        // Create and configure the alert dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rendement Journalier");
        alert.setHeaderText(null);
        alert.setContentText("Votre rendement journalier est de : " + rendement);

        // Display the alert dialog
        alert.showAndWait();

    }

    @FXML
    void taskStatusAdd(ActionEvent event) {
        for (Creneau creneau : creneauxNonlib) {
            ChoiceBox<EtatRealisation> dialog = new ChoiceBox<>();
            dialog.getItems().setAll(EtatRealisation.values());
            dialog.setValue(creneau.getTacheAllouee().getEtatTask());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Set Task Status");
            alert.setHeaderText("Please enter the status of the task: " + creneau.getTacheAllouee().getNom());
            alert.getDialogPane().setContent(dialog);
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setResizable(true);
            alert.showAndWait().ifPresent(result -> {
                creneau.getTacheAllouee().setEtatTask(dialog.getValue());
                System.out.println("Task status updated for task: " + creneau.getTacheAllouee().getNom());
            });
        }
        tasksTextField.clear();
        stat.printTasksByDate(startDate,tasksTextField);
    }

    @FXML
    void moyenneRendementOnaction(ActionEvent event) {
        double moyenneRendement = stat.calculRendementjournalierMoyen();


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Moyenne Rendement Journalier");
        alert.setHeaderText(null);
        alert.setContentText("La moyenne du rendement journalier est : " + moyenneRendement);

        // Display the alert dialog
        alert.showAndWait();
    }

    @FXML
    void tempsPasseOnaction(ActionEvent event) {
        ListView<Category> dialog = new ListView<>();
        Category.showCategories(dialog);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Choose the Name of the Category");
        alert.getDialogPane().setContent(dialog);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setResizable(true);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Category selectedCategory = dialog.getSelectionModel().getSelectedItem();
            if (selectedCategory != null) {
                long tempsPasse = stat.categorieplusactive(selectedCategory.getName());

                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Temps passé Catégorie");
                alert1.setHeaderText(null);
                alert1.setContentText("Le temps passé dans la catégorie '" + selectedCategory.getName() + "' est : " + tempsPasse + " minutes.");

                // Display the alert dialog
                alert1.showAndWait();
            }
        }
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = UserData.getUser();
        creneauxNonlib = user.getCalendrier().creneauNonLib();
        stat = new Statistique(creneauxNonlib, user.getCalendrier().getPeriode());
        startDate = LocalDate.now();
        stat.printTasksByDate(startDate, tasksTextField);

        LocalDate jourRentable = stat.jourleplusRentable();
        int nbCompletedTasks = (int) creneauxNonlib.stream()
                .filter(task -> task.getTacheAllouee().getEtatTask() == EtatRealisation.COMPLETED)
                .count();

        jourRentableTextArea.setWrapText(true);
        jourRentableTextArea.setEditable(false);

        String text = "Most profitable day: " + jourRentable + "\nTotal completed tasks: " + nbCompletedTasks;
        jourRentableTextArea.setText(text);
        stat. displayBadgeCounts( goodTextField, veryGoodTextField, excellentTextField);
        nbEncouragementTextField.setText("Well done : "+String.valueOf(stat.calculNbEncouragement()));

    }
}
