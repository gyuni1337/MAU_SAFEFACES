package com.safefaces.safefaces.Backend.Repository;

import com.safefaces.safefaces.Backend.DatabaseConnection;
import com.safefaces.safefaces.Backend.Model.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for CRUD operations on contacts.
 *
 * @author Noor Nabi
 * @author Gyundyuz Sadulov
 * @author Emma Yousif
 */
public class ContactRepository {

    /**
     * Retrieves all contacts for a given user, ordered by name.
     *
     * @param userId the ID of the user whose contacts to fetch
     * @return list of contacts, empty if none found or DB unavailable
     */
    public List<Contact> findByUserId(int userId) {
        List<Contact> result = new ArrayList<>();
        String sql = "SELECT name, relation, phone_number, image_path, voice_note_path " +
                "FROM contacts WHERE user_id = ? ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(new Contact(
                            rs.getString("name"),
                            rs.getString("phone_number"),
                            rs.getString("relation"),
                            rs.getString("image_path"),
                            rs.getString("voice_note_path")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("ContactRepository.findByUserId failed: " + e.getMessage());
        }
        return result;
    }

    /**
     * Inserts a new contact for the given user.
     *
     * @param userId  the ID of the user who owns the contact
     * @param contact the contact to insert
     */
    public void save(int userId, Contact contact) {
        String sql = "INSERT INTO contacts (user_id, name, relation, phone_number, image_path) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, contact.getName());
            stmt.setString(3, contact.getRelation() != null ? contact.getRelation() : "Okänd");
            stmt.setString(4, contact.getPhoneNumber());
            stmt.setString(5, contact.getImagePath() != null ? contact.getImagePath() : "emptyavatar.jpg");
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ContactRepository.save failed: " + e.getMessage());
        }
    }

    /**
     * Deletes a contact by name for the given user.
     *
     * @param userId the ID of the user who owns the contact
     * @param name   the name of the contact to delete
     */
    public void deleteByName(int userId, String name) {
        String sql = "DELETE FROM contacts WHERE name = ? AND user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ContactRepository.deleteByName failed: " + e.getMessage());
        }
    }
}
