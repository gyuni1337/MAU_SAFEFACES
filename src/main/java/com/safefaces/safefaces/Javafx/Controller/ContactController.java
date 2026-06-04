package com.safefaces.safefaces.Javafx.Controller;
import com.safefaces.safefaces.Core.Model.User;
import com.safefaces.safefaces.Core.Repository.CaregiverPatientRepository;
import com.safefaces.safefaces.Javafx.App.AppState;
import com.safefaces.safefaces.Javafx.App.SessionManager;
import com.safefaces.safefaces.Core.Model.Contact;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.stage.Stage;
import java.util.List;
import java.util.Objects;

/**
 * This controller is responsible for displaying and managing
 * the users contacts.
 * Creating and displaying the contact list,
 * simulated phone calls, voice memo playback,
 * navigation between application views and SOS-related actions.
 *
 * This controller retrieves contact information through the
 * ContactService class and dynamically creates the user interface elements shown in the contact list.
 * @author Hamdi Ahmed
 * @author  Noor Nabi
 * @author Shaima ALmoayed
 */



public class ContactController {

    @FXML private VBox contactListBox;
    @FXML private Label sectionLabel;
    @FXML private Button sosButton;
    private boolean voiceHeaderAdded;
    private boolean callHeaderAdded;


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

    /**
     * This method Builds and populates the contact list view.
     * It clears any existing contact entries and creates a new row for each contact retrieved
     * from the ContactService.
     * If no contact are available, a message is shown.
     * @author Noor Nabi
     * @author Hamdi Ahmed
     */

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
            VBox card = buildRow(contact);
            VBox.setMargin(card, new javafx.geometry.Insets(4, 0, 4, 0));
            contactListBox.getChildren().add(card);
        }
    }

    private VBox buildRow(Contact contact) {
        // Outer card
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-background-radius: 18;"
                + " -fx-padding: 14 14 14 14;"
                + " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 2);");
        card.setMargin(card, new javafx.geometry.Insets(4, 0, 4, 0));

        HBox row = new HBox(10);
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        // Profile photo
        String imageName = contact.getImagePath() != null ? contact.getImagePath() : "emptyavatar.jpg";
        ImageView imageView = buildAvatar(imageName);

        // Name + relation
        VBox nameBox = new VBox(4);
        HBox.setHgrow(nameBox, Priority.ALWAYS);
        nameBox.setMinWidth(86);
        Label nameLabel = new Label(contact.getName());
        nameLabel.setStyle("-fx-font-family: 'Helvetica Neue'; -fx-font-size: 20; -fx-font-weight: 600; -fx-letter-spacing: 0.5; -fx-text-fill: #1a3d2e;");
        Label relationLabel = new Label(contact.getRelation());
        relationLabel.setStyle("-fx-font-size: 13; -fx-text-fill: #8aab90;");
        nameBox.getChildren().addAll(nameLabel, relationLabel);

        // Call button (light green circle)
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

        // Voice memo button (light green circle)
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

    /**
     * Simulates a phone call to a selected contact.
     * This method displays a dialog indicating that the selected contact is being called.
     * @param event the button click event
     * @uthor Noor Nabi
     */

    @FXML
    private void handleAddContact() {
        System.out.println("Lägg till kontakt – ej implementerat ännu");
    }

    /**
     * Plays the selected contact's voice memo.
     * Stops any currently playing audio before
     * starting playback of the new recording.
     *
     *If no voice memo exists, an information dialog is displayed
     * @param contact the contact whose voice memo should be played.
     * @param btn the button that triggered the action
     * @author Hamdi Ahmed
     */

    @FXML
    private void handleVoiceMessage(Contact contact, Object ignored) {
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

    /**
     * Stops and disposes the currently active media player.
     * Used to ensure that only one voice memo is playing at the time.
     * @author Hamdi Ahmed
     */
    private void stopCurrentPlayer(){
        if(currentPlayer !=null){
            currentPlayer.stop();
            currentPlayer.dispose();
            currentPlayer =null;
        }
    }

    /**
     * Opens the profile page
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void openProfile(){
        try{
            FXMLLoader loader=new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/ProfileView.fxml")
            );
            Parent root=loader.load();
            javafx.stage.Stage stage=(javafx.stage.Stage) contactListBox.getScene().getWindow();

}
