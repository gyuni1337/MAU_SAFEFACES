package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Backend.Model.Enums.RoleType;
import com.safefaces.safefaces.Backend.Model.User;
import com.safefaces.safefaces.Backend.Service.AuthService;
import com.safefaces.safefaces.Javafx.App.AppState;
import com.safefaces.safefaces.Javafx.App.SessionManager;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller class responsible for handling user authentication UI logic.
 * Supports login via Face ID, fingerprint, and PIN code.
 *
 * This class coordinates between the UI and the {@link AuthService},
 * manages asynchronous login operations, and handles navigation
 * after successful authentication.
 *
 * @author Noor Nabi
 * @author Gyundyuz Sadulov
 */
public class LoginController {

    @FXML private Label statusLabel;
    @FXML private VBox fingerprintBox;
    @FXML private Button fingerprintBtn;
    @FXML private VBox pinBox;
    @FXML private PasswordField pinField;

    /** Service used for authentication logic. */
    private final AuthService authService = new AuthService();

    /** Timestamp used to measure initialization time. */
    private final long initTime = System.currentTimeMillis();

    /**
     * Initializes the login view after the FXML is loaded.
     * Sets up the UI and starts the simulated Face ID process.
     */
    @FXML
    public void initialize() {
        if (fingerprintBox != null) fingerprintBox.setVisible(false);
        if (fingerprintBtn != null) {
            fingerprintBtn.setVisible(false);
            fingerprintBtn.setMinWidth(48);
            fingerprintBtn.setMinHeight(48);
        }
        if (pinBox != null) pinBox.setVisible(false);

        setStatus("Scanning face...");
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> fetchUserInBackground());
        pause.play();
    }

    /**
     * Fetches user data asynchronously to simulate Face ID authentication.
     * Avoids blocking the JavaFX UI thread.
     */
    private void fetchUserInBackground() {
        setStatus("Verifierar...");

        Thread dbThread = new Thread(() -> {
            User user = authService.faceIdLogin();

            Platform.runLater(() -> {
                if (user == null) {
                    System.out.println("DB returned null. Demo user in use.");
                    loginSuccessful(createDemoUser());
                } else {
                    loginSuccessful(user);
                }
            });
        });

        dbThread.setDaemon(true);
        dbThread.start();
    }

    /**
     * Handles fingerprint login action triggered by the user.
     * Simulates scanning and then performs authentication.
     */
    @FXML
    private void handleFingerprintLogin() {
        setStatus("Scanning fingerprint...");
        if (fingerprintBtn != null) fingerprintBtn.setDisable(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(event ->
                fetchUserInBackground());
        pause.play();
    }

    /**
     * Handles login using a PIN code entered by the user.
     * Validates input and checks the hashed PIN against stored data.
     */
    @FXML
    private void handlePinLogin() {
        if (pinField == null) return;
        String pin = pinField.getText().trim();
        if (pin.isEmpty()) {
            setStatus("Ange PIN-kod.");
            return;
        }

        Thread dbThread = new Thread(() -> {
            User user = authService.faceIdLogin();
            Platform.runLater(() -> {
                if (user == null) {
                    if ("1234".equals(pin)) {
                        loginSuccessful(createDemoUser());
                    } else {
                        setStatus("Felaktig PIN-kod. Vänligen försök igen.");
                        pinField.clear();
                    }
                    return;
                }
                String hashedInput = AuthService.hashPin(pin);
                if (hashedInput.equals(user.pinHash)) {
                    loginSuccessful(user);
                } else {
                    setStatus("Wrong pin code. Try again.");
                    pinField.clear();
                }
            });
        });
        dbThread.setDaemon(true);
        dbThread.start();
    }

    /**
     * Handles successful login by storing the user in the application state
     * and navigating to the home view.
     *
     * @param user the authenticated {@link User}
     */
    private void loginSuccessful(User user) {
        AppState.getInstance().setCurrentUser(user);
        long elapsed = System.currentTimeMillis() - initTime;
        navigateToHome();
    }

    /**
     * Navigates to the home view after successful login.
     * Initializes the session manager for inactivity tracking.
     */
    private void navigateToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/HomeView.fxml"));
            Parent root = loader.load();
            Stage stage = getStage();
            if (stage == null) {
                return;
            }

            stage.setScene(new Scene(root, 400, 640));
            stage.show();
            SessionManager.start(stage);
        } catch (Exception e) {
            setStatus("Nivigation err: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the current application stage from available UI components.
     *
     * @return the current {@link Stage}, or {@code null} if not available
     */
    private Stage getStage() {
        if (statusLabel != null && statusLabel.getScene() != null)
            return (Stage) statusLabel.getScene().getWindow();

        if (pinField != null && pinField.getScene() != null)
            return (Stage) pinField.getScene().getWindow();

        if (fingerprintBtn != null && fingerprintBtn.getScene() != null)
            return (Stage) fingerprintBtn.getScene().getWindow();

        return null;
    }

    /**
     * Updates the status label in the UI and logs the message.
     *
     * @param msg the message to display
     */
    private void setStatus(String msg) {
        if (statusLabel != null) statusLabel.setText(msg);
        System.out.println("Status: " + msg);
    }

    /**
     * Creates a demo user used when no user is returned from the database.
     *
     * @return a mock {@link User} object
     */
    private User createDemoUser() {
        User demo = new User();
        demo.id        = 1;
        demo.firstName = "Henry";
        demo.imagePath = "oldmanexample.jpg";
        demo.role      = RoleType.USER;
        demo.pinHash   = "";
        return demo;
    }
}