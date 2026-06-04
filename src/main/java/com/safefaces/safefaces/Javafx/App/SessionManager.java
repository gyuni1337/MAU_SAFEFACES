package com.safefaces.safefaces.Javafx.App;

import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
public class SessionManager {
    private static final int MINUTES_BEFORE_TIMEOUT = 5;
    private static PauseTransition timeoutTimer;
    private static Stage mainStage;

    public static void start(Stage stage) {
        mainStage = stage;
        timeoutTimer = new PauseTransition(Duration.minutes(MINUTES_BEFORE_TIMEOUT));
        timeoutTimer.setOnFinished(e ->
                expireSession());
        timeoutTimer.play();
    }
    public static void beginSession() {
        if (timeoutTimer != null) {
            timeoutTimer.playFromStart();
        }
    }
    public static Stage getStage() {
        return mainStage;
    }
    public static void logout() {
        expireSession();
    }

    // Manual logout and inactivity timeout should leave the app in the same state.
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
    public static void stop() {
        if (timeoutTimer != null) {
            timeoutTimer.stop();
        }
    }
}
