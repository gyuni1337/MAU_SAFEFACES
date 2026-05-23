package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Core.Model.User;
import com.safefaces.safefaces.Core.Service.AuthService;
import com.safefaces.safefaces.Javafx.App.AppState;
import com.safefaces.safefaces.Javafx.App.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for the login screen.
 * Handles login as demo User and login as Vårdgivare (username + PIN).
 *
 * @author Noor Nabi
 * @author Gyundyuz Sadulov
 */
public class LoginController {

    @FXML private Label statusLabel;
    @FXML private VBox caregiverBox;
    @FXML private TextField usernameField;
    @FXML private PasswordField pinField;
    @FXML private PauseTransition autoLoginTimer;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        // nothing to auto-start — user picks login method

        System.out.println(AuthService.hashPin("1234"));
    }

    /** Logs in as the patient user fetched from the database. */
    @FXML
    private void handleUserLogin() {
        setStatus("Loggar in...");
        Thread thread = new Thread(() -> {
            var user = authService.faceIdLogin();
            javafx.application.Platform.runLater(() -> {
                if (user == null) {
                    setStatus("Kunde inte hämta användare från databasen.");
                } else {
                    loginSuccessful(user);
                }
            });
        });
        thread.setDaemon(true);
        thread.start();
    }

    /** Toggles the caregiver login form. */
    @FXML
    private void handleShowCaregiverLogin() {
        boolean show = !caregiverBox.isVisible();
        caregiverBox.setVisible(show);
        caregiverBox.setManaged(show);
        setStatus("");
    }

    /** Submits the caregiver username + PIN to AuthService. */
    @FXML
    private void handleCaregiverSubmit() {
        String username = usernameField.getText().trim();
        String pin = pinField.getText().trim();

        if (username.isEmpty() || pin.isEmpty()) {
            setStatus("Fyll i användarnamn och PIN-kod.");
            return;
        }

        setStatus("Verifierar...");

        Thread thread = new Thread(() -> {
            User user = authService.login(username, pin);
            Platform.runLater(() -> {
                if (user == null) {
                    setStatus("Felaktigt användarnamn eller PIN-kod.");
                    pinField.clear();
                } else {
                    loginSuccessful(user);
                }
            });
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void loginSuccessful(User user) {
        AppState.getInstance().setCurrentUser(user);
        navigateToHome();
    }

    private void navigateToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/HomeView.fxml"));
            Parent root = loader.load();
            Stage stage = getStage();
            if (stage == null) return;
            stage.setScene(new Scene(root, 400, 640));
            stage.show();
            SessionManager.start(stage);
        } catch (Exception e) {
            setStatus("Navigationsfel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Stage getStage() {
        if (statusLabel != null && statusLabel.getScene() != null)
            return (Stage) statusLabel.getScene().getWindow();
        if (pinField != null && pinField.getScene() != null)
            return (Stage) pinField.getScene().getWindow();
        return null;
    }

    private void setStatus(String msg) {
        if (statusLabel != null) statusLabel.setText(msg);
    }
}
