package com.safefaces.safefaces.Javafx.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class BottomNavController {
    @FXML private HBox navBar;

    @FXML
    private void goHome() {
        System.out.println("home");
    }

    @FXML
    private void goReminders() {
        System.out.println("reminders");
    }

    @FXML
    private void goProfile() {
        System.out.println("user");
    }
}
