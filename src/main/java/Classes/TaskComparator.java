package main.java.Classes;

import java.io.Serializable;
import java.util.Comparator;

/**
 * The TaskComparator class compares two Task objects based on their priority and deadline.
 */
public class TaskComparator implements Comparator<Task>, Serializable {

    /**
     * Compares two Task objects.
     *
     * @param task1 the first task to compare
     * @param task2 the second task to compare
     * @return a negative integer if task1 is less than task2, zero if they are equal, or a positive integer if task1 is greater than task2
     */
    @Override
    public int compare(Task task1, Task task2) {
        // Comparaison basée sur la priorité
        int priorityComparison = Integer.compare(task1.getPriorite().ordinal(), task2.getPriorite().ordinal());
        if (priorityComparison != 0) {
            return priorityComparison;
        }

        // Comparison based on deadline
        return task1.getDeadline().compareTo(task2.getDeadline());
    }
}
