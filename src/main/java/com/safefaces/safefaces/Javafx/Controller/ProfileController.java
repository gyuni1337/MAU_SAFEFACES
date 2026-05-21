package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Core.Model.Medication;
import com.safefaces.safefaces.Core.Model.User;
import com.safefaces.safefaces.Core.Repository.MedicationRepository;
import com.safefaces.safefaces.Javafx.App.AppState;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.List;

/**
 * Controller for the profile/journal view.
 * Displays user info, location, and medications fetched from the database.
 *
 * @author Emma Yousif
 */
public class ProfileController {

    @FXML private Label nameLabel;
    @FXML private Label ageLabel;
    @FXML private Label locationLabel;
    @FXML private ImageView profileImage;
    @FXML private Label medsLabel;
    @FXML private VBox medsBox;

    private final MedicationRepository medicationRepository = new MedicationRepository();

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

        loadMedications(user.id);
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
    private void toggleMeds() {
        boolean isVisible = medsBox.isVisible();
        medsBox.setVisible(!isVisible);
        medsBox.setManaged(!isVisible);
        medsLabel.setText(isVisible ? "Mediciner ◀" : "Mediciner ▼");
    }
}
