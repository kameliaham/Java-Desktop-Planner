package main.java.Classes;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Represents a user's calendar.
 * Provides methods for manual and automatic task scheduling.
 */
public class UserCalendar implements Serializable {
    private Periode periode = new Periode();
    private ArrayList<Jour> jours = new ArrayList<>();

    private ArrayList<Task> tachesNonProg = new ArrayList<>() ;

    /****************** Constructors **************/
    public UserCalendar(Periode periode, ArrayList<Jour> jours ) {
        this.periode = periode;
        this.jours = jours;
    }

    public UserCalendar(){}

   /**************** Getters and Setters ********************/
    public ArrayList<Jour> getJours() {
        return jours;
    }

    public Periode getPeriode() {
        return periode;
    }

    public void setPeriode(Periode periode) {
        this.periode = periode;
    }



    /************************** Methodes *******************************/
    /**
     * Plans a task manually within a given time slot.
     *
     * @param dureeMin The minimum duration required for the task.
     * @param task     The task to be planned.
     * @param creneau  The time slot for scheduling the task.
     * @return True if the task was successfully planned, false otherwise.
     */
    public boolean planTacheManuel (int dureeMin,Task task,Creneau creneau){
        long durCreneau = creneau.calculerDuree();
        if (durCreneau<task.DureeTache())
        {
            return false ;
        }else {
            LocalDate day = creneau.getDate();
            Jour jour = new Jour(day);
            for (Jour jour1 : jours) {
                if (jour.equals(jour1)) {
                    jour = jour1;
                }
            }
            if (jour != null) {
                jour.decomposerCrenau(dureeMin, task, creneau);
                return true;
            } else {
                return false; // Jour not found
            }
        }
        }

    /**
     * Retrieves a map of time slots available before a given deadline, sorted by date.
     *
     * @param deadline The deadline date.
     * @return A map of days and their available time slots before the deadline.
     */
    public Map<Jour, ArrayList<Creneau>> creneauAvantDatelimiteSort(LocalDate deadline) {
        Map<Jour, ArrayList<Creneau>> creneaux = new TreeMap<>(Comparator.comparing(Jour::getDate));
        for (Jour jour : jours) {
            if (jour.getDate().isBefore(deadline.plusDays(1)) && !jour.getCrenaux().isEmpty()) {
                ArrayList<Creneau> creneauLib = new ArrayList<>(jour.getCreneauLibre());
                // Sort the time slots based on duration (longest to shortest)
                if (!creneauLib.isEmpty())
                {Collections.sort(creneauLib, new CreneauComparator());
                creneaux.put(jour, creneauLib);}
            }
        }
        return creneaux;
    }

    /**
     * Retrieves a map of non-blocked time slots available before a given deadline, sorted by date.
     *
     * @param deadline The deadline date.
     * @return A map of days and their non-blocked time slots before the deadline.
     */
    public Map<Jour, ArrayList<Creneau>> creneauAvantDatelimiteNonbloquee (LocalDate deadline) {
        Map<Jour, ArrayList<Creneau>> creneaux = new TreeMap<>(Comparator.comparing(Jour::getDate));
        for (Jour jour : jours) {
            if (jour.getDate().isBefore(deadline.plusDays(1)) && !jour.getCrenaux().isEmpty()) {
                ArrayList<Creneau> creneauLib = new ArrayList<>(jour.getCreneauNonbloquee());
                // Sort the time slots based on duration (longest to shortest)
                if (!creneauLib.isEmpty())
                {Collections.sort(creneauLib, new CreneauComparator());
                creneaux.put(jour, creneauLib);}
            }
        }
        return creneaux;
    }

    /**
     * Retrieves a list of tasks that need to be scheduled before a given deadline.
     *
     * @param deadline The deadline date.
     * @return A list of tasks that need to be scheduled before the deadline.
     */
    public ArrayList<Task> TasksAplanifier (LocalDate deadline) {
        ArrayList<Task> tasks = new ArrayList<>();
        for (Jour jour : jours) {
            if (jour.getDate().isBefore(deadline.plusDays(1)) && !jour.getCrenaux().isEmpty()) {
                for (Creneau creneau : jour.getCrenaux())
                {
                    if (creneau.getTacheAllouee()!=null)
                    {
                        if (creneau.getTacheAllouee().getBloqTache()!=false)
                        {
                            tasks.add(creneau.getTacheAllouee());
                        }
                    }
                }
            }
        }
        return tasks;
    }

