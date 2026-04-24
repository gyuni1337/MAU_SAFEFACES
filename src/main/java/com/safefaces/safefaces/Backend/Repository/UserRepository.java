package com.safefaces.safefaces.Backend.Repository;

import com.safefaces.safefaces.Backend.DatabaseConnection;
import com.safefaces.safefaces.Backend.Model.Enums.RoleType;
import com.safefaces.safefaces.Backend.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//spara användaren med hjälp av databasen
public class UserRepository {

    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.id = rs.getInt("id");
                user.firstName = rs.getString("name");
                user.age = rs.getInt("age");
                user.pinHash = rs.getString("pin_hash");
                user.role = RoleType.valueOf(rs.getString("role").toUpperCase());

                return user;

            }
        }
        return null;
    }

    public User getPatientUser() throws SQLException {
        String sql = "SELECT * FROM users WHERE role = 'user' LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.id        = rs.getInt("id");
                user.firstName = rs.getString("name");
                user.age       = rs.getInt("age");
                user.pinHash   = rs.getString("pin_hash");
                user.imagePath = rs.getString("image_path");
                user.role      = RoleType.valueOf(rs.getString("role").toUpperCase());
                return user;
            }
        }
        return null;
    }
}
