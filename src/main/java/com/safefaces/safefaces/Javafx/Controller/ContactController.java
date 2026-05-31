package com.safefaces.safefaces.Javafx.Controller;
import com.safefaces.safefaces.Javafx.App.AppState;
import com.safefaces.safefaces.Javafx.App.SessionManager;
import com.safefaces.safefaces.Core.Model.Contact;
import com.safefaces.safefaces.Core.Service.ContactService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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

public class ContactController {

    @FXML private VBox contactListBox;
    private boolean voiceHeaderAdded = false;
    private boolean callHeaderAdded = false;

    private final ContactService contactService = new ContactService();
    private MediaPlayer currentPlayer;
    @FXML

    public void initialize() {
        int userId = AppState.getInstance().getCurrentUser().getId();
        buildContactList();
    }

    private void buildContactList() {
        if (contactListBox == null)
            return;

        contactListBox.getChildren().clear();
        voiceHeaderAdded = false;
        callHeaderAdded = false;


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
        VBox callBox = new VBox(4);
        callBox.setAlignment(Pos.CENTER);


        if(!callHeaderAdded){
            Label callHeader = new Label("Ring");
            callHeader.setStyle("-fx-font-size: 11; -fx-text-fill: #888;-fx-font-weight:bold;");
            callBox.getChildren().add(callHeader);
            callHeaderAdded = true;
        }

        Button callBtn = new Button("📞");
        callBtn.setStyle("-fx-font-size:20; -fx-background-color:#e8f5e9; " +
                "-fx-background-radius:50; -fx-min-width:48; -fx-min-height:48;");
        callBtn.setUserData(contact.getName());
        callBtn.setOnAction(this::handleCall);
        callBox.getChildren().add(callBtn);

        VBox voiceBox = new VBox(4);
        voiceBox.setAlignment(Pos.CENTER);

        if(!voiceHeaderAdded){
            Label voiceHeader = new Label("Röstmemo");
            voiceHeader.setStyle("-fx-font-size: 11; -fx-text-fill: #888;-fx-font-weight:bold;");
            voiceBox.getChildren().add(voiceHeader);
            voiceHeaderAdded = true;
        }

        Button voiceBtn = new Button("\uD83C\uDF99\uFE0F");
        voiceBtn.setStyle("-fx-font-size:16; -fx-background-color:#e8f5e9; " +
                "-fx-text-fill:white; -fx-background-radius:50; " +
                "-fx-min-width:48; -fx-min-height:48;");
        voiceBtn.setUserData(contact.getName());
        voiceBtn.setOnAction(e->handleVoiceMessage(contact,voiceBtn));

        voiceBox.getChildren().add(voiceBtn);
        row.getChildren().addAll(imageView, nameBox, spacer, callBox, voiceBox);
        return row;
    }

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

    @FXML
    private void handleVoiceMessage(Contact contact,Button btn) {
        SessionManager.beginSession();

        String filePath = contact.getVoicePath();
        if(filePath == null || filePath.isBlank()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Inget röstmemo");
            alert.showAndWait();
            return;
        }

       String audioPath ="/com/safefaces/safefaces/audio/" + filePath;
        var url =getClass().getResource(audioPath);
        if(url == null){
            System.out.println("Hittar inte ljudfilen: " +audioPath);
            return;
        }
        System.out.println("Hittar filen spelar upp ");
        stopCurrentPlayer();

       stopCurrentPlayer();
       currentPlayer = new MediaPlayer((new Media(url.toExternalForm())));
       currentPlayer.play();

        

    }
    private void stopCurrentPlayer(){
        if(currentPlayer !=null){
            currentPlayer.stop();
            currentPlayer.dispose();
            currentPlayer =null;
        }
    }


}