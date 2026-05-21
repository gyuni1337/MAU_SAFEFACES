package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Core.Model.Reminder;
import com.safefaces.safefaces.Core.Model.User;
import com.safefaces.safefaces.Core.Repository.CaregiverPatientRepository;
import com.safefaces.safefaces.Core.Repository.ReminderRepository;
import com.safefaces.safefaces.Javafx.App.AppState;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.Time;
import java.util.List;

/**
 * Controller for the caregiver view.
 * Lets a caregiver pick one of their assigned patients and manage reminders for them.
 *
 * @author Gyundyuz Sadulov
 */
public class CaregiverController {

    @FXML private ComboBox<String> patientPicker;
    @FXML private ComboBox<String> typePicker;
    @FXML private VBox formBox;
    @FXML private VBox reminderListBox;
    @FXML private TextField titleField;
    @FXML private TextField descField;
    @FXML private TextField timeField;
    @FXML private Label formStatus;

    private final CaregiverPatientRepository caregiverPatientRepo = new CaregiverPatientRepository();
    private final ReminderRepository reminderRepo;

    private List<User> patients;
    private User selectedPatient;

    public CaregiverController() {
        // userId placeholder — replaced in initialize() with real caregiver id
        reminderRepo = new ReminderRepository(0);
    }

    @FXML
    public void initialize() {
        typePicker.getItems().addAll("PERSONAL", "CAREGIVER", "MEDICATION");

        int caregiverId = AppState.getInstance().getCurrentUser().getId();
        patients = caregiverPatientRepo.findPatientsByCaregiver(caregiverId);

        if (patients.isEmpty()) {
            patientPicker.setPromptText("Inga patienter tilldelade");
            patientPicker.setDisable(true);
            return;
        }

        for (User p : patients) {
            String label = p.firstName + (p.lastName != null ? " " + p.lastName : "");
            patientPicker.getItems().add(label);
        }

        patientPicker.setOnAction(e -> {
            int idx = patientPicker.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                selectedPatient = patients.get(idx);
                formBox.setVisible(true);
                formBox.setManaged(true);
                loadReminders(selectedPatient.id);
            }
        });
    }

    @FXML
    private void handleAddReminder() {
        if (selectedPatient == null) return;

        String title = titleField.getText().trim();
        String time  = timeField.getText().trim();

        if (title.isEmpty() || time.isEmpty()) {
            formStatus.setText("Titel och tid är obligatoriska.");
            return;
        }

        Time startTime;
        try {
            if (!time.matches("\\d{2}:\\d{2}")) throw new IllegalArgumentException();
            startTime = Time.valueOf(time + ":00");
        } catch (Exception e) {
            formStatus.setText("Ogiltigt tidsformat. Använd HH:MM.");
            return;
        }

        Reminder reminder = new Reminder();
        reminder.title        = title;
        reminder.description  = descField.getText().trim();
        reminder.startTime    = startTime;
        reminder.reminderType = typePicker.getValue() != null
                ? typePicker.getValue() : "PERSONAL";

        reminderRepo.save(selectedPatient.id, reminder);

        titleField.clear();
        descField.clear();
        timeField.clear();
        typePicker.getSelectionModel().clearSelection();
        formStatus.setText("");

        loadReminders(selectedPatient.id);
    }

    private void loadReminders(int userId) {
        reminderListBox.getChildren().clear();

        ReminderRepository repo = new ReminderRepository(userId);
        List<Reminder> reminders = repo.getActiveReminders();

        if (reminders.isEmpty()) {
            Label empty = new Label("Inga aktiva påminnelser.");
            empty.setStyle("-fx-font-size: 15px; -fx-text-fill: #888; -fx-padding: 8;");
            reminderListBox.getChildren().add(empty);
            return;
        }

        for (Reminder r : reminders) {
            reminderListBox.getChildren().add(buildReminderRow(r));
        }
    }

    private HBox buildReminderRow(Reminder r) {
        HBox row = new HBox(12);
        row.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 14;");
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        VBox text = new VBox(4);
        HBox.setHgrow(text, Priority.ALWAYS);

        Label title = new Label(r.title);
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label time = new Label(r.startTime != null
                ? "🕐 " + r.startTime.toString().substring(0, 5) : "");
        time.setStyle("-fx-font-size: 13px; -fx-text-fill: #aaa;");

        text.getChildren().addAll(title, time);
        if (r.description != null && !r.description.isBlank()) {
            Label desc = new Label(r.description);
            desc.setStyle("-fx-font-size: 13px; -fx-text-fill: #888;");
            desc.setWrapText(true);
            text.getChildren().add(desc);
        }

        Button deleteBtn = new Button("🗑");
        deleteBtn.setStyle("-fx-background-color: #ffe0e0; -fx-background-radius: 50; " +
                "-fx-font-size: 16; -fx-min-width: 40; -fx-min-height: 40; -fx-cursor: hand;");
        deleteBtn.setOnAction(e -> {
            reminderRepo.deleteById(r.id);
            loadReminders(selectedPatient.id);
        });

        row.getChildren().addAll(text, deleteBtn);
        return row;
    }
}
