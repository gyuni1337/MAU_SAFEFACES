package com.safefaces.safefaces.Javafx.Controller;

import javafx.fxml.FXML;

public class SosCallController {

    @FXML
    private void endCall() {
        MainController.instance.showHome();
    }
}
