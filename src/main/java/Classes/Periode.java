package main.java.Classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The Periode class represents a time period defined by a start date and an end date.
 */
public class Periode implements Serializable {
    private LocalDate debut;
    private LocalDate fin;

    /******************* Contructor ************************/
    /**Constructs a Periode object with the specified start and end dates..*/
    public Periode(LocalDate debut, LocalDate fin) {
        this.debut = debut;
        this.fin = fin;
    }
    public Periode(){}


    /******************** Getters and Setters ***************/

    public LocalDate getDebut() {
        return debut;
    }

    public void setDebut(LocalDate debut) {
        this.debut = debut;
    }

    public LocalDate getFin() {
        return fin;
    }

    public void setFin(LocalDate fin) {
        this.fin = fin;
    }


    /***************************** Methodes *****************************/
    /**
     * Checks if the time period is valid, i.e., the start date is before or equal to the end date.
     * @return True if the time period is valid, false otherwise.
     */
    public boolean estValide() {
        return debut.isBefore(fin) || debut.isEqual(fin);
    }

    /**
     * Generates a list of dates within the time period, including the start and end dates.
     * @return The list of dates within the time period.
     */
    public List<LocalDate> generateDateRange() {
        List<LocalDate> dateRange = new ArrayList<>();
        LocalDate currentDate = debut ;
        while (!currentDate.isAfter(fin)) {
            dateRange.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }
        return dateRange;
    }
}
