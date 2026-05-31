package com.safefaces.safefaces.Javafx.App;

import com.safefaces.safefaces.Javafx.View.LoginView;
import javafx.application.Application;

/**
 * Entry point for the JavaFX application.
 * Launches the application by starting the {@link LoginView}.
 *
 * This class is responsible for initializing the JavaFX runtime.
 *
 * @author Gyundyuz Sadulov
 * @author Noor Nabi
 */
public class Launcher {

    /**
     * Main method that starts the JavaFX application.
     *
     * @param args command-line arguments passed to the application
     */
    static void main(String[] args) {
     Application.launch(LoginView.class, args);
   }
}
