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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.stage.Stage;
import javafx.scene.control.Button;

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

    /**
     * This method  is automatically called by JavaFX
     * after the FXML file has been loaded.
     * It initializes the contact view and loads all contacts for
     * currently logged-in user
     * @author Noor Nabi
     * @author Hamdi Ahmed
     */
    @FXML

    public void initialize() {
        int userId = AppState.getInstance().getCurrentUser().getId();
        buildContactList();
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
            contactListBox.getChildren().add(buildRow(contact));
        }
    }

    /**
     * This method creates a visual row representing a contract.
     * The row contains the contact image, name, relationship, call button and voice memo button.
     * @param contact the contact to display
     * @return an HBox containing the contact information
     * @author Noor Nabi
     * @author Hamdi Ahmed
     */

    private HBox buildRow(Contact contact) {
        HBox row = new HBox(16);
        row.setStyle("-fx-background-color:white; -fx-background-radius:16; -fx-padding:14;");
        row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        imageView.setPreserveRatio(false);

        Circle clip = new Circle(40, 40, 40);
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
        nameBox.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(contact.getName());
        nameLabel.setStyle("-fx-font-size:18; -fx-font-weight:bold; -fx-text-fill:#000000;");        Label relationLabel = new Label(contact.getRelation());
        relationLabel.setStyle("-fx-font-size:13; -fx-text-fill:#888;");
        nameBox.getChildren().addAll(nameLabel, relationLabel);

        VBox callBox = new VBox(5);
        callBox.setAlignment(Pos.CENTER);


        if(!callHeaderAdded){
            Label callHeader = new Label("Ring");
            callHeader.setStyle("-fx-font-size: 9; -fx-text-fill: #888;-fx-font-weight:bold;-fx-text-fill:black;");
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
            voiceHeader.setStyle("-fx-font-size: 9; -fx-text-fill: #888;-fx-font-weight:bold;-fx-text-fill: black;");
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
        row.getChildren().addAll(imageView, nameBox, callBox, voiceBox);
        return row;
    }

    /**
     * Simulates a phone call to a selected contact.
     * This method displays a dialog indicating that the selected contact is being called.
     * @param event the button click event
     * @uthor Noor Nabi
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

            stage.setScene(new Scene(root,400,700));
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Opens the reminders page
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void openReminders() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/components/Reminders.fxml")
            );
            Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) contactListBox.getScene().getWindow();

            stage.setScene(new Scene(root, 400, 700));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Refreshes and displays the contact list
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void openContacts() {
        buildContactList();
    }

    /**
     * Displays a dialog simulating an ongoing emergency call to 112
     *
     * @author Shaima Almoayed
     */
   @FXML
    private void showCalling112(){
        Alert calling=new Alert(Alert.AlertType.INFORMATION);
        calling.setTitle("Samtal");
        calling.setHeaderText("Ringer 112...");
        calling.setContentText("Samtal pågår");

        ButtonType endCall=new ButtonType("Anvslut samtal", ButtonBar.ButtonData.CANCEL_CLOSE);
        calling.getButtonTypes().setAll(endCall);
        calling.showAndWait();
    }

    /**
     * Opens the SOS confirmation screen before placing an emergency call
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void openSosConfirm(){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/SosConfirmView.fxml")
            );
            Parent root = loader.load();
            Stage stage=(Stage) contactListBox.getScene().getWindow();

            stage.setScene(new Scene(root, 400, 700));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}