package main.java.Classes;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Represents a simple task that can be scheduled.
 * Extends the Task class and implements the Serializable interface.
 */
public class TaskSimple extends Task implements Serializable {

    private int periodicite = 0 ;

    /**
     * Constructs a TaskSimple object with the specified parameters.
     */
    public TaskSimple(String nom, LocalTime dureeTache, Priorite priorite, LocalDate deadline, Category categorie, EtatRealisation etatTask, Boolean bloqTache) {
        super(nom, dureeTache, priorite, deadline, categorie, etatTask, bloqTache);
    }

    public TaskSimple(String nom, LocalTime dureeTache, Priorite priorite, LocalDate deadline, Category categorie, EtatRealisation etatTask, Boolean bloqTache,int periodicite) {
        super(nom, dureeTache, priorite, deadline, categorie, etatTask, bloqTache);
        this.periodicite = periodicite;
    }



    public int getPeriodicite() {
        return periodicite;
    }
    public void setPeriodicite(int periodicite) {
        this.periodicite = periodicite;
    }

    /**
     * Plans the scheduling of the task based on the available time slots.
     *
     * @param mapCreneau The map of days (Jour) mapped to lists of time slots (Creneau).
     * @param dureemin   The minimum duration for scheduling.
     * @return True if the task is successfully scheduled, false otherwise.
     */
    public boolean planifierTache (Map<Jour, ArrayList<Creneau>> mapCreneau, int dureemin) {
        ArrayList<Creneau> orderedCreneaux = new ArrayList<>();
        boolean planifier = false;
        // Parcourir tous les créneaux et les ajouter à la liste ordonnée
        for (ArrayList<Creneau> creneaux : mapCreneau.values()) {
            orderedCreneaux.addAll(creneaux);
        }
        // Trier les créneaux par ordre décroissant de durée
        Collections.sort(orderedCreneaux, new CreneauComparator());
        // Décomposer la tâche en plusieurs sous-tâches jusqu'à ce qu'elle soit complètement planifiée ou qu'aucun créneau ne convienne
        while (!planifier && !orderedCreneaux.isEmpty()) {
            Creneau largestCreneau = orderedCreneaux.get(0);
            Jour jour = null ;
            for (Map.Entry<Jour, ArrayList<Creneau>> entry : mapCreneau.entrySet()) {
                LocalDate currentDate = entry.getKey().getDate(); // Obtenir la LocalDate du jour actuel
                if (currentDate.equals(largestCreneau.getDate())) {
                    jour = entry.getKey(); // Récupérer le jour correspondant à la LocalDate recherchée
                    break;
                }
            }
            if (this.DureeTache() <= largestCreneau.getDuree().toMinutes()) {
                jour.decomposerCrenau(dureemin,this, largestCreneau);
                planifier = true;
            } else {
                orderedCreneaux.remove(0);
            }
        }
        // Si la tâche n'a pas pu être complètement planifiée
        if (!planifier) {
            System.out.println("La tâche n'a pas pu être planifiée avec les créneaux disponibles.");
            //Icii on pose la question des taches uscheduled
        }
        return planifier;
    }

    /**
     * Plans the scheduling of the periodic task based on the available time slots.
     *
     * @param mapCreneau The map of days (Jour) mapped to lists of time slots (Creneau).
     * @param dureemin   The minimum duration for scheduling.
     * @return True if the task is successfully scheduled, false otherwise.
     */
    public Boolean planifierTachePeriodique(Map<Jour, ArrayList<Creneau>> mapCreneau, int dureemin) {
        boolean planifier = false;
        int index = 0;
        for (Map.Entry<Jour, ArrayList<Creneau>> entry : mapCreneau.entrySet()) {
            if (index % periodicite == 0) {
                Jour jour = entry.getKey();
                ArrayList<Creneau> orderedCreneaux = entry.getValue();
                Collections.sort(orderedCreneaux, new CreneauComparator());
                Creneau largestCreneau = orderedCreneaux.get(0);

                if (this.DureeTache() <= largestCreneau.getDuree().toMinutes()) {
                    jour.decomposerCrenau(dureemin, this, largestCreneau);
                    orderedCreneaux.remove(0);
                    planifier = true;
                }
            }
            index++;
        }


        if (!planifier) {
            System.out.println("La tâche n'a pas pu être planifiée avec les créneaux disponibles.");
            // Handle the case when the task could not be scheduled
        }
        return planifier ;
    }

    public void setDuration (Duration duration)
    {
        long totalHours = duration.toHours();
        int minutes = duration.toMinutesPart();
        LocalTime localTime = LocalTime.of((int) totalHours, minutes);
        this.setDureeTache(localTime);
    }
}
