package com.safefaces.safefaces.Core.Service;

import com.safefaces.safefaces.Core.Model.Contact;
import com.safefaces.safefaces.Core.Repository.ContactRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Service class for contact management.
 * Delegates persistence to {@link ContactRepository} and provides
 * a demo-data fallback when the database is unavailable.
 *
 * @author Noor Nabi
 * @author Gyundyuz Sadulov
 * @author Emma Yousif
 */
public class ContactService {

    private final ContactRepository repo = new ContactRepository();

    /** Hardcoded user ID — replace with session-aware userId when multi-user support is added. */
    private static final int USER_ID = 1;

    /** Local fallback storage used when the DB is unreachable. */
    private final HashMap<String, Contact> localContacts = new HashMap<>();

    /**
     * Retrieves the contact list from the database.
     * Falls back to demo contacts if the repository returns nothing (e.g. DB down).
     *
     * @return list of contacts
     */
    public List<Contact> getContactList() {
        List<Contact> result = repo.findByUserId(USER_ID);
        if (result.isEmpty()) {
            System.out.println("DB unavailable or empty — showing demo contacts.");
            return getDemoContacts();
        }
        return result;
    }

    /**
     * Adds a contact to the database, or to local storage if the DB is unreachable.
     *
     * @param contact the contact to add
     */
    public void addContact(Contact contact) {
        try {
            repo.save(USER_ID, contact);
        } catch (Exception e) {
            System.out.println("DB not available, saved locally: " + e.getMessage());
            localContacts.put(contact.getName(), contact);
        }
    }

    /**
     * Returns all contacts as a name-keyed map.
     *
     * @return map of contact name → contact
     */
    public HashMap<String, Contact> getAllContacts() {
        HashMap<String, Contact> map = new HashMap<>();
        for (Contact c : getContactList()) map.put(c.getName(), c);
        return map;
    }

    /**
     * Removes a contact by name from the database, or from local storage if the DB is unreachable.
     *
     * @param name the name of the contact to remove
     */
    public void removeContact(String name) {
        try {
            repo.deleteByName(USER_ID, name);
        } catch (Exception e) {
            localContacts.remove(name);
        }
    }

    private List<Contact> getDemoContacts() {
        List<Contact> demo = new ArrayList<>();
        demo.add(new Contact("Aisha",  "070-111 22 33", "Vårdgivare",    "aisha.jpg",  "nurse.mp3"));
        demo.add(new Contact("Lisa",   "070-222 33 44", "Syster",        "lisa.jpg",   "syster.mp3"));
        demo.add(new Contact("Hanna",  "070-333 44 55", "Dotter",        "hanna.jpg",  "dotter.mp3"));
        demo.add(new Contact("Maria",  "070-444 55 66", "Sjuksköterska", "maria.jpg",  "nurse.mp3"));
        demo.add(new Contact("Bert",   "070-555 66 77", "Bror",          "bert.jpg",   "bror.mp3"));
        return demo;
    }
}
