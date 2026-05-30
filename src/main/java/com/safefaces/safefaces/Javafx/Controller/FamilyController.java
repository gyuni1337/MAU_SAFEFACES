package com.safefaces.safefaces.Javafx.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

/**
 * Controller for the FamilyController
 *
 * @author Shaima Almoayed
 */
public class FamilyController {
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
     * Reads the family information aloud
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void readFamily(){
        System.out.println("Läser upp familjeträd");
    }
}


