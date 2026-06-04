package com.safefaces.safefaces.Javafx.Controller;

import javafx.fxml.FXML;

public class InformationController {

    @FXML
    private void goBack() {
        MainController.instance.showJournal();
    }

    @FXML
    private void readInformation() {
        System.out.println("Läser upp information");
    }
}
