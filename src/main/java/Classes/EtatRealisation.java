package main.java.Classes;

import java.io.Serializable;

/**
 * An enumeration representing different states of task realization.
 */

public enum EtatRealisation implements Serializable {
        NOT_REALIZED,
        IN_PROGRESS,
        COMPLETED,
        CANCELLED,
        DELAYED,
}
