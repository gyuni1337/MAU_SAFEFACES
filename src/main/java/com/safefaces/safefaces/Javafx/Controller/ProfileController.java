package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Javafx.App.AppState;
import com.safefaces.safefaces.Backend.Model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class ProfileController {

    @FXML
    private Label nameLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private ImageView profileImage;

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

    @FXML private Label medsLabel;
    @FXML private VBox medsBox;
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