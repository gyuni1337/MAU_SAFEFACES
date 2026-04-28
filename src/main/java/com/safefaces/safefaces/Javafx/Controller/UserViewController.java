package com.safefaces.safefaces.Javafx.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.safefaces.safefaces.Javafx.Model.User;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.util.Objects;

public class UserViewController {
    @FXML private Label profileNameLabel;
    @FXML private ImageView profileImage;
    @FXML private VBox contactListBox;
    private User activeUser;

//    public void setUser(User user) {
//        this.activeUser = user;
//        updateProfile();
//        loadExampleContacts();
//    }

//    private void updateProfile() {
//        makeImageRound(profileImage);
//        profileNameLabel.setText("Hello, " + activeUser.getName() + "!");
//
//        try {
//            Image image = new Image(
//                    Objects.requireNonNull(getClass().getResourceAsStream("/com/safefaces/safefaces/images/" + activeUser.getImagePath())));
//            profileImage.setImage(image);
//        } catch (Exception e) {
//            System.out.println("Could not load profile image");
//        }
//    }
//
//    private void makeImageRound(ImageView imageView) {
//        double radius = Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2;
//        Circle clip = new Circle(radius, radius, radius);
//        imageView.setClip(clip);
//    }


    /* Skapade metoden bara som exempel för demon */
    private void loadExampleContacts() {

        String[][] demo = {
                {"Anna Anderson", "emptyavatar.jpg"},
                {"John Berger", "emptyavatar.jpg"},
                {"Leonore Smith", "emptyavatar.jpg"},
                {"Marie Schwartz", "emptyavatar.jpg"},
                {"Peter Parker", "emptyavatar.jpg"},
                {"Joanne Jo", "emptyavatar.jpg"},
                {"Oscar Nilsen", "emptyavatar.jpg"},
                {"Carl Carlson", "emptyavatar.jpg"},
                {"Dani Do", "emptyavatar.jpg"},
                {"Louis Lane", "emptyavatar.jpg"}
        };

        for (String[] c : demo) {
            HBox row = new HBox(15);
            row.setStyle("-fx-padding: 10; -fx-alignment: center-left;");

            ImageView imageView = new ImageView();
            imageView.setFitWidth(60);
            imageView.setFitHeight(60);
            imageView.setPreserveRatio(true);

            try {
                imageView.setImage(new Image(
                        Objects.requireNonNull(getClass().getResourceAsStream("/com/safefaces/safefaces/images/" + c[1]))));
            } catch (Exception ignored) {}

            Label label = new Label(c[0]);
            label.setStyle("-fx-font-size: 20;");

            row.getChildren().addAll(imageView, label);
            contactListBox.getChildren().add(row);
        }
    }
     @FXML
    private void openPhoneBook(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/safefaces/safefaces/mainView.fxml"));
            Parent root = loader.load();

            Stage stage =(Stage) profileNameLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
     }

}
