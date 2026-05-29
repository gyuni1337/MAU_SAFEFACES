package com.safefaces.safefaces.Javafx.App;

import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Manages user session lifecycle and timeout behavior in the application.
 * Automatically logs out the user after a period of inactivity and redirects
 * to the login view.
 *
 * This class uses a timer-based mechanism to track inactivity
 * and ensures the application returns to a secure state when the session expires.
 *
 * @author Noor Nabi
 */
public class SessionManager {

    /** Number of minutes before the session times out. */
    private static final int MINUTES_BEFORE_TIMEOUT = 5;

    /** Timer used to track session inactivity. */
    private static PauseTransition timeoutTimer;

    /** Reference to the main application stage. */
    private static Stage mainStage;

    /**
     * Initializes the session manager and starts the timeout timer.
     *
     * @param stage the main application stage used for scene switching
     */
    public static void start(Stage stage) {
        mainStage = stage;
        timeoutTimer = new PauseTransition(Duration.minutes(MINUTES_BEFORE_TIMEOUT));
        timeoutTimer.setOnFinished(e ->
                expireSession());
        timeoutTimer.play();
    }

    /**
     * Starts or resets the session timer.
     * Should be called when user activity is detected.
     */
    public static void beginSession() {
        if (timeoutTimer != null) {
            timeoutTimer.playFromStart();
        }
    }

    /**
     * Returns the main application stage.
     *
     * @return the primary {@link Stage} of the application
     */
    public static Stage getStage() {
        return mainStage;
    }

    /**
     * Logs out the current user immediately and redirects to the login view.
     */
    public static void logout() {
        expireSession();
    }

    /**
     * Expires the current session by logging out the user
     * and redirecting to the login view.
     */
    private static void expireSession() {
        AppState.getInstance().logout();
        try {
            FXMLLoader loader = new FXMLLoader(
                    SessionManager.class.getResource(
                            "/com/safefaces/safefaces/LoginView.fxml"));
            Parent root = loader.load();
            mainStage.setScene(new Scene(root, 400, 640));
            mainStage.show();
            System.out.println("User logged out.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the session timer.
     * Should be called when the application is closed
     * or when session tracking is no longer needed.
     */
    public static void stop() {
        if (timeoutTimer != null) {
            timeoutTimer.stop();
        }
    }
}