package main.java.Classes;

import java.io.Serializable;
import java.util.Comparator;


/**
 * A comparator for comparing Creneau objects based on their durations.
 * Implements the Comparator and Serializable interfaces.
 */

public class CreneauComparator implements Comparator<Creneau>, Serializable {
    /**
     * Compares two Creneau objects based on their durations.
     *
     * @param creneau1 the first Creneau object to compare
     * @param creneau2 the second Creneau object to compare
     * @return a negative integer if creneau1 has a shorter duration than creneau2,
     *         zero if creneau1 and creneau2 have the same duration,
     *         a positive integer if creneau1 has a longer duration than creneau2
     */
    @Override
    public int compare(Creneau creneau1, Creneau creneau2) {
        // Compare the durations of the time slots
        return Long.compare(creneau2.getDuree().toMinutes(), creneau1.getDuree().toMinutes());
    }
}
