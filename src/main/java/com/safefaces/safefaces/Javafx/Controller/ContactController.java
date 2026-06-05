package com.safefaces.safefaces.Javafx.Controller;
import com.safefaces.safefaces.Core.Model.User;
import com.safefaces.safefaces.Core.Repository.CaregiverPatientRepository;
import com.safefaces.safefaces.Javafx.App.AppState;
import com.safefaces.safefaces.Javafx.App.SessionManager;
import com.safefaces.safefaces.Core.Model.Contact;
import com.safefaces.safefaces.Core.Model.Enums.RoleType;
import com.safefaces.safefaces.Core.Service.ContactService;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
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
import java.util.Locale;
import java.util.Objects;

public class ContactController {

    @FXML private VBox contactListBox;
    @FXML private Label sectionLabel;

    private final ContactService contactService = new ContactService();
    private MediaPlayer currentPlayer;
    private static final double AVATAR_SIZE = 88;
    @FXML
    public void initialize() {
        var user = AppState.getInstance().getCurrentUser();
        if (user != null && user.role == RoleType.CAREGIVER) {
            if (sectionLabel != null) sectionLabel.setText("Dina patienter");
            buildPatientList(user.id);
        } else {
            buildContactList();
        }
    }

    private void buildPatientList(int caregiverId) {
        if (contactListBox == null) return;
        contactListBox.getChildren().clear();

        List<User> patients = new CaregiverPatientRepository().findPatientsByCaregiver(caregiverId);

        if (patients.isEmpty()) {
            Label empty = new Label("Inga patienter tilldelade.");
            empty.setStyle("-fx-font-size:16; -fx-text-fill:#888; -fx-padding:20;");
            contactListBox.getChildren().add(empty);
            return;
        }

        for (User patient : patients) {
            VBox card = buildPatientRow(patient);
            VBox.setMargin(card, new javafx.geometry.Insets(4, 0, 4, 0));
            contactListBox.getChildren().add(card);
        }
    }

