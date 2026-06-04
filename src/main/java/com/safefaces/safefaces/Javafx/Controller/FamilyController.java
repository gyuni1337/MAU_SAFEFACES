package com.safefaces.safefaces.Javafx.Controller;

import javafx.fxml.FXML;

public class FamilyController {

    @FXML
    private void goBack() {
        MainController.instance.showJournal();
    }

    @FXML
    private void readFamily() {
        System.out.println("Läser upp familjeträd");
    }
}
