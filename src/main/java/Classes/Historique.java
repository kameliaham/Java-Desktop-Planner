package main.java.Classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Stack;

public class Historique implements Serializable {
    private Stack<UserCalendar> calendarStack;

    public Historique() {
        calendarStack = new Stack<>();
    }

        /**
         * Checks if the end date of a calendar period is equal to today's date and adds it to the stack.
         *
         * @param calendar The UserCalendar object to be checked and added.
         */
        public void addToHistorique(UserCalendar calendar) {
            LocalDate today = LocalDate.now();
            if (calendar.getPeriode().getFin().equals(today)) {
                calendarStack.push(calendar);
            }
        }

        /**
         * Adds a UserCalendar to the stack.
         *
         * @param calendar The UserCalendar object to be added.
         */
        public void addCalendar(UserCalendar calendar) {
            calendarStack.push(calendar);
        }

        /**
         * Removes the top UserCalendar from the stack.
         *
         * @return The removed UserCalendar object, or null if the stack is empty.
         */
        public UserCalendar removeCalendar() {
            if (!calendarStack.isEmpty()) {
                return calendarStack.pop();
            }
            return null;
        }

        /**
         * Searches for a UserCalendar in the stack based on its period.
         *
         * @param startDate The start date of the UserCalendar's period.
         * @param endDate   The end date of the UserCalendar's period.
         * @return The UserCalendar object if found, or null if not found.
         */
        public UserCalendar findCalendarByPeriod(LocalDate startDate, Locale endDate) {
            for (UserCalendar calendar : calendarStack) {
                if (calendar.getPeriode().getDebut().equals(startDate) && calendar.getPeriode().getFin().equals(endDate)) {
                    return calendar;
                }
            }
            return null;
        }


}



