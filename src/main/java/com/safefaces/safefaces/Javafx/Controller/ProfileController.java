package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Core.Model.Medication;
import com.safefaces.safefaces.Core.Model.User;
import com.safefaces.safefaces.Core.Model.Enums.RoleType;
import com.safefaces.safefaces.Core.Repository.MedicationRepository;
import com.safefaces.safefaces.Javafx.App.AppState;
import com.safefaces.safefaces.Javafx.App.SessionManager;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.List;

public class ProfileController {

    @FXML private Label nameLabel;
    @FXML private Label ageLabel;
    @FXML private Label locationLabel;
    @FXML private Label locationSep;
    @FXML private ImageView profileImage;
    @FXML private Label medsLabel;
    @FXML private VBox medsBox;
    @FXML private Label roleBadge;
    @FXML private HBox medCard;
    @FXML private HBox healthCard;
    @FXML private HBox familyCard;
    @FXML private HBox lifeCard;

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
            profileImage.setFitWidth(130);
            profileImage.setFitHeight(130);
            profileImage.setPreserveRatio(false);
            profileImage.setSmooth(true);
            profileImage.setViewport(centerSquareViewport(image));
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
        } else {
            if (roleBadge != null) {
                roleBadge.setText("Användare");
                roleBadge.setStyle(roleBadge.getStyle() + "-fx-background-color: #6abf69;");
            }
            loadMedications(user.id);
        }
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
            String text = med.name + " — " + med.dose + "  (" + toMedicationDisplayTime(med.timeOfDay) + ")";
            medsBox.getChildren().add(styledLabel(text, "#333", 16));
        }
    }

    private String toMedicationDisplayTime(String time) {
        if (time == null) return "";
        return switch (time) {
            case "MORNING" -> "morgon";
            case "NOON" -> "middag";
            case "EVENING" -> "kväll";
            case "NIGHT" -> "natt";
            case "AS_NEEDED" -> "vid behov";
            default -> time;
        };
    }

    private Label styledLabel(String text, String color, int size) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-size: " + size + "px; -fx-text-fill: " + color + ";");
        lbl.setWrapText(true);
        return lbl;
    }

    private Rectangle2D centerSquareViewport(Image image) {
        double size = Math.min(image.getWidth(), image.getHeight());
        double x = (image.getWidth() - size) / 2;
        double y = (image.getHeight() - size) / 2;
        return new Rectangle2D(x, y, size, size);
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
