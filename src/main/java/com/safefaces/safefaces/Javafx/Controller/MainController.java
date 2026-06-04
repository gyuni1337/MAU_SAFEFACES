package com.safefaces.safefaces.Javafx.Controller;
import com.safefaces.safefaces.Core.Model.Enums.RoleType;
import com.safefaces.safefaces.Javafx.App.AppState;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class MainController {

    public static MainController instance;

    @FXML private AnchorPane contentArea;

    @FXML private AnchorPane bottomContainer;

    @FXML private AnchorPane topBar;

    public void loadView(String fxml) {
        try {
            // SOS screens are full-screen mockups, so they should not inherit the normal header.
            boolean isSosView = fxml.endsWith("SosConfirmView.fxml") || fxml.endsWith("SosCallView.fxml");
            if (topBar != null) {
                topBar.setVisible(!isSosView);
                topBar.setManaged(!isSosView);
            }

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(fxml)
            );
            Parent view = loader.load();

            AnchorPane.setTopAnchor(view, 0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view, 0.0);
            AnchorPane.setRightAnchor(view, 0.0);
            contentArea.getChildren().setAll(view);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showHome() {
        loadView("/com/safefaces/safefaces/components/Contact.fxml");
    }

    public void showReminders() {
        var user = AppState.getInstance().getCurrentUser();
        if (user != null && user.role == RoleType.CAREGIVER) {
            loadView("/com/safefaces/safefaces/components/CaregiverView.fxml");
        } else {
            loadView("/com/safefaces/safefaces/components/Reminders.fxml");
        }
    }

    public void showJournal() {
        loadView("/com/safefaces/safefaces/components/Journal.fxml");
    }

    public void showInformation() {
        loadView("/com/safefaces/safefaces/components/InformationView.fxml");
    }

    public void showHealth() {
        loadView("/com/safefaces/safefaces/components/HealthView.fxml");
    }

    public void showMedicine() {
        loadView("/com/safefaces/safefaces/components/MedicineView.fxml");
    }

    public void showFamily() {
        loadView("/com/safefaces/safefaces/components/FamilyView.fxml");
    }

    public void showLifeStory() {
        loadView("/com/safefaces/safefaces/components/LifeStoryView.fxml");
    }

    public void showSosConfirm() {
        loadView("/com/safefaces/safefaces/components/SosConfirmView.fxml");
    }

    public void showSosCall() {
        loadView("/com/safefaces/safefaces/components/SosCallView.fxml");
    }

    @FXML
    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/components/BottomNav.fxml")
            );

            HBox nav = loader.load();
            nav.setPrefHeight(70);
            nav.setMinHeight(70);
            nav.setMaxHeight(70);
            BottomNavController navController = loader.getController();
            navController.setMainController(this);

            // Keep the nav floating above the background instead of flush with the window edge.
            AnchorPane.setBottomAnchor(nav, 10.0);
            AnchorPane.setLeftAnchor(nav, 12.0);
            AnchorPane.setRightAnchor(nav, 12.0);

            bottomContainer.getChildren().setAll(nav);
        } catch (Exception e) {
            e.printStackTrace();
        }

        instance = this;
        showHome();
    }
}
