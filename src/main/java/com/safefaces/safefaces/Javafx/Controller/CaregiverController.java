package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Core.Model.Contact;
import com.safefaces.safefaces.Core.Model.Medication;
import com.safefaces.safefaces.Core.Model.Reminder;
import com.safefaces.safefaces.Core.Model.User;
import com.safefaces.safefaces.Core.Repository.CaregiverPatientRepository;
import com.safefaces.safefaces.Core.Repository.ContactRepository;
import com.safefaces.safefaces.Core.Repository.MedicationRepository;
import com.safefaces.safefaces.Core.Repository.ReminderRepository;
import com.safefaces.safefaces.Javafx.App.AppState;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.Time;
import java.util.List;

// Vårdgivare-vyn — här kan man välja en patient och hantera
// deras påminnelser, kontakter och mediciner via tre tabbar
public class CaregiverController {

    // patient-dropdown + tabpane som visas när man valt någon
    @FXML private ComboBox<String> patientPicker;
    @FXML private TabPane tabPane;

    // reminder-tabben
    @FXML private ComboBox<String> typePicker;
    @FXML private VBox reminderListBox;
    @FXML private TextField titleField;
    @FXML private TextField descField;
    @FXML private TextField timeField;
    @FXML private Label formStatus;

    // kontakt-tabben
    @FXML private TextField contactNameField;
    @FXML private TextField contactRelationField;
    @FXML private TextField contactPhoneField;
    @FXML private Label contactStatus;
    @FXML private VBox contactListBox;

    // medicin-tabben
    @FXML private TextField medNameField;
    @FXML private TextField medDoseField;
    @FXML private TextField medTimeField;
    @FXML private Label medStatus;
    @FXML private VBox medListBox;

    private final CaregiverPatientRepository caregiverPatientRepo = new CaregiverPatientRepository();
    private final ContactRepository contactRepo = new ContactRepository();
    private final MedicationRepository medicationRepo = new MedicationRepository();
    private final ReminderRepository reminderRepo;

    private List<User> patients;
    private User selectedPatient;

