package com.safefaces.safefaces.Javafx.Controller;

import javafx.fxml.FXML;

public class SosConfirmController {

    @FXML
    private void startCall() {
        MainController.instance.showSosCall();
    }

    @FXML
    private void cancel() {
        MainController.instance.showHome();
    }
}
