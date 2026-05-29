package com.safefaces.safefaces.Core.Repository;

import com.safefaces.safefaces.Core.DatabaseConnection;
import com.safefaces.safefaces.Core.Model.Medication;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for reading medication data from the database.
 *
 * @author Gyundyuz Sadulov
 */
public class MedicationRepository {

    /**
     * Returns all active medications for a given user.
     *
     * @param userId the user's ID
     * @return list of active medications, empty if none found
     */
    public List<Medication> findActiveByUserId(int userId) {
        List<Medication> list = new ArrayList<>();
        String sql = """
                SELECT id, user_id, name, dose, time_of_day, is_active
                FROM safefaces.medications
                WHERE user_id = ? AND is_active = TRUE
                ORDER BY time_of_day
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Medication m = new Medication();
                    m.id        = rs.getInt("id");
                    m.userId    = rs.getInt("user_id");
                    m.name      = rs.getString("name");
                    m.dose      = rs.getString("dose");
                    m.timeOfDay = rs.getString("time_of_day");
                    m.isActive  = rs.getBoolean("is_active");
                    list.add(m);
                }
            }
        } catch (SQLException e) {
            System.out.println("MedicationRepository error: " + e.getMessage());
        }
        return list;
    }

    public void save(int userId, Medication med) {
        String sql = """
                INSERT INTO safefaces.medications (user_id, name, dose, time_of_day, is_active)
                VALUES (?, ?, ?, ?, TRUE)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, med.name);
            stmt.setString(3, med.dose);
            stmt.setString(4, med.timeOfDay);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("MedicationRepository.save error: " + e.getMessage());
        }
    }

    public void deactivateById(int id) {
        String sql = "UPDATE safefaces.medications SET is_active = FALSE WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("MedicationRepository.deactivateById error: " + e.getMessage());
        }
    }
}
