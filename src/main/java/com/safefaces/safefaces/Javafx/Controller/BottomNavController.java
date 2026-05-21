package com.safefaces.safefaces.Javafx.Controller;
import javafx.fxml.FXML;

/**
 * Controller class for the bottom navigation bar in the application.
 * Handles user interactions and delegates navigation actions
 * to the {@link MainController}.
 *
 * @author Anna Andersson
 * @author Erik Svensson
 */
public class BottomNavController {

    /** Reference to the main controller responsible for view switching. */
    private MainController mainController;

    /**
     * Sets the main controller used for handling navigation between views.
     *
     * @param mainController the main controller instance
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Navigates to the home view.
     * Triggered by user interaction in the UI.
     */
    @FXML
    private void goHome() {
        mainController.showHome();
    }

    /**
     * Navigates to the reminders view.
     * Triggered by user interaction in the UI.
     */
    @FXML
    private void goReminders() {
        mainController.showReminders();
    }

    /**
     * Navigates to the profile view.
     * Triggered by user interaction in the UI.
     */
    @FXML
    private void goJournal() {
        mainController.showJournal();
    }
}