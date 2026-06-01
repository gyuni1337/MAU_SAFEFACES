package com.safefaces.safefaces.Javafx.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller for the emergency call screen.
 * Handles ending the emergency call and returning
 * the user to the home vie
 *
 * @author Shaima Almoayed
 */
public class SosCallController {

    @FXML
    private Button endCallButton;

    /**
     * Ends the emergency call and returns
     * the user to the home screen.
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void endCall() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/HomeView.fxml")
            );
            Parent root = loader.load();
            Stage stage = (Stage) endCallButton.getScene().getWindow();

            stage.setScene(new Scene(root, 400, 700));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
