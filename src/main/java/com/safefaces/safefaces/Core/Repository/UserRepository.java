package com.safefaces.safefaces.Core.Repository;

import com.safefaces.safefaces.Core.DatabaseConnection;
import com.safefaces.safefaces.Core.Model.Enums.RoleType;
import com.safefaces.safefaces.Core.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Repository class responsible for retrieving user data from the database.
 * Provides methods to fetch users by username or by role.
 *
 * @author Noor Nabi
 * @author Gyundyuz Sadulov
 */
public class UserRepository {


    /**
     * Retrieves a user from the database based on the provided username.
     *
     * @param username the username used to search for the user
     * @return a {@link User} object if a matching user is found;
     *         otherwise {@code null}
     * @throws SQLException if a database access error occurs
     */
    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";

        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.id        = rs.getInt("id");
                    user.firstName = rs.getString("first_name");
                    user.lastName  = rs.getString("last_name");
                    user.age       = rs.getInt("age");
                    user.pinHash   = rs.getString("pin_hash");
                    user.imagePath = rs.getString("image_path");
                    user.role      = RoleType.valueOf(rs.getString("role").toUpperCase());
                    user.location  = rs.getString("location");
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves a single user with the role "user" from the database.
     * This method is typically used to fetch a patient or default user.
     *
     * @return a {@link User} with the role "user" if found;
     *         otherwise {@code null}
     * @throws SQLException if a database access error occurs
     */
    public User getPatientUser() throws SQLException {
        String sql = "SELECT * FROM safefaces.users WHERE UPPER(role) = 'USER' LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.id        = rs.getInt("id");
                user.firstName = rs.getString("first_name");
                user.lastName  = rs.getString("last_name");
                user.age       = rs.getInt("age");
                user.pinHash   = rs.getString("pin_hash");
                user.imagePath = rs.getString("image_path");
                user.role      = RoleType.valueOf(rs.getString("role").toUpperCase());
                user.location  = rs.getString("location");
                return user;
            }
        }
        return null;
    }
}
