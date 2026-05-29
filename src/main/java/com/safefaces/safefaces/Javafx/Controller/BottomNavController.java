package com.safefaces.safefaces.Javafx.Controller;
import com.safefaces.safefaces.Core.Model.Enums.RoleType;
import com.safefaces.safefaces.Javafx.App.AppState;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Controller class for the bottom navigation bar in the application.
 * Handles user interactions and delegates navigation actions
 * to the {@link MainController}.
 *
 * @author Anna Andersson
 * @author Erik Svensson
 */
public class BottomNavController {

    @FXML private ImageView leftNavIcon;

    /** Reference to the main controller responsible for view switching. */
    private MainController mainController;

    @FXML
    public void initialize() {
        var user = AppState.getInstance().getCurrentUser();
        if (user != null && user.role == RoleType.CAREGIVER && leftNavIcon != null) {
            try {
                leftNavIcon.setImage(new Image(
                        getClass().getResourceAsStream(
                                "/com/safefaces/safefaces/images/contacts.png")));
            } catch (Exception e) {
                System.out.println("Kunde inte ladda caregiver-ikon.");
            }
        }
    }

    /**
     * Sets the main controller used for handling navigation between views.
     *
     * @param mainController the main controller instance
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Navigates to the home view.
     * Triggered by user interaction in the UI.
     */
    @FXML
    private void goHome() {
        mainController.showHome();
    }

    /**
     * Navigates to the reminders view.
     * Triggered by user interaction in the UI.
     */
    @FXML
    private void goReminders() {
        mainController.showReminders();
    }

    /**
     * Navigates to the profile view.
     * Triggered by user interaction in the UI.
     */
    @FXML
    private void goJournal() {
        mainController.showJournal();
    }
}