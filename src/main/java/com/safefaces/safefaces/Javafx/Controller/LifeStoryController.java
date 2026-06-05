package com.safefaces.safefaces.Javafx.Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class LifeStoryController {

    @FXML private ImageView storyImage;
    @FXML private Label slideCounter;

    private final List<Image> storyImages = new ArrayList<>();
    private Timeline slideshow;
    private int currentIndex;

    @FXML
    public void initialize() {
        loadStoryImages();
        showCurrentImage();

        slideshow = new Timeline(new KeyFrame(Duration.seconds(2), e -> showNextImage()));
        slideshow.setCycleCount(Timeline.INDEFINITE);
        slideshow.play();
    }

    @FXML
    private void goBack() {
        stopSlideshow();
        MainController.instance.showJournal();
    }

    @FXML
    private void readLifeStory() {
        System.out.println("Läser upp livsberättelse");
    }

    private void loadStoryImages() {
        for (int i = 1; i <= 5; i++) {
            var stream = getClass().getResourceAsStream("/com/safefaces/safefaces/images/story" + i + ".jpg");
            if (stream != null) {
                storyImages.add(new Image(stream));
            }
        }
    }

    private void showCurrentImage() {
        if (storyImages.isEmpty()) {
            if (slideCounter != null) slideCounter.setText("Inga bilder hittades.");
            return;
        }

        storyImage.setImage(storyImages.get(currentIndex));
        if (slideCounter != null) {
            slideCounter.setText((currentIndex + 1) + " / " + storyImages.size());
        }
    }

    private void showNextImage() {
        if (storyImages.isEmpty()) return;
        currentIndex = (currentIndex + 1) % storyImages.size();
        showCurrentImage();
    }

    private void stopSlideshow() {
        if (slideshow != null) {
            slideshow.stop();
        }
    }
}
