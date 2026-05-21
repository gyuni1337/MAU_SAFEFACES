package com.safefaces.safefaces.Core.Repository;

import com.safefaces.safefaces.Core.DatabaseConnection;
import com.safefaces.safefaces.Core.Model.Enums.RoleType;
import com.safefaces.safefaces.Core.Model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing caregiver-patient assignments.
 *
 * @author Gyundyuz Sadulov
 */
public class CaregiverPatientRepository {

    /**
     * Returns all patients assigned to a given caregiver.
     *
     * @param caregiverId the caregiver's user ID
     * @return list of patient users
     */
    public List<User> findPatientsByCaregiver(int caregiverId) {
        List<User> patients = new ArrayList<>();
        String sql = """
                SELECT u.id, u.first_name, u.last_name, u.age, u.image_path,
                       u.pin_hash, u.role, u.location
                FROM safefaces.users u
                JOIN safefaces.caregiver_patients cp ON u.id = cp.patient_id
                WHERE cp.caregiver_id = ?
                ORDER BY u.first_name
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, caregiverId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    User u = new User();
                    u.id        = rs.getInt("id");
                    u.firstName = rs.getString("first_name");
                    u.lastName  = rs.getString("last_name");
                    u.age       = rs.getInt("age");
                    u.imagePath = rs.getString("image_path");
                    u.pinHash   = rs.getString("pin_hash");
                    u.role      = RoleType.valueOf(rs.getString("role").toUpperCase());
                    u.location  = rs.getString("location");
                    patients.add(u);
                }
            }
        } catch (SQLException e) {
            System.out.println("CaregiverPatientRepository error: " + e.getMessage());
        }
        return patients;
    }
}
