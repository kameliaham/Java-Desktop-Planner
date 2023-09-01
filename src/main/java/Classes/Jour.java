package main.java.Classes;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


/**
 * Represents a day with time slots (creneaux) for scheduling tasks.
 */
public class Jour implements Serializable {
    private LocalDate day ;
    private ArrayList<Creneau> creneaux = new ArrayList<>() ;

    /*************** Constructor **************/
    /**
     * Constructs a new Jour instance with the specified date.
     *
     * @param jour the date of the day
     */
    public Jour (LocalDate jour)  {
        this.day = jour;
    }


    /*********************** Getters and Setters *****************/
    public void setCrenaux(ArrayList<Creneau> crenaux) {
        this.creneaux = crenaux;
    }

    public ArrayList<Creneau> getCrenaux() {
        return creneaux;
    }

    public LocalDate getDate() {
        return day;
    }

    /***************************** Les methodes ***********************************/


    /**
     * Checks if a given time slot overlaps with any existing time slots in the day.
     *
     * @param newCreneau the new time slot to check
     * @return true if the time slot is available, false if there is a conflict
     */
    private boolean isTimeSlotAvailable(Creneau newCreneau) {
        for (Creneau existingCreneau : creneaux) {
            if (existingCreneau.overlapsWith(newCreneau)) {
                return false; // There is a conflict
            }
        }
        return true; // No conflict, the time slot is available
    }


    /**
     * Returns the list of free time slots (creneaux) in the day.
     *
     * @return the list of free time slots
     */
    public ArrayList<Creneau> getCreneauLibre() {
        ArrayList<Creneau> creneauLib = new ArrayList<>();
        for (Creneau creneau : creneaux) {
            if (creneau.getEtatCrenau() == EtatCrenau.LIBRE) {
                creneauLib.add(creneau);
            }
        }
        return creneauLib;
    }

    /**
     * Returns the list of non-bloquee time slots (creneaux) in the day.
     *
     * @return the list of non-bloquee time slots
     */
    public ArrayList<Creneau> getCreneauNonbloquee() {
        ArrayList<Creneau> creneauNonbloquee = new ArrayList<>();
        for (Creneau creneau : creneaux) {
            if ((creneau.getEtatCrenau() == EtatCrenau.LIBRE)||(creneau.getTacheAllouee().getBloqTache() == false)) {
                creneauNonbloquee.add(creneau);
            }
        }
        return creneauNonbloquee;
    }

    /**
     * Returns the list of non-libre (occupied) time slots (creneaux) in the day.
     *
     * @return the list of non-libre time slots
     */
    public ArrayList<Creneau> getCreneauNonLibre () {
        ArrayList<Creneau> creneaunonLib = new ArrayList<>();
        for (Creneau creneau : creneaux) {
            if (creneau.getEtatCrenau() == EtatCrenau.OCCUPE) {
                creneaunonLib.add(creneau);
            }
        }
        return creneaunonLib;
    }


    /**
     * Adds a new time slot (creneau) to the day.
     *
     * @param debut the start time of the time slot
     * @param fin   the end time of the time slot
     * @return true if the time slot is added successfully, false if there is a conflict with an existing time slot
     */
    public boolean ajouterCreneau(LocalTime debut, LocalTime fin) {
        Creneau creneau = new Creneau(debut,fin,day);
        if (isTimeSlotAvailable(creneau)) {
            creneaux.add(creneau);
            return  true;
        } else {
            System.out.println("A conflicting time slot already exists.");
            return false;
        }
    }

    /**
     * Decomposes a time slot (creneau) into smaller time slots to accommodate a task.
     *
     * @param dureeMin the minimum duration for a time slot
     * @param task     the task to be allocated in the time slot
     * @param creneau  the time slot to be decomposed
     */
    public void decomposerCrenau(int dureeMin, Task task, Creneau creneau) {
        LocalTime firstCreneauEnd = creneau.getEndTime() ;
        int index = creneaux.indexOf(creneau);
        if (index != -1) {
            Creneau existingCreneau = creneaux.get(index);
            LocalTime endTache = existingCreneau.getStartTime().plusMinutes(task.DureeTache());
            Duration remainingTime = Duration.between(endTache,existingCreneau.getEndTime());

            // Attempt to decompose the existing creneau
            boolean decomp = existingCreneau.decomposer(remainingTime,dureeMin);
            if (decomp)
            {
                // Le creneau ne va pas etre decomposer
                task.setDureeTache(task.getDureeTache().plus(remainingTime));;
                existingCreneau.setTacheAllouee(task);
                existingCreneau.setEtatCrenau(EtatCrenau.OCCUPE);

            }
            if (!decomp) {
                // le creneau va etre decomposer , allouer a new creneau qui va etre d'etat libre
                existingCreneau.setEndTime(endTache);
                existingCreneau.setTacheAllouee(task);
                existingCreneau.setEtatCrenau(EtatCrenau.OCCUPE);
                LocalTime startNewCre = endTache;
                LocalTime endNewCren = firstCreneauEnd;
                Creneau newcreneau = new Creneau(startNewCre,endNewCren, day);
                creneaux.add(newcreneau);
            }
        }
    }



    /**
     * Checks if this Jour object is equal to another object.
     *
     * 2 Jours are equals if the have the same day (localdate)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Jour other = (Jour) obj;
        return day.equals(other.day);
    }

    @Override
    public int hashCode() {
        return day.hashCode();
    }

}
