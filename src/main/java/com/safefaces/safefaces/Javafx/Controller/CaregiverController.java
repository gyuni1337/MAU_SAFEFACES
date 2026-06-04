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
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.sql.Time;
import java.util.List;

public class CaregiverController {

    // patient picker + outer tab container
    @FXML private ComboBox<String> patientPicker;
    @FXML private VBox tabPane;

    // custom tab bar buttons
    @FXML private HBox tabReminders;
    @FXML private HBox tabContacts;
    @FXML private HBox tabMeds;

    // content panes
    @FXML private VBox reminderPane;
    @FXML private VBox contactPane;
    @FXML private VBox medPane;

    // reminder form
    @FXML private ComboBox<String> typePicker;
    @FXML private VBox reminderListBox;
    @FXML private TextField titleField;
    @FXML private TextField descField;
    @FXML private TextField timeField;
    @FXML private Label formStatus;

    // contact form
    @FXML private TextField contactNameField;
    @FXML private TextField contactRelationField;
    @FXML private TextField contactPhoneField;
    @FXML private Label contactStatus;
    @FXML private VBox contactListBox;

    // med form
    @FXML private TextField medNameField;
    @FXML private TextField medDoseField;
    @FXML private ComboBox<String> medTimePicker;
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
        medTimePicker.getItems().addAll("morgon", "middag", "kvall");

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
                switchTab(0);
            }
        });
    }

    // ── Tab switching ──

    @FXML private void showReminders() { switchTab(0); }
    @FXML private void showContacts()  { switchTab(1); }
    @FXML private void showMeds()      { switchTab(2); }

    private static final String ACTIVE_TAB =
            "-fx-background-color: white; -fx-background-radius: 12; -fx-padding: 10 0 10 0; " +
            "-fx-cursor: hand; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 6, 0, 0, 1);";
    private static final String INACTIVE_TAB =
            "-fx-background-color: transparent; -fx-background-radius: 12; -fx-padding: 10 0 10 0; -fx-cursor: hand;";
    private static final String ACTIVE_LABEL =
            "-fx-font-size: 13; -fx-font-family: 'Helvetica Neue'; -fx-font-weight: 600; -fx-text-fill: #1a6b3d;";
    private static final String INACTIVE_LABEL =
            "-fx-font-size: 13; -fx-font-family: 'Helvetica Neue'; -fx-font-weight: 600; -fx-text-fill: #6a9070;";

    private void switchTab(int index) {
        // reset all
        tabReminders.setStyle(INACTIVE_TAB);
        tabContacts.setStyle(INACTIVE_TAB);
        tabMeds.setStyle(INACTIVE_TAB);
        labelOf(tabReminders).setStyle(INACTIVE_LABEL);
        labelOf(tabContacts).setStyle(INACTIVE_LABEL);
        labelOf(tabMeds).setStyle(INACTIVE_LABEL);

        reminderPane.setVisible(false); reminderPane.setManaged(false);
        contactPane.setVisible(false);  contactPane.setManaged(false);
        medPane.setVisible(false);      medPane.setManaged(false);

        switch (index) {
            case 0 -> {
                tabReminders.setStyle(ACTIVE_TAB);
                labelOf(tabReminders).setStyle(ACTIVE_LABEL);
                reminderPane.setVisible(true); reminderPane.setManaged(true);
            }
            case 1 -> {
                tabContacts.setStyle(ACTIVE_TAB);
                labelOf(tabContacts).setStyle(ACTIVE_LABEL);
                contactPane.setVisible(true); contactPane.setManaged(true);
            }
            case 2 -> {
                tabMeds.setStyle(ACTIVE_TAB);
                labelOf(tabMeds).setStyle(ACTIVE_LABEL);
                medPane.setVisible(true); medPane.setManaged(true);
            }
        }
    }

    private Label labelOf(HBox tab) {
        return (Label) tab.getChildren().get(0);
    }

    // ── Påminnelser ──

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

        titleField.clear(); descField.clear(); timeField.clear();
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
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-padding: 14;" +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0, 0, 2);");

        // green icon badge
        VBox badge = badge("far-bell");

        VBox text = new VBox(4);
        HBox.setHgrow(text, Priority.ALWAYS);

        Label title = new Label(r.title);
        title.setStyle("-fx-font-size: 16; -fx-font-family: 'Helvetica Neue'; " +
                       "-fx-font-weight: 600; -fx-text-fill: #1a3d2e;");

        Label time = new Label(r.startTime != null ? "🕐  " + r.startTime.toString().substring(0, 5) : "");
        time.setStyle("-fx-font-size: 13; -fx-text-fill: #8aab90;");

        text.getChildren().addAll(title, time);
        if (r.description != null && !r.description.isBlank()) {
            Label desc = new Label(r.description);
            desc.setStyle("-fx-font-size: 13; -fx-text-fill: #888;");
            desc.setWrapText(true);
            text.getChildren().add(desc);
        }

        Button del = deleteButton();
        del.setOnAction(e -> {
            reminderRepo.deleteById(r.id);
            loadReminders(selectedPatient.id);
        });

        row.getChildren().addAll(badge, text, del);
        return row;
    }

    // ── Kontakter ──

    @FXML
    private void handleAddContact() {
        if (selectedPatient == null) return;

        String name  = contactNameField.getText().trim();
        String phone = contactPhoneField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            contactStatus.setText("Namn och telefon är obligatoriska.");
            return;
        }

        Contact contact = new Contact(name, phone, contactRelationField.getText().trim(), "emptyavatar.jpg", null);
        contactRepo.save(selectedPatient.id, contact);

        contactNameField.clear(); contactRelationField.clear(); contactPhoneField.clear();
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
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-padding: 14;" +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0, 0, 2);");

        VBox badge = badge("fas-address-book");

        VBox text = new VBox(4);
        HBox.setHgrow(text, Priority.ALWAYS);

        Label name = new Label(c.getName());
        name.setStyle("-fx-font-size: 16; -fx-font-family: 'Helvetica Neue'; " +
                      "-fx-font-weight: 600; -fx-text-fill: #1a3d2e;");

        Label sub = new Label(
                (c.getRelation() != null ? c.getRelation() : "") +
                (c.getPhoneNumber() != null ? "  •  " + c.getPhoneNumber() : ""));
        sub.setStyle("-fx-font-size: 13; -fx-text-fill: #8aab90;");

        text.getChildren().addAll(name, sub);

        Button del = deleteButton();
        del.setOnAction(e -> {
            contactRepo.deleteByName(patientId, c.getName());
            loadContacts(patientId);
        });

        row.getChildren().addAll(badge, text, del);
        return row;
    }

    // ── Mediciner ──

    @FXML
    private void handleAddMedication() {
        if (selectedPatient == null) return;

        String name = medNameField.getText().trim();
        String dose = medDoseField.getText().trim();
        String time = medTimePicker.getValue();

        if (name.isEmpty() || dose.isEmpty()) {
            medStatus.setText("Namn och dos är obligatoriska.");
            return;
        }

        Medication med = new Medication();
        med.name      = name;
        med.dose      = dose;
        med.timeOfDay = time;

        medicationRepo.save(selectedPatient.id, med);

        medNameField.clear(); medDoseField.clear();
        medTimePicker.getSelectionModel().clearSelection();
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
        HBox row = new HBox(14);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-padding: 14;" +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0, 0, 2);");

        VBox badge = badge("fas-pills");

        VBox text = new VBox(4);
        HBox.setHgrow(text, Priority.ALWAYS);

        Label name = new Label(m.name);
        name.setStyle("-fx-font-size: 16; -fx-font-family: 'Helvetica Neue'; " +
                      "-fx-font-weight: 600; -fx-text-fill: #1a3d2e;");

        Label sub = new Label(m.dose + (m.timeOfDay != null ? "  •  " + m.timeOfDay : ""));
        sub.setStyle("-fx-font-size: 13; -fx-text-fill: #8aab90;");

        text.getChildren().addAll(name, sub);

        Button del = deleteButton();
        del.setOnAction(e -> {
            medicationRepo.deactivateById(m.id);
            loadMedications(selectedPatient.id);
        });

        row.getChildren().addAll(badge, text, del);
        return row;
    }

    // ── Helpers ──

    private VBox badge(String icon) {
        VBox b = new VBox();
        b.setAlignment(Pos.CENTER);
        b.setStyle("-fx-background-color: #e8f5e9; -fx-background-radius: 14;" +
                   "-fx-min-width: 46; -fx-min-height: 46;" +
                   "-fx-pref-width: 46; -fx-pref-height: 46;");
        FontIcon fi = new FontIcon(icon);
        fi.setIconSize(20);
        fi.setIconColor(javafx.scene.paint.Color.web("#1a6b3d"));
        b.getChildren().add(fi);
        return b;
    }

    private Button deleteButton() {
        Button btn = new Button();
        FontIcon trash = new FontIcon("fas-trash-alt");
        trash.setIconSize(16);
        trash.setIconColor(javafx.scene.paint.Color.web("#c0392b"));
        btn.setGraphic(trash);
        btn.setStyle("-fx-background-color: #fdecea; -fx-background-radius: 50;" +
                     "-fx-min-width: 42; -fx-min-height: 42; -fx-cursor: hand; -fx-border-width: 0;");
        return btn;
    }

    private Label emptyLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-size: 15; -fx-text-fill: #8aab90; -fx-padding: 8;");
        return lbl;
    }
}