    public CaregiverController() {
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
                tabPane.setVisible(true);
                tabPane.setManaged(true);
                loadReminders(selectedPatient.id);
                loadContacts(selectedPatient.id);
                loadMedications(selectedPatient.id);
            }
        });
    }

    // --- påminnelser ---

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
        reminder.reminderType = typePicker.getValue() != null ? typePicker.getValue() : "PERSONAL";

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
            reminderListBox.getChildren().add(emptyLabel("Inga aktiva påminnelser."));
            return;
        }

        for (Reminder r : reminders) {
            reminderListBox.getChildren().add(buildReminderRow(r));
        }
    }

    private HBox buildReminderRow(Reminder r) {
        HBox row = buildRow();

        VBox text = new VBox(4);
        HBox.setHgrow(text, Priority.ALWAYS);

        Label title = new Label(r.title);
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label time = new Label(r.startTime != null ? "🕐 " + r.startTime.toString().substring(0, 5) : "");
        time.setStyle("-fx-font-size: 13px; -fx-text-fill: #aaa;");

        text.getChildren().addAll(title, time);
        if (r.description != null && !r.description.isBlank()) {
            Label desc = new Label(r.description);
            desc.setStyle("-fx-font-size: 13px; -fx-text-fill: #888;");
            desc.setWrapText(true);
            text.getChildren().add(desc);
        }

        Button deleteBtn = deleteButton();
        deleteBtn.setOnAction(e -> {
            reminderRepo.deleteById(r.id);
            loadReminders(selectedPatient.id);
        });

        row.getChildren().addAll(text, deleteBtn);
        return row;
    }

    // --- kontakter ---

    @FXML
    private void handleAddContact() {
        if (selectedPatient == null) return;

        String name  = contactNameField.getText().trim();
        String phone = contactPhoneField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            contactStatus.setText("Namn och telefon är obligatoriska.");
            return;
        }

        Contact contact = new Contact(
                name,
                phone,
                contactRelationField.getText().trim(),
                "emptyavatar.jpg",
                null
        );
        contactRepo.save(selectedPatient.id, contact);

        contactNameField.clear();
        contactRelationField.clear();
        contactPhoneField.clear();
        contactStatus.setText("");

        loadContacts(selectedPatient.id);
    }

    private void loadContacts(int userId) {
        contactListBox.getChildren().clear();

        List<Contact> contacts = contactRepo.findByUserId(userId);

        if (contacts.isEmpty()) {
            contactListBox.getChildren().add(emptyLabel("Inga kontakter registrerade."));
            return;
        }

        for (Contact c : contacts) {
            contactListBox.getChildren().add(buildContactRow(c, userId));
        }
    }

    private HBox buildContactRow(Contact c, int patientId) {
        HBox row = buildRow();

        VBox text = new VBox(4);
        HBox.setHgrow(text, Priority.ALWAYS);

        Label name = new Label(c.getName());
        name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label sub = new Label((c.getRelation() != null ? c.getRelation() : "") +
                (c.getPhoneNumber() != null ? "  •  " + c.getPhoneNumber() : ""));
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #888;");

        text.getChildren().addAll(name, sub);

        Button deleteBtn = deleteButton();
        deleteBtn.setOnAction(e -> {
            contactRepo.deleteByName(patientId, c.getName());
            loadContacts(patientId);
        });

        row.getChildren().addAll(text, deleteBtn);
        return row;
    }

    // --- mediciner ---

    @FXML
    private void handleAddMedication() {
        if (selectedPatient == null) return;

        String name = medNameField.getText().trim();
        String dose = medDoseField.getText().trim();
        String time = medTimeField.getText().trim();

        if (name.isEmpty() || dose.isEmpty()) {
            medStatus.setText("Namn och dos är obligatoriska.");
            return;
        }

        Medication med = new Medication();
        med.name      = name;
        med.dose      = dose;
        med.timeOfDay = time.isEmpty() ? null : time;

        medicationRepo.save(selectedPatient.id, med);

        medNameField.clear();
        medDoseField.clear();
        medTimeField.clear();
        medStatus.setText("");

        loadMedications(selectedPatient.id);
    }

    private void loadMedications(int userId) {
        medListBox.getChildren().clear();

        List<Medication> meds = medicationRepo.findActiveByUserId(userId);

        if (meds.isEmpty()) {
            medListBox.getChildren().add(emptyLabel("Inga aktiva mediciner."));
            return;
        }

        for (Medication m : meds) {
            medListBox.getChildren().add(buildMedRow(m));
        }
    }

    private HBox buildMedRow(Medication m) {
        HBox row = buildRow();

        VBox text = new VBox(4);
        HBox.setHgrow(text, Priority.ALWAYS);

        Label name = new Label(m.name);
        name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label sub = new Label(m.dose + (m.timeOfDay != null ? "  •  " + m.timeOfDay : ""));
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #888;");

        text.getChildren().addAll(name, sub);

        Button deleteBtn = deleteButton();
        deleteBtn.setOnAction(e -> {
            medicationRepo.deactivateById(m.id);
            loadMedications(selectedPatient.id);
        });

        row.getChildren().addAll(text, deleteBtn);
        return row;
    }

    // --- helpers för att slippa copy-paste ---

    private HBox buildRow() {
        HBox row = new HBox(12);
        row.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 14;");
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        return row;
    }

    private Button deleteButton() {
        Button btn = new Button("🗑");
        btn.setStyle("-fx-background-color: #ffe0e0; -fx-background-radius: 50; " +
                "-fx-font-size: 16; -fx-min-width: 40; -fx-min-height: 40; -fx-cursor: hand;");
        return btn;
    }

    private Label emptyLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-size: 15px; -fx-text-fill: #888; -fx-padding: 8;");
        return lbl;
    }
}
