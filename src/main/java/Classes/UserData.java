package main.java.Classes;


/**
 * The `UserData` class represents the user data in the application.
 * It holds references to the current user and a selected time slot (Creneau).
 */

public class UserData {
    private static Utilisateur user;
    private static Creneau creneau;

    public static Creneau getCreneau() {
        return creneau;
    }

    public static void setCreneau(Creneau creneau) {
        UserData.creneau = creneau;
    }

    public static Utilisateur getUser() {
        return user;
    }

    public static void setUser(Utilisateur newUser) {
        user = newUser;
    }
}

