package com.safefaces.safefaces.Javafx.Controller;

import com.safefaces.safefaces.Javafx.Model.Contact;
import com.safefaces.safefaces.Javafx.Service.ContactService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.management.relation.Role;

public class MainController {

    @FXML
    private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private Label outputLabel;
    @FXML private ListView<String> contactListView;


    private ContactService contactService = new ContactService();

    @FXML
    public void handleContacts(){
        contactService.addContact(new Contact("Lisa", "0701", null, null));
        contactService.addContact(new Contact("Aisha","0702", null, null));
        contactService.addContact(new Contact("John","0703", null, null));
        contactService.addContact(new Contact("Bart","0704", null, null));

        handleContacts();
    }

    @FXML
    private void handleAddContact() {

        String name = nameField.getText();
        String phone = phoneField.getText();

        if (name.isEmpty() || phone.isEmpty()) {
            outputLabel.setText("Fill in name and phone number please");

            return;
        }

        Contact newContact = new Contact(name, phone, null, null);

        contactService.addContact(newContact);

        outputLabel.setText(name + " has been registered");

        nameField.clear();
        phoneField.clear();

        handleContacts();
    }

//    @FXML
//    protected void handleContacts() {
//
//        ObservableList<String> items = FXCollections.observableArrayList();
//
//        for (Contact contact : contactService.getAllContacts().values()) {
//
//            String displayText =
//                    contact.getName() + ": " +
//                            contact.getPhoneNumber();
//
//            items.add(displayText);
//        }
//
//        contactListView.setItems(items);
//        outputLabel.setText("Contacts listed");
//    }

    @FXML
    private void openUserView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/safefaces/safefaces/ContactView.fxml"));
            Parent root = loader.load();

            UserViewController controller = loader.getController();

            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

