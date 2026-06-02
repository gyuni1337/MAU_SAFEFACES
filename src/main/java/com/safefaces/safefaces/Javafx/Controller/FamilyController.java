package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Javafx.App.AppState;
import javafx.fxml.FXML;

public class FamilyController {

    @FXML
    private void goBack() {
        AppState.getInstance().getMainController().loadView("/com/safefaces/safefaces/ProfileView.fxml");
    }

    @FXML
    private void readFamily() {
        System.out.println("Läser upp familjeträd");
    }
}
