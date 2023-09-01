package main.java.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import main.java.Classes.*;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    protected Utilisateur user = new Utilisateur();

    private MyDesktopPlanner planner = new MyDesktopPlanner();

    @FXML
    private TextField Loguser;

    @FXML
    private TextField Name;

    @FXML
    private TextField Signuser;

    @FXML
    private TextField Surname;


    @FXML
    private Text textlogin;

    @FXML
    private Text textsign;

    @FXML
    private Button btnLogin;

    @FXML
    private Button btnSignup;


    @FXML
    private DatePicker dateDebut;

    @FXML
    private DatePicker dateFin;

    @FXML
    private TextField dureeMin;

    @FXML
    private Text textDatefin;

    @FXML
    private Text textEntier;

    private Boolean addPer = false;


    @FXML
    void saveuserinfo(ActionEvent event) {
        boolean minDur = false ;
        boolean periodeGet = false;
        /************** Lire la duree minimale d'un creneau **************/
        String input = dureeMin.getText();
        try {
            textEntier.setVisible(false);
            int intValue = Integer.parseInt(input);
            user.setDureeMinCrenau(intValue);
            minDur = true ;
        } catch (NumberFormatException e) {
            textEntier.setVisible(true);
        }

        /*************** Lire la periode ***************************/
        if (addPer == true) {
            LocalDate debut = dateDebut.getValue();
            LocalDate fin = dateFin.getValue();
            Periode periode = new Periode(debut, fin);
            Boolean perVal = periode.estValide();
            if (perVal == true){
                        textDatefin.setVisible(false);
                        addPer = true;
                        user.getCalendrier().setPeriode(periode);
                        periodeGet = true;
            }
            else {textDatefin.setVisible(true);}
        }
        else {
            periodeGet = true;
            LocalDate today = LocalDate.now();
            LocalDate endDate = today.plusDays(6);
            Periode periode = new Periode(today, endDate);
            user.getCalendrier().setPeriode(periode);
        }
        if (periodeGet == true && minDur == true)
        {
            showAlert("Information Saved");
            Parent page = null;
            try {
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();
                UserData.setUser(user);
                page = FXMLLoader.load(getClass().getResource("../FXML/CreneauLibre.fxml"));
                // Replace the main content area with the page
                Scene scene = new Scene(page);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    @FXML
    void noPeriode(ActionEvent event) {addPer = false;}


    @FXML
    void AddPeriode(ActionEvent event) {
        dateDebut.setVisible(true);
        dateFin.setVisible(true);
        addPer = true ;
        LocalDate today = LocalDate.now();
        // Configuration de la fabrique de cellules de jour pour désactiver les dates antérieures à la date d'aujourd'hui
        dateDebut.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(today));
            }
        });
        dateFin.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(today));
            }
        });
    }




    @FXML
    void Login(ActionEvent event) {
        if (!Loguser.getText().isEmpty()) {
            Boolean authSuccess = user.Authentifier(Loguser.getText());
            System.out.println(authSuccess);
            if (authSuccess) {
                System.out.println("success");
                Parent page = null;
                try {
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                    Utilisateur user = new Utilisateur();
                    planner.chargerUtilisateurs();
                    user = MyDesktopPlanner.utilisateursMap.get(Loguser.getText());
                    System.out.println(user.getCalendrier().getPeriode().getDebut());
                    System.out.println(MyDesktopPlanner.utilisateursMap.get(Loguser.getText()).getCalendrier().getPeriode().getDebut());
                    UserData.setUser(MyDesktopPlanner.utilisateursMap.get(Loguser.getText()));
                    page = FXMLLoader.load(getClass().getResource("../FXML/Sidebar.fxml"));
                    // Replace the main content area with the page
                    Scene scene = new Scene(page);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else {
                showAlert("Veuillez vérifier votre pseudo ou créer un compte.");
            }
        }
        else {textlogin.setVisible(true);}
    }

    @FXML
    void SignUp(ActionEvent event) {
        if (!Signuser.getText().isEmpty()) {
            boolean CreerSucce = user.CreerCompte(Signuser.getText(), Name.getText(), Surname.getText());
            if (CreerSucce) {
                Parent page = null;
                try {
                    UserData.setUser(user);
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                    page = FXMLLoader.load(getClass().getResource("../FXML/Periode_Calender.fxml"));
                    // Replace the main content area with the page
                    Scene scene = new Scene(page);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                showAlert("Ce pseudo est déja utilisé.");
            }
        }else {
            textsign.setVisible(true);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
