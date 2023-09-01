package main.java.Classes;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Represents a decomposable task that can be decomposed into smaller subtasks.
 * Extends the Task class and implements the Decomposable interface.
 */
public class TaskDecom extends Task implements Decomposable, Serializable {

    private ArrayList<TaskSimple> SousTaches = new ArrayList<>();

    /**
     * Constructs a TaskDecom object with the specified parameters..
     */
    public TaskDecom(String nom, LocalTime dureeTache, Priorite priorite, LocalDate deadline, Category categorie, EtatRealisation etatTask, Boolean bloqTache) {
        super(nom, dureeTache, priorite, deadline, categorie, etatTask, bloqTache);
    }



    /**
     * Plans the decomposition of the task into subtasks based on the available time slots.
     *
     * @param mapCreneau The map of days (Jour) mapped to lists of time slots (Creneau).
     * @param dureemin   The minimum duration for decomposition.
     * @return True if the task is successfully decomposed and planned, false otherwise.
     */
    public boolean planifierTache (Map<Jour, ArrayList<Creneau>> mapCreneau, int dureemin) {
        for (Map.Entry<Jour, ArrayList<Creneau>> entry : mapCreneau.entrySet()) {
            Jour jour = entry.getKey();
            ArrayList<Creneau> creneaux = entry.getValue();
        }

        boolean decomposed = false;
        boolean sousTacheExist = false ;
        int i = 1 ;
        ArrayList<Creneau> orderedCreneaux = new ArrayList<>();
        // Parcourir tous les créneaux et les ajouter à la liste ordonnée
        for (ArrayList<Creneau> creneaux : mapCreneau.values()) {
            orderedCreneaux.addAll(creneaux);
        }
        // Trier les créneaux par ordre décroissant de durée
        Collections.sort(orderedCreneaux, new CreneauComparator());
        // Décomposer la tâche en plusieurs sous-tâches jusqu'à ce qu'elle soit complètement planifiée ou qu'aucun créneau ne convienne
        while (!decomposed && !orderedCreneaux.isEmpty()) {
            Creneau largestCreneau = orderedCreneaux.get(0);
            Jour jour = null ;
            for (Map.Entry<Jour, ArrayList<Creneau>> entry : mapCreneau.entrySet()) {
                LocalDate currentDate = entry.getKey().getDate(); // Obtenir la LocalDate du jour actuel
                if (currentDate.equals(largestCreneau.getDate())) {
                    jour = entry.getKey(); // Récupérer le jour correspondant à la LocalDate recherchée
                    break;
                }
            }
            if (decomposer(largestCreneau.getDuree(), dureemin)) {
                if (!sousTacheExist) {
                    jour.decomposerCrenau(dureemin, this, largestCreneau);
                } else {
                    String taskName = this.getNom() + " " + i;
                    TaskSimple task = new TaskSimple(taskName, this.getDureeTache(), this.getPriorite(), this.getDeadline(), this.getCategorie(), EtatRealisation.IN_PROGRESS, this.getBloqTache());
                    jour.decomposerCrenau(dureemin, task, largestCreneau);
                }
                decomposed = true;
            } else {
                String taskName = this.getNom() + " " + i;
                long totalHours = largestCreneau.getDuree().toHours();
                int minutes = largestCreneau.getDuree().toMinutesPart();
                LocalTime localTime = LocalTime.of((int) totalHours, minutes);
                TaskSimple task = new TaskSimple(taskName, localTime, this.getPriorite(), this.getDeadline(), this.getCategorie(), EtatRealisation.IN_PROGRESS, this.getBloqTache());
                jour.decomposerCrenau(dureemin, task, largestCreneau);
                Duration tacheDuration = Duration.ofMinutes(this.getDureeTache().toSecondOfDay() / 60);
                Duration remainingDuration = tacheDuration.minus(largestCreneau.getDuree());
                long remainingHours = remainingDuration.toHours();
                int remainingMinutes = remainingDuration.toMinutesPart();
                LocalTime remainingTime = LocalTime.of((int) remainingHours, remainingMinutes);
                this.setDureeTache(remainingTime);
                this.SousTaches.add(task);
                orderedCreneaux.remove(0);
                sousTacheExist = true;
                i++;
            }
            if ((!SousTaches.isEmpty())) {
                for (Task task : SousTaches) {
                }
            }
        }
        // Si la tâche n'a pas pu être complètement planifiée
        if (!decomposed) {
             System.out.println("La tâche n'a pas pu être planifiée avec les créneaux disponibles.");
        }
        return decomposed;
    }

    /**
     * Sets the duration of the decomposed task.
     *
     * @param duration The duration of the task.
     */
    public void setDuration (Duration duration)
    {
        long totalHours = duration.toHours();
        int minutes = duration.toMinutesPart();
        LocalTime localTime = LocalTime.of((int) totalHours, minutes);
        this.setDureeTache(localTime);
    }

    /**
     * Checks if the task can be further decomposed based on the given duration.
     *
     * @param duration  The duration to compare with.
     * @param dureeMin  The minimum duration for decomposition.
     * @return True if the task can be decomposed further, false otherwise.
     */
    @Override
    public boolean decomposer(Duration duration,int dureeMin) {
        return this.DureeTache() <= duration.toMinutes(); //Si la duree de la tache inferieure a la duree du creneau on rrete la decomposition
    }


}





