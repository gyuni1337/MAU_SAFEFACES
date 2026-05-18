package com.safefaces.safefaces.Backend.Controller;

import com.safefaces.safefaces.Javafx.App.AppState;

/**
 * Controller class responsible for user authentication.
 * Handles login operations and connects them to the application's state.
 *
 * @author Gyundyuz Sadulov
 */
public class AuthController {

    private AppState appState;

    /**
     * Constructs an AuthController and links it to the application state.
     *
     * @param state the current application state used to store session
     *              and authenticated user information
     */
    public AuthController(AppState state) {
        appState = state;
    }


    /**
     * Performs login for a user using the provided username and PIN.
     *
     * @param username the user's username
     * @param pin the user's PIN code
     */
    public void login(String username, String pin) {
//        User currentUser = AuthService.login(username, pin);
    }
}
