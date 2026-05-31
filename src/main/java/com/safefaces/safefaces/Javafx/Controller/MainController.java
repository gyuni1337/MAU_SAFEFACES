package com.safefaces.safefaces.Javafx.Controller;
import com.safefaces.safefaces.Core.Model.Enums.RoleType;
import com.safefaces.safefaces.Javafx.App.AppState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * Main controller responsible for managing the primary application layout.
 * Handles dynamic view loading and coordinates navigation between different
 * sections such as Home, Reminders, and Profile.
 *
 * This controller acts as a central hub for switching views inside the content area
 * and initializing shared UI components like the bottom navigation bar.
 *
 * @author Gyundyuz Sadulov
 */
public class MainController {

    /** Container where views are dynamically loaded. */
    @FXML private AnchorPane contentArea;

    /** Container for the bottom navigation bar. */
    @FXML private AnchorPane bottomContainer;

    /**
     * Loads a specified FXML view into the content area.
     *
     * @param fxml the path to the FXML file to be loaded
     */
    public void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(fxml)
            );
            Parent view = loader.load();

            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            contentArea.getChildren().setAll(view);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the home view.
     *
     * @author Shaima Almoayed
     * @author Gyundyuz Sadulov
     */
    public void showHome() {
        bottomContainer.setVisible(true);
        bottomContainer.setManaged(true);
        loadView("/com/safefaces/safefaces/components/Contact.fxml");
    }

    /**
     * Displays the reminders view for regular users,
     * or the caregiver view for users with the CAREGIVER role.
     *
     * @author Gyundyuz Sadulov
     * @author Shaima Almoayed
     */
    public void showReminders() {
        bottomContainer.setVisible(true);
        bottomContainer.setManaged(true);
        var user = AppState.getInstance().getCurrentUser();
        if (user != null && user.role == RoleType.CAREGIVER) {
            loadView("/com/safefaces/safefaces/components/CaregiverView.fxml");
        } else {
            loadView("/com/safefaces/safefaces/components/Reminders.fxml");
        }
    }


    /**
     * Initializes the main layout after the FXML has been loaded.
     * Loads the bottom navigation bar and sets up communication
     * between this controller and the {@link BottomNavController}.
     * Also loads the default view (home).
     */
    @FXML
    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/components/BottomNav.fxml")
            );

            HBox nav = loader.load();
            nav.setPrefHeight(78);
            nav.setMinHeight(78);
            BottomNavController navController = loader.getController();
            navController.setMainController(this);

            AnchorPane.setTopAnchor(nav, 0.0);
            AnchorPane.setBottomAnchor(nav, 0.0);
            AnchorPane.setLeftAnchor(nav, 0.0);
            AnchorPane.setRightAnchor(nav, 0.0);

            bottomContainer.getChildren().setAll(nav);

//            System.out.println(nav.getPrefHeight());
//            System.out.println(bottomContainer.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }

        showHome();
    }

    /**
     * Displays the profile page in a separate scene
     *
     * @author Shaima Almoayed
     */
    public void showJournal(){
        try {
            FXMLLoader loader=new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/ProfileView.fxml"));

            Parent root=loader.load();
            javafx.stage.Stage stage=
                    (javafx.stage.Stage)contentArea.getScene().getWindow();

            stage.setScene(new javafx.scene.Scene(root, 400,700));
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}