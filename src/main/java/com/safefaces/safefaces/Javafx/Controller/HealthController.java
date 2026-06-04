package com.safefaces.safefaces.Javafx.Controller;

import javafx.fxml.FXML;

public class HealthController {

    @FXML
    private void goBack() {
        MainController.instance.showJournal();
    }

    @FXML
    private void readHealth() {
        System.out.println("Läser upp hälsodata");
    }
}
