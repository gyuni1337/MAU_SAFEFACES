package com.safefaces.safefaces.Javafx.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class BottomNavController {
    @FXML private HBox navBar;

    private void navigateTo(String fxmlPath) {

        try{
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/" + fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) navBar.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 700));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goHome() {
        System.out.println("home");
    }

    @FXML
    private void goContacts() {
        System.out.println("contacts");
    }

    @FXML
    private void goUser() {
        System.out.println("user");
    }
}
