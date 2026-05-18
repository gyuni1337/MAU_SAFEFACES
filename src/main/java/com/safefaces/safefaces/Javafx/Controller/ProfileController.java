package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Javafx.App.AppState;
import com.safefaces.safefaces.Backend.Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

/**
 * Controller class responsible for displaying and managing
 * the user's profile view.
 *
 * Retrieves the current user from {@link AppState} and updates
 * the UI with user information such as name, age, and profile image.
 * Also manages interaction with additional UI components such as
 * the medications section.
 *
 * @author Emma Yousif
 */
public class ProfileController {

    /** Label displaying the user's full name. */
    @FXML
    private Label nameLabel;

    /** Label displaying the user's age. */
    @FXML
    private Label ageLabel;

    /** ImageView displaying the user's profile picture. */
    @FXML
    private ImageView profileImage;

    /**
     * Initializes the profile view after the FXML has been loaded.
     * Retrieves the currently logged-in user and populates the UI
     * with user details. If no user is found, default values are shown.
     */
    @FXML
    public void initialize() {
        User user = AppState.getInstance().getCurrentUser();

        if (user != null) {
            nameLabel.setText(user.firstName + " " + user.lastName);
            ageLabel.setText("Ålder: " + user.age);

            try {
                Image image = new Image(
                        getClass().getResourceAsStream("/com/safefaces/safefaces/images/" + user.imagePath)
                );
                profileImage.setTranslateX(-15);
                profileImage.setImage(image);

                Circle clip = new Circle(60, 60, 60);
                profileImage.setClip(clip);

            } catch (Exception e) {
                System.out.println("Kunde inte ladda profilbild.");
            }

        } else {
            nameLabel.setText("Ingen användare");
            ageLabel.setText("");
        }
    }

    /** Label used for toggling the medication section. */
    @FXML private Label medsLabel;

    /** Container holding medication-related UI content. */
    @FXML private VBox medsBox;

    /**
     * Toggles the visibility of the medication section.
     * Updates both visibility and layout management, and
     * dynamically changes the label text to reflect the state.
     */
    @FXML private void toggleMeds() {
        boolean isVisible = medsBox.isVisible();

        medsBox.setVisible(!isVisible);
        medsBox.setManaged(!isVisible);

        if (!isVisible) {
            medsLabel.setText("Mediciner ▼");
        } else {
            medsLabel.setText("Mediciner ◀");
        }
    }
}