package main.java.Classes;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The Statistique class represents statistics related to tasks and periods.
 */
public class Statistique implements Serializable {


    private int nbtachesmin;
    private List<Creneau> tasks;
    private Periode period;

    /************************* Construtor ******************************************/

    /**
     * Constructs a Statistique object with the specified list of tasks and period.
     *
     * @param tasks  the list of tasks
     * @param period the period
     */
    public Statistique( List<Creneau> tasks, Periode period) {
        this.nbtachesmin = 1;
        this.tasks = tasks;
        this.period = period;
    }



    /**
     * Checks if a encouraging message should be displayed for the specified date.
     *
     * @param date the date to check
     * @return true if an encouraging message should be displayed, false otherwise
     */
    public boolean msgEncouragement(LocalDate date) {
        int completedTasks = 0;
        for (Creneau task : tasks) {

            if (task.getTacheAllouee().getEtatTask() == EtatRealisation.COMPLETED && task.getDate().equals(date)) {
                completedTasks++;
            }
        }
        return  completedTasks >= nbtachesmin;
    }

    /**
     * Calculates the number of encouraging messages.
     *
     * @return the number of encouraging messages
     */
    public int calculNbEncouragement() {
        int nbEncouragement = 0;

        Collections.sort(tasks, Comparator.comparing(Creneau::getDate));

        LocalDate previousDate = null;

        for (Creneau task : tasks) {
            LocalDate taskDate = task.getDate();
            //on evite de recalculer pour les memes dates
            if (taskDate.equals(previousDate)) {
                continue;
            }

            if (msgEncouragement(taskDate)) {
                nbEncouragement++;
            }

            previousDate = taskDate;
        }
        return nbEncouragement;
    }

    public int getNbtachesmin() {
        return nbtachesmin;
    }

    public void setNbtachesmin(int nbtachesmin) {
        this.nbtachesmin = nbtachesmin;
    }

    /**
     * Prints the tasks for a given date to the specified TextArea.
     *
     * @param date          the date to display tasks for
     * @param tasksTextArea the TextArea to display the tasks
     */
    public void printTasksByDate(LocalDate date, TextArea tasksTextArea) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDate = date.format(formatter);

        tasksTextArea.appendText("Tâches du jour : \n\n");

