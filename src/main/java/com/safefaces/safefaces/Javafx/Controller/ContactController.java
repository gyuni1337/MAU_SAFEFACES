package com.safefaces.safefaces.Javafx.Controller;
import com.safefaces.safefaces.Javafx.App.AppState;
import com.safefaces.safefaces.Javafx.App.SessionManager;
import com.safefaces.safefaces.Core.Model.Contact;
import com.safefaces.safefaces.Core.Model.Enums.RoleType;
import com.safefaces.safefaces.Core.Service.ContactService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;

import java.util.List;
import java.util.Objects;

public class ContactController {

    @FXML private VBox contactListBox;
    @FXML private Label sectionLabel;

    private final ContactService contactService = new ContactService();
    private MediaPlayer currentPlayer;
    @FXML
    public void initialize() {
        var user = AppState.getInstance().getCurrentUser();
        if (user != null && user.role == RoleType.CAREGIVER && sectionLabel != null) {
            sectionLabel.setText("Dina patienter");
        }
        buildContactList();
    }

    private void buildContactList() {
        if (contactListBox == null)
            return;

        contactListBox.getChildren().clear();

        List<Contact> contacts = contactService.getContactList();

        if (contacts.isEmpty()) {
            Label empty = new Label("No contacts registered.");
            empty.setStyle("-fx-font-size:16; -fx-text-fill:#888; -fx-padding:20;");
            contactListBox.getChildren().add(empty);
            return;
        }

        for (Contact contact : contacts) {
            VBox card = buildRow(contact);
            VBox.setMargin(card, new javafx.geometry.Insets(4, 0, 4, 0));
            contactListBox.getChildren().add(card);
        }
    }

    private VBox buildRow(Contact contact) {
        // Outer card
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-background-radius: 18;"
                + " -fx-padding: 14 16 14 16;"
                + " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 2);");
        card.setMargin(card, new javafx.geometry.Insets(4, 0, 4, 0));

        HBox row = new HBox(14);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // Profile photo
        ImageView imageView = new ImageView();
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        imageView.setPreserveRatio(true);

        Circle clip = new Circle(40, 40, 40);
        imageView.setClip(clip);

        String imageName = contact.getImagePath() != null ? contact.getImagePath() : "emptyavatar.jpg";
        try {
            Image img = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/com/safefaces/safefaces/images/" + imageName)));
            imageView.setImage(img);
        } catch (Exception e) {
            try {
                Image fallback = new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream("/com/safefaces/safefaces/images/emptyavatar.jpg")));
                imageView.setImage(fallback);
            } catch (Exception ignored) {}
        }

        // Name + relation
        VBox nameBox = new VBox(4);
        HBox.setHgrow(nameBox, Priority.ALWAYS);
        Label nameLabel = new Label(contact.getName());
        nameLabel.setStyle("-fx-font-family: 'Helvetica Neue'; -fx-font-size: 22; -fx-font-weight: 600; -fx-letter-spacing: 0.5; -fx-text-fill: #1a3d2e;");
        Label relationLabel = new Label(contact.getRelation());
        relationLabel.setStyle("-fx-font-size: 13; -fx-text-fill: #8aab90;");
        nameBox.getChildren().addAll(nameLabel, relationLabel);

        // Call button (light green circle)
        VBox callBox = new VBox(4);
        callBox.setAlignment(javafx.geometry.Pos.CENTER);
        Label callLabel = new Label("Ring");
        callLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #8aab90;");
        VBox callCircle = new VBox();
        callCircle.setAlignment(javafx.geometry.Pos.CENTER);
        callCircle.setStyle("-fx-background-color: #e8f5e9; -fx-background-radius: 50;"
                + " -fx-min-width: 52; -fx-min-height: 52;"
                + " -fx-pref-width: 52; -fx-pref-height: 52;"
                + " -fx-cursor: hand;");
        org.kordamp.ikonli.javafx.FontIcon callIcon = new org.kordamp.ikonli.javafx.FontIcon("fas-phone");
        callIcon.setIconSize(22);
        callIcon.setIconColor(javafx.scene.paint.Color.web("#1a6b3d"));
        callCircle.getChildren().add(callIcon);
        callCircle.setOnMouseClicked(e -> {
            SessionManager.beginSession();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ringer...");
            alert.setHeaderText("Ringer " + contact.getName());
            alert.setContentText("I samtal med " + contact.getName() + ".\nTryck OK för att avsluta.");
            alert.showAndWait();
        });
        callBox.getChildren().addAll(callLabel, callCircle);

        // Voice memo button (light green circle)
        VBox voiceBox = new VBox(4);
        voiceBox.setAlignment(javafx.geometry.Pos.CENTER);
        Label voiceLabel = new Label("Röstmemo");
        voiceLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #8aab90;");
        VBox voiceCircle = new VBox();
        voiceCircle.setAlignment(javafx.geometry.Pos.CENTER);
        voiceCircle.setStyle("-fx-background-color: #e8f5e9; -fx-background-radius: 50;"
                + " -fx-min-width: 52; -fx-min-height: 52;"
                + " -fx-pref-width: 52; -fx-pref-height: 52;"
                + " -fx-cursor: hand;");
        org.kordamp.ikonli.javafx.FontIcon voiceIcon = new org.kordamp.ikonli.javafx.FontIcon("fas-microphone");
        voiceIcon.setIconSize(22);
        voiceIcon.setIconColor(javafx.scene.paint.Color.web("#1a6b3d"));
        voiceCircle.getChildren().add(voiceIcon);
        voiceCircle.setOnMouseClicked(e -> handleVoiceMessage(contact, null));
        voiceBox.getChildren().addAll(voiceLabel, voiceCircle);

        row.getChildren().addAll(imageView, nameBox, callBox, voiceBox);
        card.getChildren().add(row);
        return card;
    }

    @FXML
    private void handleAddContact() {
        System.out.println("Lägg till kontakt – ej implementerat ännu");
    }

    @FXML
    private void handleVoiceMessage(Contact contact, Object ignored) {
        SessionManager.beginSession();

       String audioPath ="/com/safefaces/safefaces/audio/Audio1.mp3";
        var url =getClass().getResource(audioPath);
        if(url == null){
            System.out.println("Hittar inte ljudfilden: " +audioPath);
            return;
        }
        System.out.println("Hittar filen spelar upp ");
        stopCurrentPlayer();


        new MediaPlayer((new Media(url.toExternalForm()))).play();
        

    }
    private void stopCurrentPlayer(){
        if(currentPlayer !=null){
            currentPlayer.stop();
            currentPlayer.dispose();
            currentPlayer =null;
        }
    }


}