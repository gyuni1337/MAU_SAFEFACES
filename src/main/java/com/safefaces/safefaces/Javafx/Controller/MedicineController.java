package com.safefaces.safefaces.Javafx.Controller;

import javafx.fxml.FXML;

public class MedicineController {

    @FXML
    private void goBack() {
        MainController.instance.showJournal();
    }

    @FXML
    private void readMedicines() {
        System.out.println("Läser upp mediciner");
    }
}
