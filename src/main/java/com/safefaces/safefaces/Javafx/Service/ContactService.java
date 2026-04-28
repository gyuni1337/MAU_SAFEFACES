package com.safefaces.safefaces.Javafx.Service;
import com.safefaces.safefaces.Javafx.Model.Contact;

import com.safefaces.safefaces.Javafx.Model.Contact;

import java.util.HashMap;

//logik, validering,
public class ContactService {

    private HashMap<String, Contact> contacts = new HashMap<>();

    public void addContact(Contact contact) {
        contacts.put(contact.getName(), contact);
    }

    public void removeContact(String name) {
        contacts.remove(name);
    }

    public HashMap<String, Contact> getAllContacts() {
        return contacts;
    }
}
