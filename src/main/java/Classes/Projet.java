package main.java.Classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The Projet class represents a project.
 */
public class Projet implements Serializable {
    private String Nom ; 
    private String Description ;
    private ArrayList<Task> taches = new ArrayList<>() ;
    private EtatRealisation EtatProjet ;

    /**
     * Constructs a Projet object with the specified name and description.
     *
     * @param nom         the name of the project
     * @param description the description of the project
     */
    public Projet(String nom, String description) {
        Nom = nom;
        Description = description;
    }


    /********************** Getters and Setters *************************/

    public String getDescription() {
        return Description;
    }

    public String getNom() {
        return Nom;
    }

    public ArrayList<Task> getTaches() {
        return taches;
    }

    /**
     * Adds a task to the project.
     *
     * @param task the task to be added
     */
    public void addTask(Task task) {
        taches.add(task);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Projet other = (Projet) obj;
        return Objects.equals(getNom(), other.getNom());
    }


}
