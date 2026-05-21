package com.safefaces.safefaces.Core.Service;

import com.safefaces.safefaces.Core.Model.User;
import com.safefaces.safefaces.Core.Repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

/**
 * Service class responsible for handling authentication logic.
 * PIN verification uses bcrypt to match the hashes stored in the database.
 *
 * @author Noor Nabi
 * @author Gyundyuz Sadulov
 */
public class AuthService {

    private final UserRepository userRepository = new UserRepository();

    /**
     * Authenticates a user by username and plain-text PIN.
     *
     * @param username the username
     * @param pin      the plain-text PIN entered by the user
     * @return the authenticated {@link User}, or {@code null} if credentials are wrong
     */
    public User login(String username, String pin) {
        try {
            User user = userRepository.getUserByUsername(username);
            if (user != null && BCrypt.checkpw(pin, user.pinHash)) {
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Database error while logging in: " + e.getMessage());
        }
        return null;
    }

    /**
     * Face ID login — fetches the default patient user from the DB.
     *
     * @return a {@link User}, or {@code null} if the DB is unavailable
     */
    public User faceIdLogin() {
        try {
            return userRepository.getPatientUser();
        } catch (SQLException e) {
            System.err.println("Database error during Face ID login: " + e.getMessage());
            return null;
        }
    }

    /**
     * Verifies a plain-text PIN against a stored bcrypt hash.
     *
     * @param user the user whose stored hash to check against
     * @param pin  the plain-text PIN to verify
     * @return {@code true} if the PIN matches
     */
    public boolean verifyPin(User user, String pin) {
        return BCrypt.checkpw(pin, user.pinHash);
    }

    /**
     * Generates a bcrypt hash for a given PIN.
     * Use this when creating new users manually.
     *
     * @param pin the plain-text PIN
     * @return a bcrypt hash string ready to store in the database
     */
    public static String hashPin(String pin) {
        return BCrypt.hashpw(pin, BCrypt.gensalt());
    }

}
