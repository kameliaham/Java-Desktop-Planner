package main.java.Classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Map;

/**
 * The abstract Task class represents a task with a name, duration, priority, deadline, category, state, and blocking status.
 */
public abstract class Task implements Serializable {
    private String nom ;
    private LocalTime dureeTache; // duration in minutes
    private Priorite priorite;
    private LocalDate deadline; // optional deadline
    private Category categorie;
    private EtatRealisation etatTask ;
    private Boolean BloqTache ;


    /************************** Constructor **************************************/

    /**
     * Constructs a Task object with the specified attributes.
     */
    public Task(String nom, LocalTime dureeTache, Priorite priorite, LocalDate deadline, Category categorie,EtatRealisation etatTask , Boolean bloqTache) {
        this.nom = nom;
        this.dureeTache = dureeTache;
        this.priorite = priorite;
        this.deadline = deadline;
        this.categorie = categorie;
        this.etatTask = etatTask;
        this.BloqTache = bloqTache;
    }

    /**
     * Default constructor for the Task class.
     */
    public Task (){}


    /************************ Getters and Setters **********************************/

    public void setDureeTache(LocalTime dureeTache) {
        this.dureeTache = dureeTache;
    }
    public String getNom() {
        return nom;
    }

    public Priorite getPriorite() {
        return priorite;
    }

    public Boolean getBloqTache() {
        return BloqTache;
    }

    public EtatRealisation getEtatTask() {
        return etatTask;
    }

    public LocalDate getDeadline() {
        return deadline;
    }
    public LocalTime getDureeTache() {
        return dureeTache;
    }
    public Category getCategorie() {
        return categorie;
    }
    public void setEtatTask(EtatRealisation etatTask) {
        this.etatTask = etatTask;
    }



    /********************** Abstract methodes ***********************/
    /**
     * Abstract method to plan the task in a schedule.
     *
     * @param mapCreneau the map of time slots by day
     * @param dureemin   the minimum duration for the task
     * @return true if the task is successfully planned, false otherwise
     */
    public abstract boolean planifierTache (Map<Jour, ArrayList<Creneau>> mapCreneau, int dureemin);


    /****************** Methodes ***********************/
    /**
     * Calculates the duration of the task in minutes.
     */
    public long DureeTache() {
        int hour = dureeTache.getHour();
        int minute = dureeTache.getMinute();
        long totalMinutes = hour * 60 + minute;
        return totalMinutes;
    }



    @Override
    public String toString() {
        return nom + " - Deadline:  " + deadline + " -- Category: " + categorie.getName() + "  -- Duration: " + dureeTache +"  --Priority: "+priorite+"  -- Etat:  "+etatTask;
    }



}
