package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Backend.Model.User;
import com.safefaces.safefaces.Backend.Service.AuthService;
import com.safefaces.safefaces.Javafx.App.AppState;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AuthenticationController {
    @FXML private Label statusLabel;
    @FXML private VBox pinBox;          // pin rutan, gömd tills FaceID "misslyckas"
    @FXML private PasswordField pinField;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        pinBox.setVisible(false);
        simulateFaceId();
    }

    //simulering, då gruppen kom överens om att formell faceid blir klurigt
    private void simulateFaceId() {
        statusLabel.setText("Scanning face...");
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            User user = authService.faceIdLogin();

            if (user != null) {
                AppState.getInstance().setCurrentUser(user);
                navigateToHome();
            } else {
                statusLabel.setText("FaceID failed. Enter pin: ");
                pinBox.setVisible(true);
            }
        }); pause.play();
    }

    @FXML
    private void handlePinLogin() {
        String pin = pinField.getText();

        try {
            User user = new com.safefaces.safefaces.Backend.Repository.UserRepository().getPatientUser();

            if (user != null) {
                String hashedInput = AuthService.hashPin(pin);

                if(hashedInput.equals(user.pinHash)) {
                    AppState.getInstance().setCurrentUser(user);
                    navigateToHome();
                } else {
                    statusLabel.setText("Wrong pin. Please try again.");
                    pinField.clear();
                }
            }
        } catch (Exception e) {
            statusLabel.setText("Database error: " + e.getMessage());
        }
    }

    private void navigateToHome() {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/MainView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 640));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
