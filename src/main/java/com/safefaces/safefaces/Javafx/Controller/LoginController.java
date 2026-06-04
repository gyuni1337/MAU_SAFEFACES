package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Core.Model.User;
import com.safefaces.safefaces.Core.Service.AuthService;
import com.safefaces.safefaces.Javafx.App.AppState;
import com.safefaces.safefaces.Javafx.App.SessionManager;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController {

    // Face ID overlay
    @FXML private AnchorPane faceIdOverlay;
    @FXML private Label statusLabel;

    // Caregiver overlay
    @FXML private AnchorPane loginOverlay;
    @FXML private TextField usernameField;
    @FXML private PasswordField pinField;
    @FXML private Label caregiverStatusLabel;

    // Support overlay
    @FXML private AnchorPane supportOverlay;
    @FXML private TextField supportEmailField;
    @FXML private TextArea supportMessageField;

    private final AuthService authService = new AuthService();
    private PauseTransition autoFaceIdLogin;
    private boolean loginInProgress;

    @FXML
    public void initialize() {
        System.out.println("Hashed Password: " + AuthService.hashPin("1234"));
        autoFaceIdLogin = new PauseTransition(Duration.seconds(2));
        autoFaceIdLogin.setOnFinished(e -> {
            show(faceIdOverlay);
            handleUserLogin();
        });
        autoFaceIdLogin.play();
    }

    // ── Face ID ──────────────────────────────────────────────────────────────

    @FXML
    private void handleShowFaceId() {
        stopAutoFaceIdLogin();
        show(faceIdOverlay);
    }

    @FXML
    private void closeFaceIdOverlay() {
        hide(faceIdOverlay);
        setStatus("");
    }

    @FXML
    private void handleUserLogin() {
        stopAutoFaceIdLogin();
        if (loginInProgress) return;
        loginInProgress = true;
        setStatus("Loggar in...");
        Thread thread = new Thread(() -> {
            User user = authService.faceIdLogin();
            Platform.runLater(() -> {
                if (user == null) {
                    loginInProgress = false;
                    setStatus("Kunde inte hämta användare från databasen.");
                } else {
                    loginSuccessful(user);
                }
            });
        });
        thread.setDaemon(true);
        thread.start();
    }

    // ── Caregiver ────────────────────────────────────────────────────────────

    @FXML
    private void handleShowCaregiverLogin() {
        stopAutoFaceIdLogin();
        show(loginOverlay);
        setCaregiverStatus("");
    }

    @FXML
    private void closeCaregiverPopup() {
        hide(loginOverlay);
        if (usernameField != null) usernameField.clear();
        if (pinField != null) pinField.clear();
    }

    @FXML
    private void handleCaregiverSubmit() {
        String username = usernameField.getText().trim();
        String pin      = pinField.getText().trim();

        if (username.isEmpty() || pin.isEmpty()) {
            setCaregiverStatus("Fyll i e-postadress och lösenord.");
            return;
        }

        setCaregiverStatus("Verifierar...");
        Thread thread = new Thread(() -> {
            User user = authService.login(username, pin);
            Platform.runLater(() -> {
                if (user == null) {
                    setCaregiverStatus("Felaktigt e-post eller lösenord.");
                    pinField.clear();
                } else {
                    loginSuccessful(user);
                }
            });
        });
        thread.setDaemon(true);
        thread.start();
    }

    // ── Support ──────────────────────────────────────────────────────────────

    @FXML
    private void handleShowSupport() {
        stopAutoFaceIdLogin();
        show(supportOverlay);
    }

    @FXML
    private void closeSupportPopup() {
        hide(supportOverlay);
    }

    @FXML
    private void handleSendSupport() {
        System.out.println("Support: " + supportEmailField.getText()
                + " / " + supportMessageField.getText());
        hide(supportOverlay);
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private void show(AnchorPane pane) {
        if (pane == null) return;
        pane.setVisible(true);
        pane.setManaged(true);
    }

    private void hide(AnchorPane pane) {
        if (pane == null) return;
        pane.setVisible(false);
        pane.setManaged(false);
    }

    private void setStatus(String msg) {
        if (statusLabel != null) statusLabel.setText(msg);
    }

    private void setCaregiverStatus(String msg) {
        if (caregiverStatusLabel != null) caregiverStatusLabel.setText(msg);
    }

    private void stopAutoFaceIdLogin() {
        if (autoFaceIdLogin != null) {
            autoFaceIdLogin.stop();
        }
    }

    private void loginSuccessful(User user) {
        stopAutoFaceIdLogin();
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
            stage.setScene(new Scene(root, 400, 700));
            stage.show();
            SessionManager.start(stage);
        } catch (Exception e) {
            setStatus("Navigationsfel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Stage getStage() {
        if (faceIdOverlay != null && faceIdOverlay.getScene() != null)
            return (Stage) faceIdOverlay.getScene().getWindow();
        if (pinField != null && pinField.getScene() != null)
            return (Stage) pinField.getScene().getWindow();
        return null;
    }
}
