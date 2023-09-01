package main.java.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import main.java.Classes.Projet;
import main.java.Classes.Task;
import main.java.Classes.UserData;
import main.java.Classes.Utilisateur;

import java.net.URL;
import java.util.ResourceBundle;

public class ProjectController implements Initializable {
        protected Utilisateur user = new Utilisateur();



        @FXML
        private VBox ProjectListBox;

        @FXML
        private Pane newProject;

        @FXML
        private TextArea projectDescription;

        @FXML
        private TextField projectName;

        @FXML
        void addProjects(ActionEvent event) {
                newProject.setVisible(true);
        }

        @FXML
        void closeNewproject(ActionEvent event) {
                newProject.setVisible(false);
                populateProjects();
        }

        @FXML
        void saveProject(ActionEvent event) {
                String name = projectName.getText();
                String description = projectDescription.getText();
                Projet projet = new Projet(name,description);
                user.getProjets().add(projet);
                populateProjects();
                newProject.setVisible(false);
        }
        public void populateProjects() {
                ProjectListBox.getChildren().clear();  // Clear existing content

                for (Projet project : user.getProjets()) {
                        VBox projectBox = new VBox();
                        projectBox.setStyle("-fx-border-color: black; -fx-padding: 10px");
                        // Name label
                        Label nameLabel = new Label(project.getNom());
                        nameLabel.setStyle("-fx-font-weight: bold");
                        // Description label
                        Label descLabel = new Label(project.getDescription());

                        // ListView of tasks
                        ListView<Task> tasksListView = new ListView<>();
                        tasksListView.setPrefHeight(50);  // Adjust the height as needed

                        // Add tasks to the ListView
                        tasksListView.getItems().addAll(project.getTaches());

                        // Add labels and ListView to the project VBox
                        projectBox.getChildren().addAll(nameLabel, descLabel, tasksListView);

                        // Add the project VBox to the main VBox
                        ProjectListBox.getChildren().add(projectBox);
                }
        }

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
                user = UserData.getUser();
                populateProjects();

        }
}