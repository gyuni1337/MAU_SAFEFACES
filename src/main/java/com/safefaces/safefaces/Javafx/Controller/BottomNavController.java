
package com.safefaces.safefaces.Javafx.Controller;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
public class BottomNavController {

    private MainController mainController;

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
    private void goProfile() {
        mainController.showProfile();
    }
}