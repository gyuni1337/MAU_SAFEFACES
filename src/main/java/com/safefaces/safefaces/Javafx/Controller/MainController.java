package com.safefaces.safefaces.Javafx.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
public class MainController {

    @FXML private AnchorPane contentArea;
    @FXML private AnchorPane bottomContainer;

    public void loadView(String fxml) {
        try {
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
        loadView("/com/safefaces/safefaces/components/Reminders.fxml");
    }

    public void showProfile() {
        loadView("/com/safefaces/safefaces/components/Profile.fxml");
    }

    @FXML
    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/components/BottomNav.fxml")
            );

            HBox nav = loader.load();
            nav.setPrefHeight(78);
            nav.setMinHeight(78);
            BottomNavController navController = loader.getController();
            navController.setMainController(this);

            AnchorPane.setTopAnchor(nav, 0.0);
            AnchorPane.setBottomAnchor(nav, 0.0);
            AnchorPane.setLeftAnchor(nav, 0.0);
            AnchorPane.setRightAnchor(nav, 0.0);

            bottomContainer.getChildren().setAll(nav);

            System.out.println(nav.getPrefHeight());
            System.out.println(bottomContainer.getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }

        showHome();
    }
}