package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Core.Model.Medication;
import com.safefaces.safefaces.Core.Repository.MedicationRepository;
import com.safefaces.safefaces.Javafx.App.AppState;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class MedicineController {

    @FXML private VBox morningSection;
    @FXML private VBox morningBox;
    @FXML private VBox lunchSection;
    @FXML private VBox lunchBox;
    @FXML private VBox eveningSection;
    @FXML private VBox eveningBox;
    @FXML private Label emptyLabel;

    private final MedicationRepository repo = new MedicationRepository();

    // Reused across sections so the medicine rows do not all look identical.
    private static final String[] BG   = {"#dbeafe", "#fff3cd", "#ede9fe"};
    private static final Color[]  CLR  = {Color.web("#2563eb"), Color.web("#d97706"), Color.web("#7c3aed")};

    @FXML
    public void initialize() {
        var user = AppState.getInstance().getCurrentUser();
        if (user == null) { showEmpty(); return; }

        List<Medication> meds = repo.findActiveByUserId(user.id);
        if (meds.isEmpty()) { showEmpty(); return; }

        int idx = 0;
        for (Medication med : meds) {
            String time = med.timeOfDay == null ? "" : med.timeOfDay.toLowerCase();
            Color pillColor = CLR[idx % CLR.length];
            String pillBg   = BG[idx % BG.length];
            idx++;

            if (time.contains("morgon") || time.contains("morning")) {
                morningBox.getChildren().add(buildRow(med, pillBg, pillColor));
                show(morningSection);
            } else if (time.contains("lunch") || time.contains("middag") || time.contains("noon")) {
                lunchBox.getChildren().add(buildRow(med, pillBg, pillColor));
                show(lunchSection);
            } else if (time.contains("kväll") || time.contains("kvall") || time.contains("evening")) {
                eveningBox.getChildren().add(buildRow(med, pillBg, pillColor));
                show(eveningSection);
            } else {
                // Unknown values are still shown instead of disappearing from the patient's view.
                morningBox.getChildren().add(buildRow(med, pillBg, pillColor));
                show(morningSection);
            }
        }
    }

    private HBox buildRow(Medication med, String bg, Color iconColor) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-padding: 12 16 12 16;");

        VBox iconBox = new VBox();
        iconBox.setAlignment(Pos.CENTER);
        iconBox.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 12;"
                + " -fx-min-width: 44; -fx-min-height: 44;"
                + " -fx-pref-width: 44; -fx-pref-height: 44;");
        FontIcon icon = new FontIcon("fas-capsules");
        icon.setIconSize(22);
        icon.setIconColor(iconColor);
        iconBox.getChildren().add(icon);

        VBox text = new VBox(3);
        Label name = new Label(med.name + (med.dose != null ? "  " + med.dose : ""));
        name.setStyle("-fx-font-size: 15; -fx-font-family: 'Helvetica Neue'; -fx-font-weight: 600; -fx-text-fill: #1a3d2e;");
        Label dose = new Label(med.dose != null ? med.dose : "");
        dose.setStyle("-fx-font-size: 13; -fx-font-family: 'Helvetica Neue'; -fx-text-fill: #8aab90;");
        text.getChildren().addAll(name, dose);

        row.getChildren().addAll(iconBox, text);
        return row;
    }

    private void showEmpty() {
        if (emptyLabel != null) { emptyLabel.setVisible(true); emptyLabel.setManaged(true); }
    }

    private void show(VBox section) {
        section.setVisible(true);
        section.setManaged(true);
    }

    @FXML private void goBack()       { MainController.instance.showJournal(); }
    @FXML private void readMedicines(){ System.out.println("Läser upp mediciner"); }
}
