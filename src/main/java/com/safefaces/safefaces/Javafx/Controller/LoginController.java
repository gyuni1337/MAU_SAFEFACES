package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Core.Model.User;
import com.safefaces.safefaces.Core.Service.AuthService;
import com.safefaces.safefaces.Javafx.App.AppState;
import com.safefaces.safefaces.Javafx.App.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextArea;

import java.nio.file.StandardOpenOption;


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

    /**
     * initialize the login controller
     * Starts automatic Face ID login for the patient user
     * unless caregiver login is selected
     *
     * @author Shaima Almoayed
     */
    @FXML
    public void initialize() {
        System.out.println("Hashed Password: " + AuthService.hashPin("1234"));
    }

    // ── Face ID ──────────────────────────────────────────────────────────────

    @FXML
    private void handleShowFaceId() {
        show(faceIdOverlay);
    }

    @FXML
    private void closeFaceIdOverlay() {
        hide(faceIdOverlay);
        setStatus("");
    }

    @FXML
    private void handleUserLogin() {
        setStatus("Loggar in...");
        Thread thread = new Thread(() -> {
            User user = authService.faceIdLogin();
            Platform.runLater(() -> {
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

    // ── Caregiver ────────────────────────────────────────────────────────────

    @FXML
    private void handleShowCaregiverLogin() {
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

    private void loginSuccessful(User user) {
        AppState.getInstance().setCurrentUser(user);
        Stage stage=getStage();
        if(stage !=null){
            navigateToHome(stage);
        }
    }

    /**
     * Displays the FaceID simulation screen
     * <p>
     * simulation a Face ID scan before automatically loggin in
     * the user and navigating to the home view
     *
     * @author Shaima Almoayed
     */
    private void showFaceIdSimulation(){
        Stage stage=getStage();
        if(stage==null)return;

        AnchorPane root=new AnchorPane();

        ImageView faceId=new ImageView(new Image(getClass().getResourceAsStream("/com/safefaces/safefaces/images/face_id_simulation.png")));

        faceId.setFitWidth(400);
        faceId.setFitHeight(640);
        faceId.setPreserveRatio(false);

        root.getChildren().addAll(faceId);
        Button closeButton=new Button();

        closeButton.setLayoutX(330);
        closeButton.setLayoutY(30);

        closeButton.setPrefWidth(45);
        closeButton.setPrefHeight(45);

        closeButton.setStyle("-fx-background-color:transparent;");
        closeButton.setOnAction(e->{
            try {

            FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/safefaces/safefaces/LoginView.fxml"));

            Parent loginView=loader.load();
            stage.setScene(new Scene(loginView,400,640));
            }catch (Exception ex) {
                ex.printStackTrace();
            }
            });

        root.getChildren().add(closeButton);
        stage.setScene(new Scene(root,400,640));

        PauseTransition scanDely=new PauseTransition(Duration.seconds(6));

        scanDely.setOnFinished(e->{
            Thread thread=new Thread(()->{
                User user=authService.faceIdLogin();

                Platform.runLater(()->{
                    if(user==null){
                        setStatus("Kunde inte logga in:");
                    } else{
                        AppState.getInstance().setCurrentUser(user);
                        navigateToHome(stage);
                    }
                });
            });
            thread.setDaemon(true);
            thread.start();
        });
        scanDely.play();
    }

    /**
     * Navigares to the home view after login
     *
     * @param stage the current application stage
     * @author Shaima Almoayed
     * @author Noor Nabi
     */
    private void navigateToHome(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/HomeView.fxml"));
            Parent root = loader.load();
            Stage stage = getStage();
            if (stage == null) return;
            stage.setScene(new Scene(root, 400, 700));
            stage.show();
            SessionManager.start(stage);
        }catch (Exception e){
            e.printStackTrace();
            setStatus("Navifationsfel:"+ e.getMessage());
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
