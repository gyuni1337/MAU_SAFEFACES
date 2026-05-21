package com.safefaces.safefaces.Javafx.Controller;
import com.safefaces.safefaces.Backend.Model.Contact;
import com.safefaces.safefaces.Backend.Service.ContactService;
import com.safefaces.safefaces.Javafx.App.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;

import java.util.List;
import java.util.Objects;

/**
 * Controller class responsible for managing the contact view.
 * Displays a list of contacts and handles user interactions such as
 * calling a contact or playing a voice message.
 *
 * This class interacts with the ContactService to retrieve data and
 * updates the JavaFX UI dynamically.
 *
 * @author Gyundyuz Sadulov
 * @author Noor Nabi
 * @author Hamdi Ahmed
 * @author Emma Yousif
 */
public class ContactController {

    /** Container for displaying the list of contacts. */
    @FXML private VBox contactListBox;

    /** Service used to fetch contact data. */
    private final ContactService contactService = new ContactService();

    /** Media player used for playing voice messages. */
    private MediaPlayer currentPlayer;

    /**
     * Initializes the controller after the FXML file has been loaded.
     * Builds and displays the contact list.
     */
    @FXML
    public void initialize() {
        buildContactList();
    }

    /**
     * Builds the contact list UI by fetching contacts
     * and creating UI elements for each contact.
     */
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
            contactListBox.getChildren().add(buildRow(contact));
        }
    }

    /**
     * Creates a UI row for a specific contact.
     *
     * @param contact the contact to display
     * @return an {@link HBox} containing UI components for the contact
     */
    private HBox buildRow(Contact contact) {
        HBox row = new HBox(16);
        row.setStyle("-fx-background-color:white; -fx-background-radius:16; -fx-padding:14;");
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(56);
        imageView.setFitHeight(56);
        imageView.setPreserveRatio(true);

        Circle clip = new Circle(28, 28, 28);
        imageView.setClip(clip);

        String imageName = contact.getImagePath() != null
                ? contact.getImagePath() : "emptyavatar.jpg";

        try {
            Image img = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(
                            "/com/safefaces/safefaces/images/" + imageName)));
            imageView.setImage(img);
        } catch (Exception e) {
            try {
                Image fallback = new Image(Objects.requireNonNull(
                        getClass().getResourceAsStream(
                                "/com/safefaces/safefaces/images/emptyavatar.jpg")));
                imageView.setImage(fallback);
            } catch (Exception ignored) {}
        }

        VBox nameBox = new VBox(4);
        HBox.setHgrow(nameBox, Priority.ALWAYS);

        Label nameLabel = new Label(contact.getName());
        nameLabel.setStyle("-fx-font-size:18; -fx-font-weight:bold;");
        Label relationLabel = new Label(contact.getRelation());
        relationLabel.setStyle("-fx-font-size:13; -fx-text-fill:#888;");
        nameBox.getChildren().addAll(nameLabel, relationLabel);

        Region spacer = new Region();

        Button callBtn = new Button("📞");
        callBtn.setStyle("-fx-font-size:20; -fx-background-color:#e8f5e9; " +
                "-fx-background-radius:50; -fx-min-width:48; -fx-min-height:48;");
        callBtn.setUserData(contact.getName());
        callBtn.setOnAction(this::handleCall);

        Button voiceBtn = new Button("▶");
        voiceBtn.setStyle("-fx-font-size:16; -fx-background-color:#3a3a3a; " +
                "-fx-text-fill:white; -fx-background-radius:50; " +
                "-fx-min-width:48; -fx-min-height:48;");
        voiceBtn.setUserData(contact.getName());
        voiceBtn.setOnAction(e->handleVoiceMessage(contact,voiceBtn));

        row.getChildren().addAll(imageView, nameBox, spacer, callBtn, voiceBtn);
        return row;
    }

    /**
     * Handles the call action when a user presses the call button.
     *
     * @param event the action event triggered by the button
     */
    @FXML
    private void handleCall(javafx.event.ActionEvent event) {
        SessionManager.beginSession();
        Button btn = (Button) event.getSource();
        String contactName = (String) btn.getUserData();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Calling...");
        alert.setHeaderText("Ringing " + contactName);
        alert.setContentText("In call with " + contactName
                + ".\nPress ok to end call.");
        alert.showAndWait();
    }

    /**
     * Handles playback of a contact's voice message.
     *
     * @param contact the contact whose voice message should be played
     * @param btn the button that triggered the action
     */
    @FXML
    private void handleVoiceMessage(Contact contact,Button btn) {
        SessionManager.beginSession();

       String audioPath ="/com/safefaces/safefaces/audio/Audio1.mp3";
        var url =getClass().getResource(audioPath);
        if(url == null){
            System.out.println("Hittar inte ljudfilen: " +audioPath);
            return;
        }

        stopCurrentPlayer();

        currentPlayer = new MediaPlayer(new Media(url.toExternalForm()));

        currentPlayer.play();

        System.out.println("Hittar filen spelar upp ");
    }

    /**
     * Stops and disposes the current media player if active.
     */
    private void stopCurrentPlayer(){
        if(currentPlayer !=null){
            currentPlayer.stop();
            currentPlayer.dispose();
            currentPlayer =null;
        }
    }
}