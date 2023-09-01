package main.java.Classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The `Utilisateur` class represents a user in the application.
 * It contains information about the user, such as their username, calendar, projects, history, and preferences.
 * The class implements the `Serializable` interface to support data serialization for storage or transmission.
 */

public class Utilisateur implements Serializable {
    private String Pseudo ;
    private UserCalendar Calendrier = new UserCalendar() ;
    private ArrayList<Projet> Projets = new ArrayList<>();
    private ArrayList<Historique> historiques;
    private int DureeMinCrenau ;
    private String nom;
    private String prenom;
    private int NbTachesMin ;


    /*************** Constructor *******************/
    public Utilisateur() {
        Calendrier = new UserCalendar();
    }

    public Utilisateur(String pseudo, UserCalendar calendrier, ArrayList<Projet> projets, ArrayList<Historique> historiques, int dureeMinCrenau,String nom, String prenom, int nbTachesMin) {
        Pseudo = pseudo;
        Calendrier = calendrier;
        Projets = projets;
        this.historiques = historiques;
        DureeMinCrenau = dureeMinCrenau;
        this.nom = nom;
        this.prenom = prenom;
        NbTachesMin = nbTachesMin;
    }

    public Utilisateur(String pseudo,String nom,String prenom) {
        this.Pseudo = pseudo;
        this.nom = nom;
        this.prenom = prenom;
    }


    /******************* Getters and Setters ***************************************/
    public ArrayList<Projet> getProjets() {
        return Projets;
    }

    public String getPseudo() {
        return Pseudo;
    }

    public void setPseudo(String pseudo) {
        Pseudo = pseudo;
    }

    public void setDureeMinCrenau(int dureeMinCrenau) {
        DureeMinCrenau = dureeMinCrenau;
    }

    public Utilisateur (String pseudo)
    {
        this.Pseudo = pseudo;
    }

    public int getDureeMinCrenau() {
        return DureeMinCrenau;
    }

    public UserCalendar getCalendrier() {
        return Calendrier;
    }


    /********************************* METHODES **************************************/
    /**
     * Authenticates the user with the given username.
     *
     * @param pseudo The username to authenticate.
     * @return `true` if the authentication is successful, `false` otherwise.
     */
    public boolean Authentifier(String pseudo) {
        MyDesktopPlanner desktop = new MyDesktopPlanner();
        Utilisateur user = desktop.Ethantifier(pseudo);
        if (user != null) {
            this.Pseudo = user.Pseudo;
            this.Calendrier = user.Calendrier;
            this.Projets = user.Projets;
            this.historiques = user.historiques;
            this.DureeMinCrenau =  user.DureeMinCrenau;
            this.nom = user.nom;
            this.prenom = user.prenom;
            this.NbTachesMin = user.NbTachesMin;
            return true;
        }
        return false;
    }

    /**
     * Creates a new user account with the specified username, last name, and first name.
     *
     * @param pseudo The username for the new account.
     * @param nom    The last name of the user.
     * @param prenom The first name of the user.
     * @return `true` if the account creation is successful, `false` otherwise.
     */
    public Boolean CreerCompte(String pseudo,String nom,String prenom) {
        Utilisateur user = new Utilisateur(pseudo,nom,prenom);
        MyDesktopPlanner desktop = new MyDesktopPlanner();
        if (desktop.creerCompte(user))
        {
            this.Pseudo = user.Pseudo;
            this.nom = user.nom;
            this.prenom = user.prenom;
            return true;
        }
        return false;
    }


    /**
     * Retrieves the names of all projects associated with the user.
     *
     * @return An ArrayList containing the names of the projects.
     */
    public ArrayList<String> projectsName (){
        ArrayList<String> projectsName = new ArrayList<>();
        for (Projet projet:getProjets())
        {
            projectsName.add(projet.getNom());
        }
        return projectsName;
    }
}
