package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Core.Model.Enums.RoleType;
import com.safefaces.safefaces.Javafx.App.AppState;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class BottomNavController {

    @FXML private FontIcon remindersIcon;
    @FXML private FontIcon homeIcon;
    @FXML private FontIcon journalIcon;
    @FXML private Label remindersLabel;
    @FXML private Label homeLabel;
    @FXML private Label journalLabel;

    private static final Color ACTIVE   = Color.web("#1a6b3d");
    private static final Color INACTIVE = Color.web("#8a8a8a");

    private static final String ACTIVE_LABEL   = "-fx-font-size: 11; -fx-text-fill: #1a6b3d;";
    private static final String INACTIVE_LABEL = "-fx-font-size: 11; -fx-text-fill: #8a8a8a;";

    private MainController mainController;

    @FXML
    public void initialize() {
        var user = AppState.getInstance().getCurrentUser();
        if (user != null && user.role == RoleType.CAREGIVER) {
            remindersIcon.setIconLiteral("fas-user-cog");
            remindersLabel.setText("Admin");
            homeLabel.setText("Patienter");
        }
        setActive(1);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setActive(int index) {
        remindersIcon.setIconColor(INACTIVE);
        homeIcon.setIconColor(INACTIVE);
        journalIcon.setIconColor(INACTIVE);
        remindersLabel.setStyle(INACTIVE_LABEL);
        homeLabel.setStyle(INACTIVE_LABEL);
        journalLabel.setStyle(INACTIVE_LABEL);

        switch (index) {
            case 0 -> { remindersIcon.setIconColor(ACTIVE); remindersLabel.setStyle(ACTIVE_LABEL); }
            case 1 -> { homeIcon.setIconColor(ACTIVE);      homeLabel.setStyle(ACTIVE_LABEL); }
            case 2 -> { journalIcon.setIconColor(ACTIVE);   journalLabel.setStyle(ACTIVE_LABEL); }
        }
    }

    @FXML private void goHome()      { setActive(1); mainController.showHome(); }
    @FXML private void goReminders() { setActive(0); mainController.showReminders(); }
    @FXML private void goJournal()   { setActive(2); mainController.showJournal(); }
}
