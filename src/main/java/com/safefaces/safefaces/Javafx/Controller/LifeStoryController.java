package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Javafx.App.AppState;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class LifeStoryController {

    @FXML private ImageView imageView;
    private int imageNumber = 1;

    @FXML
    public void initialize() {
        imageView.setImage(new Image(getClass().getResourceAsStream("/com/safefaces/safefaces/images/story1.jpg")));

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> changeImage()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    private void goBack() {
        AppState.getInstance().getMainController().loadView("/com/safefaces/safefaces/ProfileView.fxml");
    }

    private void changeImage() {
        imageNumber++;
        if (imageNumber > 5) imageNumber = 1;
        imageView.setImage(new Image(getClass().getResourceAsStream("/com/safefaces/safefaces/images/story" + imageNumber + ".jpg")));
    }
}
