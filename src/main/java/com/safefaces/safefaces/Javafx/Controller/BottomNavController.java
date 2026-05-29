package com.safefaces.safefaces.Javafx.Controller;
import com.safefaces.safefaces.Core.Model.Enums.RoleType;
import com.safefaces.safefaces.Javafx.App.AppState;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BottomNavController {

    @FXML private ImageView leftNavIcon;

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

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void goHome() {
        mainController.showHome();
    }

    @FXML
    private void goReminders() {
        mainController.showReminders();
    }

    @FXML
    private void goJournal() {
        mainController.showJournal();
    }
}
