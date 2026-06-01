package com.safefaces.safefaces.Javafx.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

/**
 * Controller for the SOS confirmation screen.
 * Handles confirming or cancelling an emergency call
 *
 * @author Shaima Almoayed
 */
public class SosConfirmController {
    @FXML
    private Button callButton;

    @FXML
    private Button cancelButton;

    /**
     * Opens the emergency call screen after
     * the user confirms the 112 call
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void startCall() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/SosCallView.fxml")

            );
            Parent root = loader.load();
            Stage stage = (Stage) callButton.getScene().getWindow();

            stage.setScene(new Scene(root, 400, 700));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cancels the emergency call and returns
     * the user to the home view.
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void cancel() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/HomeView.fxml")
            );

            Parent root = loader.load();
            Stage stage = (Stage) callButton.getScene().getWindow();


            stage.setScene(new Scene(root, 400, 700));
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

