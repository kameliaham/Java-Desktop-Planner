package main.java.Classes;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a time slot with a start time, end time, and date.
 * Implements the Decomposable and Serializable interfaces.
 */

public class Creneau implements Decomposable, Serializable {
    private LocalTime startTime;
    private LocalDate date ;
    private LocalTime endTime;
    private EtatCrenau etatCrenau ;
    private Task tacheAllouee ;


    /********************* Constructors *************************************/
    /**
     * Constructs a Creneau object with the given start time, end time, and date.
     *
     * @param startTime the start time of the time slot
     * @param endTime   the end time of the time slot
     * @param date      the date of the time slot
     */
    public Creneau(LocalTime startTime, LocalTime endTime, LocalDate date) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        etatCrenau = EtatCrenau.LIBRE;
    }

    public Creneau(LocalTime startTime, LocalDate date, LocalTime endTime, EtatCrenau etatCrenau, Task tacheAllouee) {
        this.startTime = startTime;
        this.date = date;
        this.endTime = endTime;
        this.etatCrenau = etatCrenau;
        this.tacheAllouee = tacheAllouee;
    }

    /***************************** Getters and Setters **************************/
    public LocalDate getDate() {
        return date;
    }

    public long calculerDuree() {
        Duration duration = Duration.between(startTime, endTime);
        return duration.toMinutes();
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Duration getDuree() {
        return Duration.between(startTime, endTime);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setEtatCrenau(EtatCrenau etatCrenau) {
        this.etatCrenau = etatCrenau;
    }

    public EtatCrenau getEtatCrenau() {
        return etatCrenau;
    }

    public Task getTacheAllouee() {
        return tacheAllouee;
    }

    public void setTacheAllouee(Task tacheAllouee) {
        this.tacheAllouee = tacheAllouee;
    }

    /**************************** Methodes ********************************************/
    public boolean testCreneau(int minTime) { //Verifie si le creneau est valide "Sa duree sup a la duree min et la fin avant depart
        Duration duration = Duration.between(startTime,endTime);
        int minutes = (int) duration.toMinutes();
        return startTime.isBefore(endTime) && minutes >= minTime;
    }



    /*************** Redéfiner la methode decomposer de l'interface Decomposable *****************************/
    public boolean decomposer(Duration remainingTime,int dureeMin) {
        if (remainingTime.toMinutes() < dureeMin) {
            // if la duree restante inferieure a la duree min d'un creneau la creneau va devenir directement une tache
            Duration taskDuration = this.getDuree().plus(remainingTime);
            // Set the creneau state to occupe
            this.setEtatCrenau(EtatCrenau.OCCUPE);
            return true; // Decomposition successful
        } else {  //Si non le creneau va etre decomposer
            this.setEtatCrenau(EtatCrenau.OCCUPE);
            return false; // On va ajouter un nouveau creneau
        }
    }


    /******************** On peut pas avoir 2 creneau qui se croise dans la meme journé *****************/
    public boolean overlapsWith(Creneau other) {
        // Convert start and end times to numerical format for comparison
        int start1 = startTime.toSecondOfDay();
        int end1 = endTime.toSecondOfDay();
        int start2 = other.startTime.toSecondOfDay();
        int end2 = other.endTime.toSecondOfDay();

        // Check for overlap
        return !(end1 <= start2 || end2 <= start1);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Creneau other = (Creneau) obj;
        return startTime.equals(other.startTime);
    }

    @Override
    public int hashCode() {
        return startTime.hashCode();
    }


}
