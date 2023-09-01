package main.java.Classes;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The MyDesktopPlanner class represents a desktop planner application that manages users and their accounts.
 */
public class MyDesktopPlanner implements Serializable {
    /** The map of users, where the key is the user's pseudo and the value is the corresponding User object. */
    public static Map<String, Utilisateur> utilisateursMap = new HashMap<>();

    /**
     * Constructs a MyDesktopPlanner object and loads users from the serialization file.
     */
    public MyDesktopPlanner() {
        // Load users from the serialization file when creating the MyDesktopPlanner object
        chargerUtilisateurs();
    }

    /**
     * Authenticates a user based on the provided pseudo.
     * @param pseudo The pseudo of the user to authenticate.
     * @return The User object if the authentication is successful, or null if authentication fails.
     */
    public Utilisateur Ethantifier (String pseudo) {
        if (pseudoExiste(pseudo)) {
            return utilisateursMap.get(pseudo);
        }
        // Authentication failed
        return null;
    }


    /**
     * Creates a new user account.
     * @param user The User object representing the new user.
     * @return True if the account is created successfully, false if the pseudo is already used.
     */
    public boolean creerCompte(Utilisateur user) {
        // Vérifier si le pseudo est disponible
        if (pseudoExiste(user.getPseudo())) {
            System.out.println("Pseudo déjà utilisé !");
            return false;
        }
        // Ajouter le nouvel utilisateur à la map
        utilisateursMap.put(user.getPseudo(),user);
        SauvegarderUilisateurs();
        return true;
    }

    /**
     * Checks if a pseudo already exists in the user map.
     * @param pseudo The pseudo to check.
     * @return True if the pseudo already exists, false otherwise.
     */
    private boolean pseudoExiste(String pseudo) {
        if (utilisateursMap == null) {
            return false;
        }
        // Vérifier si le pseudo existe déjà dans la map des utilisateurs
        return utilisateursMap.containsKey(pseudo);
    }

    /**
     * Saves the users map to a serialization file.
     */
    public void SauvegarderUilisateurs() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("utilisateurs.ser");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(utilisateursMap);
            objectOutputStream.close();
            fileOutputStream.close();
            System.out.println("Utilisateur objects have been serialized and saved to the file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads users from the serialization file.
     */
    public void chargerUtilisateurs() {
        File file = new File("utilisateurs.ser");
        if (file.exists()) {
            try {
                FileInputStream fileIn = new FileInputStream("utilisateurs.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                utilisateursMap = (Map<String, Utilisateur>) in.readObject();
                in.close();
                fileIn.close();
                // Use the data in the users HashMap here
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
