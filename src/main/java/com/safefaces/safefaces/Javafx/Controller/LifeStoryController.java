package com.safefaces.safefaces.Javafx.Controller;

import javafx.fxml.FXML;

public class LifeStoryController {

    @FXML
    private void goBack() {
        MainController.instance.showJournal();
    }

    @FXML
    private void readLifeStory() {
        System.out.println("Läser upp livsberättelse");
    }
}