    private VBox buildPatientRow(User patient) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-background-radius: 18;"
                + " -fx-padding: 14 14 14 14;"
                + " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 2);");

        HBox row = new HBox(10);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        String imageName = patient.imagePath != null ? patient.imagePath : "emptyavatar.jpg";
        ImageView imageView = buildAvatar(imageName);

        VBox nameBox = new VBox(4);
        HBox.setHgrow(nameBox, Priority.ALWAYS);
        nameBox.setMinWidth(86);
        String fullName = patient.lastName != null
                ? patient.firstName + " " + patient.lastName
                : patient.firstName;
        Label nameLabel = new Label(fullName);
        nameLabel.setStyle("-fx-font-family: 'Helvetica Neue'; -fx-font-size: 20; -fx-font-weight: 600; -fx-letter-spacing: 0.5; -fx-text-fill: #1a3d2e;");
        Label ageLabel = new Label("Ålder: " + patient.age
                + (patient.location != null && !patient.location.isBlank() ? "  •  " + patient.location : ""));
        ageLabel.setStyle("-fx-font-size: 13; -fx-text-fill: #8aab90;");
        nameBox.getChildren().addAll(nameLabel, ageLabel);

        row.getChildren().addAll(imageView, nameBox);
        card.getChildren().add(row);
        return card;
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
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-background-radius: 18;"
                + " -fx-padding: 14 14 14 14;"
                + " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 2);");
        card.setMargin(card, new javafx.geometry.Insets(4, 0, 4, 0));

        HBox row = new HBox(10);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        String imageName = contact.getImagePath() != null ? contact.getImagePath() : "emptyavatar.jpg";
        ImageView imageView = buildAvatar(imageName);

        VBox nameBox = new VBox(4);
        HBox.setHgrow(nameBox, Priority.ALWAYS);
        nameBox.setMinWidth(86);
        Label nameLabel = new Label(contact.getName());
        nameLabel.setStyle("-fx-font-family: 'Helvetica Neue'; -fx-font-size: 20; -fx-font-weight: 600; -fx-letter-spacing: 0.5; -fx-text-fill: #1a3d2e;");
        Label relationLabel = new Label(contact.getRelation());
        relationLabel.setStyle("-fx-font-size: 13; -fx-text-fill: #8aab90;");
        nameBox.getChildren().addAll(nameLabel, relationLabel);

        VBox callBox = new VBox(4);
        callBox.setAlignment(javafx.geometry.Pos.CENTER);
        callBox.setMinWidth(52);
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

        VBox voiceBox = new VBox(4);
        voiceBox.setAlignment(javafx.geometry.Pos.CENTER);
        voiceBox.setMinWidth(64);
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

    private ImageView buildAvatar(String imageName) {
        ImageView imageView = new ImageView(loadContactImage(imageName));
        imageView.setFitWidth(AVATAR_SIZE);
        imageView.setFitHeight(AVATAR_SIZE);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);

        // Crop from the middle so uploaded portraits stay circular instead of oval.
        imageView.setViewport(centerSquareViewport(imageView.getImage()));
        imageView.setClip(new Circle(AVATAR_SIZE / 2, AVATAR_SIZE / 2, AVATAR_SIZE / 2));
        return imageView;
    }

    private Image loadContactImage(String imageName) {
        try {
            return new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/com/safefaces/safefaces/images/" + imageName)));
        } catch (Exception e) {
            return new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/com/safefaces/safefaces/images/emptyavatar.jpg")));
        }
    }

    private Rectangle2D centerSquareViewport(Image image) {
        double size = Math.min(image.getWidth(), image.getHeight());
        double x = (image.getWidth() - size) / 2;
        double y = (image.getHeight() - size) / 2;
        return new Rectangle2D(x, y, size, size);
    }

    @FXML
    private void handleAddContact() {
        System.out.println("Lägg till kontakt – ej implementerat ännu");
    }

    @FXML
    private void openSos() {
        MainController.instance.showSosConfirm();
    }

    @FXML
    private void handleVoiceMessage(Contact contact, Object ignored) {
        SessionManager.beginSession();

        String audioPath = resolveAudioPath(contact);
        var url = getClass().getResource(audioPath);
        if (url == null) {
            System.out.println("Hittar inte ljudfilen: " + audioPath);
            return;
        }
        System.out.println("Spelar upp röstmemo: " + audioPath);

        // Only one memo should be audible at a time.
        stopCurrentPlayer();

        currentPlayer = new MediaPlayer(new Media(url.toExternalForm()));
        currentPlayer.setOnEndOfMedia(this::stopCurrentPlayer);
        currentPlayer.play();

    }

    private String resolveAudioPath(Contact contact) {
        String voicePath = contact.getVoicePath();
        if (voicePath != null && !voicePath.isBlank()) {
            return toAudioResourcePath(voicePath);
        }

        String key = ((contact.getName() == null ? "" : contact.getName()) + " "
                + (contact.getRelation() == null ? "" : contact.getRelation()))
                .toLowerCase(Locale.ROOT);

        if (key.contains("bror") || key.contains("bert")) return toAudioResourcePath("bror.mp3");
        if (key.contains("dotter") || key.contains("hanna")) return toAudioResourcePath("dotter.mp3");
        if (key.contains("syster") || key.contains("lisa")) return toAudioResourcePath("syster.mp3");
        if (key.contains("vårdgivare") || key.contains("vardgivare")
                || key.contains("sjuksköterska") || key.contains("sjukskoterska")
                || key.contains("aisha") || key.contains("maria")) {
            return toAudioResourcePath("nurse.mp3");
        }

        return toAudioResourcePath("Audio1.mp3");
    }

    private String toAudioResourcePath(String fileName) {
        String path = fileName.trim();
        if (path.startsWith("/com/safefaces/safefaces/audio/")) return path;
        if (path.startsWith("audio/")) path = path.substring("audio/".length());
        if (!path.endsWith(".mp3")) path += ".mp3";
        return "/com/safefaces/safefaces/audio/" + path;
    }

    private void stopCurrentPlayer(){
        if(currentPlayer !=null){
            currentPlayer.stop();
            currentPlayer.dispose();
            currentPlayer =null;
        }
    }


}
