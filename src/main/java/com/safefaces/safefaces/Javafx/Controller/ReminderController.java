package com.safefaces.safefaces.Javafx.Controller;
import com.safefaces.safefaces.Backend.Model.Reminder;
import com.safefaces.safefaces.Backend.Service.ReminderService;
import com.safefaces.safefaces.Javafx.App.AppState;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;


public class ReminderController {
    @FXML private VBox reminderListBox;

    private ReminderService reminderService;

    @FXML
    public void initialize(){
        int userId = AppState.getInstance().getCurrentUser().getId();

        reminderService = new ReminderService(userId);
        buildReminderList();

    }
    private void buildReminderList(){
        if(reminderListBox == null)
            return;

        reminderListBox.getChildren().clear();
        List<Reminder> reminders = reminderService.getActiveReminders();


        if(reminders.isEmpty()){
            Label empty = new Label("Inga påminnelser just nu.");
            empty.setStyle("-fx-font-size:16; -fx-text-fill:#888; -fx-padding: 20;");

            reminderListBox.getChildren().add(empty);
            return;
        }
        for(Reminder reminder : reminders){
            reminderListBox.getChildren().add(buildRow(reminder));
        }

    }
    private HBox buildRow(Reminder reminder){
        HBox row = new HBox(16);
        row.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 16;
                -fx-padding:16;
                -fx-effect:dropshadow(gaussian,rgba(0,0,0,0.06), 8, 0, 0, 2);
                """);
        row.setAlignment(Pos.CENTER_LEFT);

        Label icon =new Label("\uD83D\uDCCB");
        icon.setStyle("-fx-font-size:28");
        VBox textBox = new VBox(4);
        HBox.setHgrow(textBox,Priority.ALWAYS);

        Label titelLabel = new Label(reminder.title);
        titelLabel.setStyle("-fx-font-size:17; -fx-font-weight:bold");

        Label descLabel = new Label(
        reminder.description != null &&! reminder.description.isBlank() ? reminder.description : "");

        descLabel.setStyle("-fx-font-size:13;-fx-text-fill:#888");
        descLabel.setWrapText(true);

        Label timeLabel = new Label(
                reminder.startTime != null ? "\uD83D\uDD50" + reminder.startTime.toString().substring(0,5)
                        :"");
        timeLabel.setStyle("-fx-font-size:13;-fx-text-fill:#aaa");
        textBox.getChildren().addAll(titelLabel,descLabel,timeLabel);
        row.getChildren().addAll(icon,textBox);
        return row;

    }

}
