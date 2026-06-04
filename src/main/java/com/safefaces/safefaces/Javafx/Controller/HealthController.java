package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Core.Model.HealthEntry;
import com.safefaces.safefaces.Core.Repository.HealthRepository;
import com.safefaces.safefaces.Javafx.App.AppState;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class HealthController {

    @FXML private VBox diagnosBox;
    @FXML private VBox noteringBox;
    @FXML private VBox behandlingBox;
    @FXML private VBox diagnosSection;
    @FXML private VBox noteringSection;
    @FXML private VBox behandlingSection;
    @FXML private Label emptyLabel;

    private final HealthRepository repo = new HealthRepository();
    private int userId;

    @FXML
    public void initialize() {
        userId = AppState.getInstance().getCurrentUser().getId();
        load();
    }

    private void load() {
        boolean hasEntries = false;
        hasEntries |= loadSection("diagnos",    diagnosSection,    diagnosBox);
        hasEntries |= loadSection("notering",   noteringSection,   noteringBox);
        hasEntries |= loadSection("behandling", behandlingSection, behandlingBox);

        if (emptyLabel != null) {
            emptyLabel.setVisible(!hasEntries);
            emptyLabel.setManaged(!hasEntries);
        }
    }

    private boolean loadSection(String category, VBox section, VBox listBox) {
        List<HealthEntry> entries = repo.findByUserAndCategory(userId, category);
        listBox.getChildren().clear();

        if (entries.isEmpty()) {
            section.setVisible(false);
            section.setManaged(false);
            return false;
        }

        section.setVisible(true);
        section.setManaged(true);

        for (HealthEntry e : entries) {
            listBox.getChildren().add(buildRow(e, category));
        }
        return true;
    }

    private HBox buildRow(HealthEntry entry, String category) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-padding: 10 16 10 16;");

        String iconLiteral = switch (category) {
            case "diagnos"    -> "fas-heartbeat";
            case "notering"   -> "fas-clipboard-list";
            case "behandling" -> "fas-capsules";
            default           -> "fas-circle";
        };
        String iconBg = switch (category) {
            case "diagnos"    -> "#fde8e8";
            case "notering"   -> "#e3f0fc";
            case "behandling" -> "#e8f5e9";
            default           -> "#e8f5e9";
        };
        String iconColor = switch (category) {
            case "diagnos"    -> "#e53935";
            case "notering"   -> "#1565c0";
            case "behandling" -> "#1a6b3d";
            default           -> "#1a6b3d";
        };

        VBox iconBox = new VBox();
        iconBox.setAlignment(Pos.CENTER);
        iconBox.setStyle("-fx-background-color: " + iconBg + "; -fx-background-radius: 8;" +
                         "-fx-min-width: 32; -fx-min-height: 32;" +
                         "-fx-pref-width: 32; -fx-pref-height: 32;");
        FontIcon icon = new FontIcon(iconLiteral);
        icon.setIconSize(16);
        icon.setIconColor(javafx.scene.paint.Color.web(iconColor));
        iconBox.getChildren().add(icon);

        Label text = new Label(entry.title);
        text.setStyle("-fx-font-size: 15; -fx-text-fill: #1a3d2e;");
        text.setWrapText(true);
        HBox.setHgrow(text, Priority.ALWAYS);

        row.getChildren().addAll(iconBox, text);
        return row;
    }

    @FXML private void goBack()     { MainController.instance.showJournal(); }
    @FXML private void readHealth() { System.out.println("Läser upp hälsodata"); }
}
