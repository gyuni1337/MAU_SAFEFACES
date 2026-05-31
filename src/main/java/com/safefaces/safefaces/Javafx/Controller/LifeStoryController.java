package com.safefaces.safefaces.Javafx.Controller;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;

/**
 * Controller for the life story view.
 * Displays a slideshow of the user's life story images
 * and handles navigation back to the profile view.
 *
 * @author Shaima Almoayed
 */

public class LifeStoryController {

    @FXML
    private ImageView imageView;
    private int imageNumber=1;

    /**
     * Initialize the life store view and starts
     * the automatic image slideshow
     *
     * @author Shaima Almoayed
     */
    @FXML
    public void initialize(){
        imageView.setImage(new Image(getClass().getResourceAsStream("/com/safefaces/safefaces/images/story1.jpg")));

        Timeline timeline=new Timeline(new KeyFrame(Duration.seconds(3),
                event -> changeImage()
        ));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    @FXML
    private Button backButton;

    /**
     * Returns to the profile view
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void goBack(){
        try{
            FXMLLoader loader=new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/ProfileView.fxml")
            );
            Parent root=loader.load();
            Stage stage=(Stage)backButton.getScene().getWindow();
            stage.setScene(new Scene(root,400,700));
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Changes to the next image in the slideshow
     *
     * @author Shaima Almoayed
     */
    private void changeImage(){
        imageNumber++;

        if(imageNumber>5){
            imageNumber=1;
        }
        imageView.setImage(new Image(getClass().getResourceAsStream("/com/safefaces/safefaces/images/story" + imageNumber + ".jpg")));
    }
}
