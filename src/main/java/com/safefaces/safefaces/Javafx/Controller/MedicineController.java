package com.safefaces.safefaces.Javafx.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

/**
 * Controller for the medicine view
 * Handles navigation and medicine-related actions
 *
 * @author Shaima Almoayed
 */
public class MedicineController {
    @FXML
    private Button backButton;

    /**
     * Returns to the profile view
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void goBack(){
        try{
            FXMLLoader loader=new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/ProfileView.fxml")
            );
            Parent root=loader.load();
            Stage stage=(Stage)backButton.getScene().getWindow();
            stage.setScene(new Scene(root,400,700));
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Reads the user's medicines aloud
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void readMedicines(){
        System.out.println("Läser upp mediciner");
    }
}