    /**
     * Retrieves a list of non-free time slots in the calendar.
     *
     * @return A list of non-free time slots.
     */
    public ArrayList<Creneau> creneauNonLib (){
        ArrayList<Creneau> creneaux = new ArrayList<>();
        for (Jour jour : jours) {
                ArrayList<Creneau> creneauNonlib = new ArrayList<>(jour.getCreneauNonLibre());
                creneaux.addAll(jour.getCreneauNonLibre());
        }
        return creneaux;
    }



    /**
     * Plans an urgent task by automatically allocating it within available time slots.
     *
     * @param task     The urgent task to be planned.
     * @param dureemin The minimum duration required for the task.
     */
    public void PlanifierTacheUrgente (Task task,int dureemin)
    {
        TaskSimple simpleTask = (TaskSimple) task;
        Map<Jour, ArrayList<Creneau>> mapCreneaux = creneauAvantDatelimiteNonbloquee(task.getDeadline());
        ArrayList<Task> tasksAplanifier = new ArrayList<>();
        tasksAplanifier = TasksAplanifier(task.getDeadline());
        boolean planifier = task.planifierTache(mapCreneaux,dureemin);
        planifierAuto(tasksAplanifier,dureemin);
    }

    /**
     * Replans a list of tasks by automatically scheduling them based on priority and deadlines.
     *
     * @param taskList The list of tasks to be replanned.
     * @param dureemin The minimum duration required for each task.
     * @return True if at least one task was successfully planned, false otherwise.
     */
    public boolean replanifer (List<Task> taskList, int dureemin) {
        boolean planifier = false;
        // Sort the task list based on priority and deadline
        Collections.sort(taskList, new TaskComparator());
        for (Task task : taskList) {
            if (task instanceof TaskDecom) {
                TaskDecom decomposableTask = (TaskDecom) task;
                Map<Jour, ArrayList<Creneau>> mapCreneaux = creneauAvantDatelimiteSort(task.getDeadline());
                planifier = task.planifierTache(mapCreneaux,dureemin);
                if (planifier==false)
                {
                    tachesNonProg.add(task);
                }
            } else {
                TaskSimple simpleTask = (TaskSimple) task;
                System.out.println(((TaskSimple) task).getPeriodicite());
                Map<Jour, ArrayList<Creneau>> mapCreneaux = creneauAvantDatelimiteSort(task.getDeadline());
                if (simpleTask.getPeriodicite()==0){
                    planifier = task.planifierTache(mapCreneaux,dureemin);
                    if (planifier==false)
                    {
                        tachesNonProg.add(task);
                    }
                }
                else {
                    planifier = ((TaskSimple) task).planifierTachePeriodique(mapCreneaux,dureemin);
                    if (planifier==false)
                    {
                        tachesNonProg.add(task);
                    }
                }
            }
        }
        return planifier;
    }



    public ArrayList<Task> getTachesNonProg() {
        return tachesNonProg;
    }

    public boolean planifierAuto(List<Task> taskList, int dureemin) {
        boolean planifier = false;
        // Sort the task list based on priority and deadline
        Collections.sort(taskList, new TaskComparator());
        for (Task task : taskList) {
            if (task instanceof TaskDecom) {
                TaskDecom decomposableTask = (TaskDecom) task;
                Map<Jour, ArrayList<Creneau>> mapCreneaux = creneauAvantDatelimiteSort(task.getDeadline());
                planifier = task.planifierTache(mapCreneaux,dureemin);
                if (planifier==false)
                {
                    tachesNonProg.add(task);
                }
            } else {
               TaskSimple simpleTask = (TaskSimple) task;
                System.out.println(((TaskSimple) task).getPeriodicite());
                Map<Jour, ArrayList<Creneau>> mapCreneaux = creneauAvantDatelimiteSort(task.getDeadline());
                if (simpleTask.getPeriodicite()==0){
                    planifier = task.planifierTache(mapCreneaux,dureemin);
                    if (planifier==false)
                    {
                        tachesNonProg.add(task);
                    }
                }
                else {
                    planifier = ((TaskSimple) task).planifierTachePeriodique(mapCreneaux,dureemin);
                    if (planifier==false)
                    {
                        tachesNonProg.add(task);
                    }
                }
            }
        }
        return planifier;
    }

}
