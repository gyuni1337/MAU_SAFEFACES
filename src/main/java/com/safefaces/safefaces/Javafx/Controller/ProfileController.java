package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Core.Model.Medication;
import com.safefaces.safefaces.Core.Model.User;
import com.safefaces.safefaces.Core.Model.Enums.RoleType;
import com.safefaces.safefaces.Core.Repository.CaregiverPatientRepository;
import com.safefaces.safefaces.Core.Repository.MedicationRepository;
import com.safefaces.safefaces.Core.Repository.ReminderRepository;
import com.safefaces.safefaces.Javafx.App.AppState;
import com.safefaces.safefaces.Javafx.App.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controller for the profile/journal view.
 * Displays user info, location, and medications fetched from the database.
 *
 * @author Emma Yousif
 * @author Gyundyuz Sadulov
 * @author Shaima Almoayed
 */
public class ProfileController {

    @FXML private Label nameLabel;
    @FXML private Label ageLabel;
    @FXML private Label locationLabel;
    @FXML private ImageView profileImage;
    @FXML private Label medsLabel;
    @FXML private VBox medsBox;
    @FXML private Button informationButton;
    @FXML private Button peopleButton;
    @FXML private Button oveerviewButton;
    @FXML private Button medicineButton;
    @FXML private Button healthButton;
    @FXML private Button familyButton;

    private final MedicationRepository medicationRepository = new MedicationRepository();
    private final CaregiverPatientRepository caregiverPatientRepo = new CaregiverPatientRepository();

    @FXML
    public void initialize() {
//        User user = AppState.getInstance().getCurrentUser();
//
//        if (user == null) {
//            nameLabel.setText("Ingen användare");
//            ageLabel.setText("");
//            return;
//        }
//
//        String fullName = user.lastName != null
//                ? user.firstName + " " + user.lastName
//                : user.firstName;
//        nameLabel.setText(fullName);
//        ageLabel.setText("Ålder: " + user.age);
//        locationLabel.setText(user.location != null ? "Du är hemma i " + user.location : "");
//
//        try {
//            Image image = new Image(
//                    getClass().getResourceAsStream("/com/safefaces/safefaces/images/" + user.imagePath));
//            profileImage.setTranslateX(-15);
//            profileImage.setImage(image);
//            profileImage.setClip(new Circle(60, 60, 60));
//        } catch (Exception e) {
//            System.out.println("Kunde inte ladda profilbild.");
//        }
//
//        loadMedications(user.id);
        System.out.println("ProfileView laddades");


    }

    private void loadMedications(int userId) {
        List<Medication> meds = medicationRepository.findActiveByUserId(userId);
        medsBox.getChildren().clear();

        if (meds.isEmpty()) {
            medsBox.getChildren().add(styledLabel("Inga aktiva mediciner.", "#888", 15));
            return;
        }

        for (Medication med : meds) {
            String text = med.name + " — " + med.dose + "  (" + med.timeOfDay + ")";
            medsBox.getChildren().add(styledLabel(text, "#333", 16));
        }
    }

    private Label styledLabel(String text, String color, int size) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-size: " + size + "px; -fx-text-fill: " + color + ";");
        lbl.setWrapText(true);
        return lbl;
    }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
    }

    @FXML
    private void toggleMeds() {
        boolean isVisible = medsBox.isVisible();
        medsBox.setVisible(!isVisible);
        medsBox.setManaged(!isVisible);
        medsLabel.setText(isVisible ? "Mediciner ◀" : "Mediciner ▼");
    }

    /**
     * Opens the information view and switches the current scene
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void openInformation(){
        try {
            FXMLLoader loader= new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/InformationView.fxml")

            );
            Parent root=loader.load();
            Stage stage=(Stage) informationButton.getScene().getWindow();
            stage.setScene(new Scene(root,400,700));
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Open the medicines view
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void openMedicines() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/MedicineView.fxml")

            );
            Parent root = loader.load();
            Stage stage = (Stage) informationButton.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 700));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the health view.
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void openHealth(){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/HealthView.fxml")

            );
            Parent root = loader.load();
            Stage stage = (Stage) informationButton.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 700));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Navigates to the home view
     *
     * @author Shaima Almoayed
     */
   @FXML
   private void openOverview(){
        openHomeView();
   }

    /**
     * Open the people view
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void openPeople() {
       openHomeView();
    }

    /**
     * Handles clicks on the profile button
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void openProfile() {
        System.out.println("Profil klickad");
    }

    /**
     * Opens the home page
     *
     * @author Shaima Almoayed
     */
    private void openHomeView(){
        try {
            FXMLLoader loader= new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/HomeView.fxml")

            );
            Parent root=loader.load();
            Stage stage=(Stage) informationButton.getScene().getWindow();
            stage.setScene(new Scene(root,400,700));
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Opens the family view
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void openFamily(){
            try{
                FXMLLoader loader=new FXMLLoader(
                        getClass().getResource("/com/safefaces/safefaces/FamilyView.fxml")
                );
                Parent root=loader.load();
                Stage stage=(Stage)familyButton.getScene().getWindow();

                stage.setScene(new Scene(root,400,700));
                stage.show();

            }catch (Exception e){
                e.printStackTrace();
            }
    }

    /**
     * Opens the life story view
     *
     * @author Shaima Almoayed
     */
    @FXML
    private void openLifeStory(){
        try{
            FXMLLoader loader=new FXMLLoader(
                    getClass().getResource("/com/safefaces/safefaces/LifeStoryView.fxml")
            );
            Parent root=loader.load();
            Stage stage=(Stage)familyButton.getScene().getWindow();

            stage.setScene(new Scene(root,400,700));
            stage.show();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
