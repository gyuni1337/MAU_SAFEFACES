package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Core.Model.Medication;
import com.safefaces.safefaces.Core.Model.User;
import com.safefaces.safefaces.Core.Model.Enums.RoleType;
import com.safefaces.safefaces.Core.Repository.CaregiverPatientRepository;
import com.safefaces.safefaces.Core.Repository.MedicationRepository;
import com.safefaces.safefaces.Core.Repository.ReminderRepository;
import com.safefaces.safefaces.Javafx.App.AppState;
import com.safefaces.safefaces.Javafx.App.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.List;

// Profilsidan — visar info om inloggad användare,
// beter sig lite olika beroende på om det är en vårdgivare eller vanlig user
public class ProfileController {

    @FXML private Label nameLabel;
    @FXML private Label ageLabel;
    @FXML private Label locationLabel;
    @FXML private ImageView profileImage;
    @FXML private Label medsLabel;
    @FXML private VBox medsBox;
    @FXML private Label roleBadge;
    @FXML private VBox caregiverStatsBox;
    @FXML private Label patientCountLabel;
    @FXML private Label reminderCountLabel;

    private final MedicationRepository medicationRepository = new MedicationRepository();
    private final CaregiverPatientRepository caregiverPatientRepo = new CaregiverPatientRepository();

    @FXML
    public void initialize() {
        User user = AppState.getInstance().getCurrentUser();

        if (user == null) {
            nameLabel.setText("Ingen användare");
            ageLabel.setText("");
            return;
        }

        String fullName = user.lastName != null
                ? user.firstName + " " + user.lastName
                : user.firstName;
        nameLabel.setText(fullName);
        ageLabel.setText("Ålder: " + user.age);
        locationLabel.setText(user.location != null ? "Du är hemma i " + user.location : "");

        try {
            Image image = new Image(
                    getClass().getResourceAsStream("/com/safefaces/safefaces/images/" + user.imagePath));
            profileImage.setTranslateX(-15);
            profileImage.setImage(image);
            profileImage.setClip(new Circle(60, 60, 60));
        } catch (Exception e) {
            System.out.println("Kunde inte ladda profilbild.");
        }

        if (user.role == RoleType.CAREGIVER) {
            roleBadge.setText("Vårdgivare");
            roleBadge.setStyle(roleBadge.getStyle() + "-fx-background-color: #4a90d9;");
            medsLabel.setVisible(false);
            medsLabel.setManaged(false);
            medsBox.setVisible(false);
            medsBox.setManaged(false);
            loadCaregiverStats(user.id);
        } else {
            roleBadge.setText("Användare");
            roleBadge.setStyle(roleBadge.getStyle() + "-fx-background-color: #6abf69;");
            loadMedications(user.id);
        }
    }

    private void loadCaregiverStats(int caregiverId) {
        roleBadge.setVisible(true);
        roleBadge.setManaged(true);

        List<User> patients = caregiverPatientRepo.findPatientsByCaregiver(caregiverId);
        int patientCount = patients.size();

        int totalReminders = 0;
        for (User p : patients) {
            totalReminders += new ReminderRepository(p.id).getActiveReminders().size();
        }

        patientCountLabel.setText("👥  " + patientCount + (patientCount == 1 ? " patient" : " patienter"));
        reminderCountLabel.setText("🔔  " + totalReminders + " aktiva påminnelser totalt");

        caregiverStatsBox.setVisible(true);
        caregiverStatsBox.setManaged(true);
    }

    private void loadMedications(int userId) {
        List<Medication> meds = medicationRepository.findActiveByUserId(userId);
        medsBox.getChildren().clear();

        if (meds.isEmpty()) {
            medsBox.getChildren().add(styledLabel("Inga aktiva mediciner.", "#888", 15));
            return;
        }

        for (Medication med : meds) {
            String text = med.name + " — " + med.dose + "  (" + med.timeOfDay + ")";
            medsBox.getChildren().add(styledLabel(text, "#333", 16));
        }
    }

    private Label styledLabel(String text, String color, int size) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-size: " + size + "px; -fx-text-fill: " + color + ";");
        lbl.setWrapText(true);
        return lbl;
    }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
    }

    @FXML
    private void toggleMeds() {
        boolean isVisible = medsBox.isVisible();
        medsBox.setVisible(!isVisible);
        medsBox.setManaged(!isVisible);
        medsLabel.setText(isVisible ? "Mediciner ▶" : "Mediciner ▼");
    }

    @FXML private void openInformation() { MainController.instance.showInformation(); }
    @FXML private void openMedicines()   { MainController.instance.showMedicine(); }
    @FXML private void openHealth()      { MainController.instance.showHealth(); }
    @FXML private void openFamily()      { MainController.instance.showFamily(); }
    @FXML private void openLifeStory()   { MainController.instance.showLifeStory(); }
    @FXML private void openSos()         { MainController.instance.showSosConfirm(); }
}
