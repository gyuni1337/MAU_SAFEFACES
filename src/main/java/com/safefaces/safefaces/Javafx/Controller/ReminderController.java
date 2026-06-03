package com.safefaces.safefaces.Javafx.Controller;
import com.safefaces.safefaces.Core.Model.Reminder;
import com.safefaces.safefaces.Core.Service.ReminderService;
import com.safefaces.safefaces.Javafx.App.AppState;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controller class responsible for managing the reminders view.
 * Retrieves active reminders from the backend and dynamically
 * displays them in the UI.
 *
 * This class interacts with {@link ReminderService} to access data
 * and constructs JavaFX components for each reminder.
 *
 * @author Hamdi Ahmed
 */
public class ReminderController {

    /**
     * Container for displaying the list of reminders.
     */
    @FXML
    private VBox reminderListBox;

    /**
     * Service used to fetch reminder data.
     */
    private ReminderService reminderService;

    /**
     * Initializes the controller after the FXML file has been loaded.
     * Retrieves the current user and populates the reminder list.
     */
    @FXML
    public void initialize() {
        int userId = AppState.getInstance().getCurrentUser().getId();

        reminderService = new ReminderService(userId);
        buildReminderList();
    }

    /**
     * Builds the reminder list UI by fetching active reminders
     * and creating visual rows for each entry.
     */
    private void buildReminderList() {
        if (reminderListBox == null)
            return;

        reminderListBox.getChildren().clear();
        List<Reminder> reminders = reminderService.getActiveReminders();


        if (reminders.isEmpty()) {
            Label empty = new Label("Inga påminnelser just nu.");
            empty.setStyle("-fx-font-size:16; -fx-text-fill:#888; -fx-padding: 20;");

            reminderListBox.getChildren().add(empty);
            return;
        }
        for (Reminder reminder : reminders) {
            reminderListBox.getChildren().add(buildRow(reminder));
        }
    }

    /**
     * Creates a UI row representing a single reminder.
     *
     * @param reminder the reminder to display
     * @return an {@link HBox} containing the formatted reminder information
     */
    private HBox buildRow(Reminder reminder) {
        HBox row = new HBox(16);
        row.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 16;
                -fx-padding:16;
                -fx-effect:dropshadow(gaussian,rgba(0,0,0,0.06), 8, 0, 0, 2);
                """);
        row.setAlignment(Pos.CENTER_LEFT);

        Label icon = new Label("\uD83D\uDCCB");
        icon.setStyle("-fx-font-size:28");
        VBox textBox = new VBox(4);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Label titelLabel = new Label(reminder.title);
        titelLabel.setStyle("-fx-font-size:17; -fx-font-weight:bold");

        Label descLabel = new Label(
                reminder.description != null && !reminder.description.isBlank() ? reminder.description : "");

        descLabel.setStyle("-fx-font-size:13;-fx-text-fill:#888");
        descLabel.setWrapText(true);

        Label timeLabel = new Label(
                reminder.startTime != null ? "\uD83D\uDD50" + reminder.startTime.toString().substring(0, 5)
                        : "");
        timeLabel.setStyle("-fx-font-size:13;-fx-text-fill:#aaa");
        textBox.getChildren().addAll(titelLabel, descLabel, timeLabel);
        row.getChildren().addAll(icon, textBox);
        return row;
    }

    @FXML
    private void openPeople() {
        loadPage("/com/safefaces/safefaces/HomeView.fxml");
    }

    @FXML
    private void openProfile() {
        loadPage("/com/safefaces/safefaces/ProfileView.fxml");
    }

    @FXML
    private void openReminders() {
    }

    private void loadPage(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) reminderListBox.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 700));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
