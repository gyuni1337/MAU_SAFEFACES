package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Core.Model.Reminder;
import com.safefaces.safefaces.Core.Service.ReminderService;
import com.safefaces.safefaces.Javafx.App.AppState;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class ReminderController {

    @FXML private VBox reminderListBox;

    private ReminderService reminderService;

    @FXML
    public void initialize() {
        int userId = AppState.getInstance().getCurrentUser().getId();
        reminderService = new ReminderService(userId);
        buildReminderList();
    }

    private void buildReminderList() {
        if (reminderListBox == null) return;
        reminderListBox.getChildren().clear();

        List<Reminder> reminders = reminderService.getActiveReminders();

        if (reminders.isEmpty()) {
            Label empty = new Label("Inga påminnelser just nu.");
            empty.setStyle("-fx-font-size: 15; -fx-text-fill: #8aab90; -fx-padding: 20;");
            reminderListBox.getChildren().add(empty);
            return;
        }

        for (Reminder reminder : reminders) {
            reminderListBox.getChildren().add(buildRow(reminder));
        }
    }

    private HBox buildRow(Reminder reminder) {
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-padding: 14;" +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0, 0, 2);");

        // green icon badge
        VBox badge = new VBox();
        badge.setAlignment(Pos.CENTER);
        badge.setStyle("-fx-background-color: #e8f5e9; -fx-background-radius: 14;" +
                       "-fx-min-width: 46; -fx-min-height: 46;" +
                       "-fx-pref-width: 46; -fx-pref-height: 46;");
        FontIcon bell = new FontIcon("far-bell");
        bell.setIconSize(20);
        bell.setIconColor(javafx.scene.paint.Color.web("#1a6b3d"));
        badge.getChildren().add(bell);

        // text
        VBox textBox = new VBox(4);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Label titleLabel = new Label(reminder.title);
        titleLabel.setStyle("-fx-font-size: 16; -fx-font-family: 'Helvetica Neue';" +
                            "-fx-font-weight: 600; -fx-text-fill: #1a3d2e;");

        Label timeLabel = new Label(reminder.startTime != null
                ? "🕐  " + reminder.startTime.toString().substring(0, 5) : "");
        timeLabel.setStyle("-fx-font-size: 13; -fx-text-fill: #8aab90;");

        textBox.getChildren().addAll(titleLabel, timeLabel);

        if (reminder.description != null && !reminder.description.isBlank()) {
            Label desc = new Label(reminder.description);
            desc.setStyle("-fx-font-size: 13; -fx-text-fill: #888;");
            desc.setWrapText(true);
            textBox.getChildren().add(desc);
        }

        // delete button
        Button deleteBtn = new Button();
        FontIcon trash = new FontIcon("fas-trash-alt");
        trash.setIconSize(16);
        trash.setIconColor(javafx.scene.paint.Color.web("#c0392b"));
        deleteBtn.setGraphic(trash);
        deleteBtn.setStyle("-fx-background-color: #fdecea; -fx-background-radius: 50;" +
                           "-fx-min-width: 42; -fx-min-height: 42; -fx-cursor: hand; -fx-border-width: 0;");
        deleteBtn.setOnAction(e -> {
            reminderService.deleteById(reminder.id);
            buildReminderList();
        });

        row.getChildren().addAll(badge, textBox, deleteBtn);
        return row;
    }
}