        for (Creneau task : tasks) {
            LocalDate taskDateStart = task.getDate();
            if (taskDateStart != null && taskDateStart.equals(date)) {
                tasksTextArea.appendText(task.getTacheAllouee().getNom() + ": " + task.getTacheAllouee().getEtatTask() + "\n");
            }
        }
    }

    /**
     * Calculates the daily efficiency for the specified date.
     *
     * @param date the date to calculate the daily efficiency for
     * @return the daily efficiency as a percentage
     */
    public double calculrendementjournalier(LocalDate date) {
        int totalTasks =  0;
        int completedTasks = 0;
            for (Creneau task : tasks) {
            if (task.getDate().equals(date)) {
                System.out.println("heree");
                totalTasks++;
                if (task.getTacheAllouee().getEtatTask() == EtatRealisation.COMPLETED) {
                    completedTasks++;
                }
            }
        }

        return (double) completedTasks / totalTasks;
    }

    /**
     * Finds the most profitable day based on daily efficiency.
     *
     * @return the date of the most profitable day
     */
    public LocalDate jourleplusRentable() {
        double highestRatio = 0.0;
        LocalDate jourRentable = null;

        for (Creneau task : tasks) {
            LocalDate date = task.getDate();
            double rendement = calculrendementjournalier(date);
            if (rendement > highestRatio) {
                highestRatio = rendement;
                jourRentable = date;
            }
        }

        return jourRentable;
    }

    /**
     * Calculates the total duration spent on tasks of a specific category.
     *
     * @param categoryName the name of the category
     * @return the total duration spent on tasks of the specified category
     */
    public long categorieplusactive(String categoryName) {
        long durationSum = 0;

        for (Creneau task : tasks) {
            if (task.getTacheAllouee().getCategorie().getName().equalsIgnoreCase(categoryName)) {
                durationSum += task.getTacheAllouee().DureeTache();
            }
        }

        return durationSum;
    }

    /**
     * Calculates the average daily efficiency.
     *
     * @return the average daily efficiency as a percentage
     */
    public double calculRendementjournalierMoyen() {
        LocalDate startDate = period.getDebut();
        LocalDate endDate = period.getFin();

        int nbJours = (int) ChronoUnit.DAYS.between(startDate, endDate)+1;

        double rendementTotal = 0.0;

        // Sort the tasks array by dates
        Collections.sort(tasks, Comparator.comparing(Creneau::getDate));

        LocalDate previousDate = null;

        for (Creneau task : tasks) {
            LocalDate taskDate = task.getDate();
            //on evite de recalculer pour les memes dates
            if (taskDate.equals(previousDate)) {
                continue;
            }

            double rendement = calculrendementjournalier(taskDate);
            rendementTotal += rendement;

            previousDate = taskDate;
        }

        if (nbJours > 0) {

            return rendementTotal / nbJours;
        } else {
            return 0.0;
        }
    }

    /**
     * Calculates the average daily efficiency.
     *
     * @return the average daily efficiency as a percentage
     */
    public void afficherbadgereussite() {
        int consecutiveSuccessCount = 0;
        Encouragement currentBadge = null;
        int goodCount = 0;
        int veryGoodCount = 0;

        Collections.sort(tasks, Comparator.comparing(Creneau::getDate));

        LocalDate previousDate = null;

        for (Creneau task : tasks) {
            LocalDate taskDate = task.getDate();
            // Eviter de recalculer pour les mêmes dates
            if (taskDate.equals(previousDate)) {
                continue;
            }

            if (msgEncouragement(taskDate)) {

                // Checker la consecutivite des taches
                if (previousDate != null && previousDate.plusDays(1).equals(taskDate)) {
                    consecutiveSuccessCount++;
                } else {
                    consecutiveSuccessCount = 1;
                }

                if (consecutiveSuccessCount == 5) {
                    goodCount++;
                    consecutiveSuccessCount = 0;
                    if (goodCount == 3) {
                        veryGoodCount++;
                        goodCount = 0;
                        if (veryGoodCount == 3) {
                            currentBadge = Encouragement.EXCELLENT;
                            break;
                        } else {
                            currentBadge = Encouragement.VERYGOOD;
                        }
                    } else {
                        currentBadge = Encouragement.GOOD;
                    }
                }
            } else {
                consecutiveSuccessCount = 0;
            }

            previousDate = taskDate;
        }

        if (currentBadge != null) {
            System.out.println("Congratulations! You have earned the '" + currentBadge + "' badge!");
        }
    }

    /**
     * Displays the counts of different types of badges.
     *
     * @param goodTextField      the TextField to display the count of "Good" badges
     * @param veryGoodTextField  the TextField to display the count of "Very Good" badges
     * @param excellentTextField the TextField to display the count of "Excellent" badges
     */
    public void displayBadgeCounts(TextField goodTextField, TextField veryGoodTextField, TextField excellentTextField) {
        int goodCount = 0;
        int veryGoodCount = 0;
        int excellentCount = 0;

        Collections.sort(tasks, Comparator.comparing(Creneau::getDate));

        LocalDate previousDate = null;
        int consecutiveSuccessCount = 0;

        for (Creneau task : tasks) {
            LocalDate taskDate = task.getDate();

            // Avoid recalculating for the same dates
            if (taskDate.equals(previousDate)) {
                continue;
            }

            if (msgEncouragement(taskDate)) {
                // Check the consecutiveness of tasks
                if (previousDate != null && previousDate.plusDays(1).equals(taskDate)) {
                    consecutiveSuccessCount++;
                } else {
                    consecutiveSuccessCount = 1;
                }

                if (consecutiveSuccessCount == 5) {
                    goodCount++;
                    consecutiveSuccessCount = 0;
                    if (goodCount == 3) {
                        veryGoodCount++;
                        goodCount = 0;
                        if (veryGoodCount == 3) {
                            excellentCount++;
                            veryGoodCount = 0;
                        }
                    }
                }
            } else {
                consecutiveSuccessCount = 0;
            }

            previousDate = taskDate;
        }

        goodTextField.setText("Good : "+String.valueOf(goodCount));
        veryGoodTextField.setText("Very Good : "+String.valueOf(veryGoodCount));
        excellentTextField.setText("Excellent : "+String.valueOf(excellentCount));
    }

}

