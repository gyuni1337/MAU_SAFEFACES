package com.safefaces.safefaces.Javafx.Service;

import com.safefaces.safefaces.Backend.DatabaseConnection;
import com.safefaces.safefaces.Javafx.Model.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//mostly sql
public class ContactService {

    private final HashMap<String, Contact> localContacts = new HashMap<>();

    //sql grejsi mojsi
    public List<Contact> getContactList() {
        List<Contact> result = new ArrayList<>();
        String sql = "SELECT id, name, relation, phone_number, image_path, voice_note_path " +
                "FROM contacts WHERE user_id = 1 ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name     = rs.getString("name");
                String relation = rs.getString("relation");
                String phone    = rs.getString("phone_number");
                String image    = rs.getString("image_path");

                System.out.println(">>> name=" + name + " | relation=" + relation);

                result.add(new Contact(name, phone, relation, image, null));
            }
            System.out.println("Hämtade " + result.size() + " kontakter från DB.");

        } catch (SQLException e) {
            System.out.println("DB ej tillgänglig, visar demo kontakter: " + e.getMessage());
            result = getDemoContacts();
        }
        return result;
    }

    private List<Contact> getDemoContacts() {
        List<Contact> demo = new ArrayList<>();
        demo.add(new Contact("Aisha",  "070-111 22 33", "Vårdgivare",    "aisha.jpg",  null));
        demo.add(new Contact("Lisa",   "070-222 33 44", "Syster",        "lisa.jpg",   null));
        demo.add(new Contact("Hanna",  "070-333 44 55", "Dotter",        "hanna.jpg",  null));
        demo.add(new Contact("Maria",  "070-444 55 66", "Sjuksköterska", "maria.jpg",  null));
        demo.add(new Contact("Bert",   "070-555 66 77", "Make",          "bert.jpg",   null));
        return demo;
    }

    public void addContact(Contact contact) {
        String sql = "INSERT INTO contacts (user_id, name, relation, phone_number, image_path) " +
                "VALUES (1, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, contact.getName());
            stmt.setString(2, contact.getRelation() != null ? contact.getRelation() : "Okänd");
            stmt.setString(3, contact.getPhoneNumber());
            stmt.setString(4, contact.getImagePath() != null
                    ? contact.getImagePath() : "emptyavatar.jpg");
            stmt.executeUpdate();
            System.out.println("Kontakt saved in db: " + contact.getName());
        } catch (SQLException e) {
            System.out.println("DB not available, saved locally: " + e.getMessage());
            localContacts.put(contact.getName(), contact);
        }
    }

    public HashMap<String, Contact> getAllContacts() {
        HashMap<String, Contact> map = new HashMap<>();
        for (Contact c : getContactList()) map.put(c.getName(), c);
        return map;
    }

    public void removeContact(String name) {
        String sql = "DELETE FROM contacts WHERE name = ? AND user_id = 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            localContacts.remove(name);
        }
    }
}