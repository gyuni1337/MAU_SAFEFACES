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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.List;

// Profilsidan — visar info om inloggad användare,
// beter sig lite olika beroende på om det är en vårdgivare eller vanlig user
public class ProfileController {

    @FXML private Label nameLabel;
    @FXML private Label ageLabel;
    @FXML private Label locationLabel;
    @FXML private Label locationSep;
    @FXML private ImageView profileImage;
    @FXML private Label medsLabel;
    @FXML private VBox medsBox;
    @FXML private Label roleBadge;
    @FXML private VBox caregiverStatsBox;
    @FXML private Label patientCountLabel;
    @FXML private Label reminderCountLabel;
    @FXML private HBox medCard;
    @FXML private HBox healthCard;
    @FXML private HBox familyCard;
    @FXML private HBox lifeCard;

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
        if (user.location != null && !user.location.isBlank()) {
            locationLabel.setText(user.location);
        } else {
            locationSep.setVisible(false);
            locationSep.setManaged(false);
            locationLabel.setVisible(false);
            locationLabel.setManaged(false);
        }

        try {
            Image image = new Image(
                    getClass().getResourceAsStream("/com/safefaces/safefaces/images/" + user.imagePath));
            profileImage.setImage(image);
            profileImage.setClip(new Circle(65, 65, 65));
        } catch (Exception e) {
            System.out.println("Kunde inte ladda profilbild.");
        }

        if (user.role == RoleType.CAREGIVER) {
            if (roleBadge != null) {
                roleBadge.setText("Vårdgivare");
                roleBadge.setStyle(roleBadge.getStyle() + "-fx-background-color: #4a90d9;");
            }
            if (medsLabel != null)  { medsLabel.setVisible(false);  medsLabel.setManaged(false); }
            if (medsBox != null)    { medsBox.setVisible(false);     medsBox.setManaged(false); }
            if (medCard != null)    { medCard.setVisible(false);     medCard.setManaged(false); }
            if (healthCard != null) { healthCard.setVisible(false);  healthCard.setManaged(false); }
            if (familyCard != null) { familyCard.setVisible(false);  familyCard.setManaged(false); }
            if (lifeCard != null)   { lifeCard.setVisible(false);    lifeCard.setManaged(false); }
            loadCaregiverStats(user.id);
        } else {
            if (roleBadge != null) {
                roleBadge.setText("Användare");
                roleBadge.setStyle(roleBadge.getStyle() + "-fx-background-color: #6abf69;");
            }
            loadMedications(user.id);
        }
    }

    private void loadCaregiverStats(int caregiverId) {
        if (roleBadge != null) {
            roleBadge.setVisible(true);
            roleBadge.setManaged(true);
        }

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
        if (medsBox == null) return;
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
        if (medsBox == null) return;
        boolean isVisible = medsBox.isVisible();
        medsBox.setVisible(!isVisible);
        medsBox.setManaged(!isVisible);
        if (medsLabel != null) medsLabel.setText(isVisible ? "Mediciner ➡️" : "Mediciner ⬇️");
    }

    @FXML private void openInformation() { MainController.instance.showInformation(); }
    @FXML private void openMedicines()   { MainController.instance.showMedicine(); }
    @FXML private void openHealth()      { MainController.instance.showHealth(); }
    @FXML private void openFamily()      { MainController.instance.showFamily(); }
    @FXML private void openLifeStory()   { MainController.instance.showLifeStory(); }
    @FXML private void openSos()         { MainController.instance.showSosConfirm(); }
}
