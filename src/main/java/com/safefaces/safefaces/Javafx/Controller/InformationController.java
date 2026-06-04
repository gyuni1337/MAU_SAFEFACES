package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Core.Model.User;
import com.safefaces.safefaces.Javafx.App.AppState;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

public class InformationController {

    @FXML private Label nameInfoLabel;
    @FXML private Label ageInfoLabel;
    @FXML private Label locationInfoLabel;
    @FXML private Label jobInfoLabel;
    @FXML private VBox educationBox;
    @FXML private VBox workBox;

    @FXML
    public void initialize() {
        User user = AppState.getInstance().getCurrentUser();
        if (user == null) return;

        String fullName = user.lastName != null
                ? user.firstName + " " + user.lastName : user.firstName;
        nameInfoLabel.setText(fullName);
        ageInfoLabel.setText(user.age + " år");
        locationInfoLabel.setText(user.location != null ? user.location : "–");

        addEntry(educationBox, "fas-graduation-cap", "Skola / Utbildning", "");
        addEntry(workBox, "fas-briefcase", "Arbetsplats", "");
    }

    private void addEntry(VBox box, String iconLiteral, String title, String years) {
        HBox row = new HBox(12);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        row.setStyle("-fx-padding: 10 16 10 16;");

        FontIcon icon = FontIcon.of(FontAwesomeSolid.BRIEFCASE, 16);
        try { icon = new FontIcon(iconLiteral); icon.setIconSize(16); } catch (Exception ignored) {}
        icon.setIconColor(javafx.scene.paint.Color.web("#1a6b3d"));

        VBox text = new VBox(2);
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 15; -fx-text-fill: #1a3d2e;");
        text.getChildren().add(titleLabel);
        if (!years.isEmpty()) {
            Label yearsLabel = new Label(years);
            yearsLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #8aab90;");
            text.getChildren().add(yearsLabel);
        }

        row.getChildren().addAll(icon, text);
        box.getChildren().add(row);
    }

    @FXML private void goBack()          { MainController.instance.showJournal(); }
    @FXML private void readInformation() { System.out.println("Läser upp information"); }
}
