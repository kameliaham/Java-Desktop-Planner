package main.java.Classes;

import java.time.Duration;
import java.time.LocalTime;

/**
 * An interface for decomposable objects.
 * Provides a method for decomposing the object based on a duration and minimum duration requirement.
 */

public interface Decomposable {
    public boolean decomposer(Duration duration, int dureeMin);
}
