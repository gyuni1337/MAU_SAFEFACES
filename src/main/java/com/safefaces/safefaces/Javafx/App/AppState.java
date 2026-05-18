package com.safefaces.safefaces.Javafx.App;

import com.safefaces.safefaces.Backend.Model.User;

/**
 * Singleton class that manages the global application state.
 * Stores information about the currently authenticated user
 * and provides utility methods to access and update this state.
 *
 * This class ensures that only one instance of AppState exists
 * throughout the application lifecycle.
 *
 * @author Gyundyuz Sadulov
 * @author Noor Nabi
 */
public class AppState {

    /** Singleton instance of AppState. */
    private static AppState instance;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private AppState() {
    }

    /**
     * Returns the single instance of AppState.
     * Creates the instance if it does not already exist.
     *
     * @return the singleton instance of {@link AppState}
     */
    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }

        return instance;
    }

    /** The currently authenticated user. */
    private User currentUser;

    /**
     * Returns the currently logged-in user.
     *
     * @return the current {@link User}, or {@code null} if no user is logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current user of the application.
     *
     * @param currentUser the user to set as logged-in
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    /**
     * Checks whether a user is currently logged in.
     *
     * @return {@code true} if a user is logged in, otherwise {@code false}
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Logs out the current user by clearing the application state.
     */
    public void logout() {
        currentUser = null;
    }
}
