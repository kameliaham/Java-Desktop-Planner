package main.java.Classes;

/**
 * Custom exception class for handling cases where a number is not an integer.
 */
public class NotAnIntegerException extends Exception {

    /**
     * Constructs a new NotAnIntegerException with a default error message.
     */
    public NotAnIntegerException() {
        super("The provided value is not an integer.");
    }

    /**
     * Constructs a new NotAnIntegerException with a custom error message.
     *
     * @param message the custom error message
     */
    public NotAnIntegerException(String message) {
        super(message);
    }
}
