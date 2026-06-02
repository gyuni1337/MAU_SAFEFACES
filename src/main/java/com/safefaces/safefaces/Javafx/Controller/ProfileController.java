package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Core.Model.Medication;
import com.safefaces.safefaces.Core.Model.Enums.RoleType;
import com.safefaces.safefaces.Core.Repository.CaregiverPatientRepository;
import com.safefaces.safefaces.Core.Repository.MedicationRepository;
import com.safefaces.safefaces.Javafx.App.AppState;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.List;

public class ProfileController {

    @FXML private Label nameLabel;
    @FXML private Label ageLabel;
    @FXML private Label locationLabel;
    @FXML private ImageView profileImage;
    @FXML private Label medsLabel;
    @FXML private VBox medsBox;
    @FXML private Button informationButton;
    @FXML private Button peopleButton;
    @FXML private Button overviewButton;
    @FXML private Button medicineButton;
    @FXML private Button healthButton;
    @FXML private Button familyButton;

    private final MedicationRepository medicationRepository = new MedicationRepository();
    private final CaregiverPatientRepository caregiverPatientRepo = new CaregiverPatientRepository();

    @FXML
    public void initialize() {
        System.out.println("ProfileView laddades");
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
        com.safefaces.safefaces.Javafx.App.SessionManager.logout();
    }

    @FXML
    private void toggleMeds() {
        boolean isVisible = medsBox.isVisible();
        medsBox.setVisible(!isVisible);
        medsBox.setManaged(!isVisible);
        medsLabel.setText(isVisible ? "Mediciner ◀" : "Mediciner ▼");
    }

    @FXML
    private void openInformation() {
        nav("/com/safefaces/safefaces/InformationView.fxml");
    }

    @FXML
    private void openMedicines() {
        nav("/com/safefaces/safefaces/MedicineView.fxml");
    }

    @FXML
    private void openHealth() {
        nav("/com/safefaces/safefaces/HealthView.fxml");
    }

    @FXML
    private void openOverview() {
        AppState.getInstance().getMainController().showHome();
    }

    @FXML
    private void openPeople() {
        AppState.getInstance().getMainController().showHome();
    }

    @FXML
    private void openProfile() {
        System.out.println("Profil klickad");
    }

    @FXML
    private void openFamily() {
        nav("/com/safefaces/safefaces/FamilyView.fxml");
    }

    @FXML
    private void openLifeStory() {
        nav("/com/safefaces/safefaces/LifeStoryView.fxml");
    }

    private void nav(String fxml) {
        AppState.getInstance().getMainController().loadView(fxml);
    }
}
