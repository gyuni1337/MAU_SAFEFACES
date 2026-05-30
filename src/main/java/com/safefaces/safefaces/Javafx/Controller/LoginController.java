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
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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


/**
 * Controller for the login screen.
 * Handles login as demo User and login as Vårdgivare (username + PIN).
 *
 * @author Noor Nabi
 * @author Gyundyuz Sadulov
 */
public class LoginController {

    @FXML
    private Label statusLabel;
    @FXML
    private VBox caregiverBox;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField pinField;
    @FXML
    private PauseTransition autoLoginTimer;
    @FXML
    private AnchorPane loginOverlay;
    @FXML
    private AnchorPane supportOverlay;
    @FXML
    private TextField supportEmailField;
    @FXML
    private TextArea supportMessageField;

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
        startAutoUserLogin();
        System.out.println(AuthService.hashPin("1234"));
        supportEmailField.focusedProperty().addListener((obs, oldVal,focused)->{
            if (focused){
                supportEmailField.setStyle("-fx-background-color: #cfeef6; - fx-border-color: #0fc6b0; -fx-text-fill: #00584f; -fx-font-size: 9; -fx-padding:0;");

            }else {
                supportEmailField.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: #00584f; -fx-font-size:9; -fx-padding: 0;");
            }
                });

    }

    /**
     * Logs in as the patient user fetched from the database.
     */
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

    /**
     * Starts a short delay before automatically triggering
     * the FaceID simulation for patient login
     * <p>
     * If the Vårdgivare login button is predded besfore the timer
     * finishes, this process is cancelled .
     *
     * @author Shaima Almoayed
     */
    private void startAutoUserLogin() {
        autoLoginTimer = new PauseTransition(Duration.seconds(3));

        autoLoginTimer.setOnFinished(e -> showFaceIdSimulation());
        autoLoginTimer.play();
        ;
    }

    /**
     * Displayer or hides the Vårdgivare login form
     * <p>
     * Stop automatic patient FaceID login if the caregiver
     * chooses to log in manually
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void handleShowCaregiverLogin() {
        if (autoLoginTimer != null) {
            autoLoginTimer.stop();
        }
        loginOverlay.setVisible(true);
        loginOverlay.setManaged(true);
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

    /**
     *
     * @param user
     * @author Shaima Almoayed
     * @author Noor Nabi
     */
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
            stage.setScene(new Scene(root, 400, 640));
            stage.show();
            SessionManager.start(stage);
        }catch (Exception e){
            e.printStackTrace();
            setStatus("Navifationsfel:"+ e.getMessage());
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

    /**
     * Close the caregiver login window
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void closeCaregiverPopup(){
        loginOverlay.setVisible(false);
        loginOverlay.setManaged(false);
    }

    /**
     * Opens the support window
     * Stops automatic login and displays the support form
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void handleShowSupport(){
        System.out.println("Support klickad");

        if(autoLoginTimer !=null){
            autoLoginTimer.stop();
        }
        supportOverlay.setVisible(true);
        supportOverlay.setManaged(true);
    }

    /**
     * Save the support massage and closes the support window
     * The massage is stored in a text file for later review
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void handleSendSupport(){
        String emil=supportEmailField.getText().trim() ;
        String massage=supportMessageField.getText().trim();

        if (emil.isEmpty() || massage.isEmpty()){
            return;
        }
        try {
            java.nio.file.Files.writeString(
                    java.nio.file.Path.of("support_messages.txt"),
                    "E-post:" + emil+ "\nMeddelande:" + massage +
                            "\n------------------------------\n",
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
            supportEmailField.clear();
            supportMessageField.clear();

            supportOverlay.setVisible(false);
            supportOverlay.setManaged(false);

            setStatus("Tack! Ditt meddelande har skickats.");
        }catch (Exception e){
            e.printStackTrace();
            setStatus("Kunde inte spara meddelandet.");
        }

    }

    /**
     * Closes the support popup window
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void closeSupportPopup(){
        loginOverlay.setVisible(false);
        loginOverlay.setManaged(false);
    }

    /**
     * Navigates to the contact list view after successful login
     * Loads the contact list interface and starts the user session 
     * 
     * @param stage the application stage
     * 
     * @author Shaima Almoayed
     */
    private void navigateToContactList(Stage stage){
        try {
            FXMLLoader loader=new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/ContactListView.fxml")
            );
            Parent root=loader.load();
            stage.setScene(new Scene(root,400,640));
            stage.show();

            SessionManager.start(stage);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
