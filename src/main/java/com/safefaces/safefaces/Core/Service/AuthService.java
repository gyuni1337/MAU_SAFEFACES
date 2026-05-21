package com.safefaces.safefaces.Core.Service;

import com.safefaces.safefaces.Core.Model.Enums.RoleType;
import com.safefaces.safefaces.Core.Model.User;
import com.safefaces.safefaces.Core.Repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;

/**
 * Service class responsible for handling authentication logic.
 * Provides methods for user login via PIN and Face ID, as well as
 * hashing functionality for secure PIN handling.
 *
 * @author Noor Nabi
 * @author Gyundyuz Sadulov
 */
public class AuthService {
    private final UserRepository userRepository = new UserRepository();

    /**
     * Attempts to authenticate a user using a username and PIN code.
     *
     * @param username the username of the user
     * @param pin the plain-text PIN code entered by the user
     * @return a {@link User} object if authentication is successful;
     *         otherwise {@code null}
     */
    public User login(String username, String pin) {

        try {
            User user = userRepository.getUserByUsername(username);

            if (user != null && hashPin(pin).equals(user.pinHash)) {
                return user; // inte klar
            }
        } catch (SQLException e) {
            System.err.println("Database error while logging in: " + e.getMessage());
        } return null;
    }

    /**
     * Attempts to authenticate a user using Face ID.
     * Currently retrieves a default user with role "user".
     *
     * @return a {@link User} if authentication is successful;
     *         otherwise {@code null}
     */
    public User faceIdLogin() {

        try {
            return userRepository.getPatientUser();
        } catch (SQLException e) {
            System.err.println("Database error while trying logging in with FaceID: " + e.getMessage());
            return null;
        }
    }

    /**
     * Hashes a PIN code using the SHA-256 algorithm.
     *
     * @param pin the plain-text PIN code
     * @return a Base64-encoded hash of the PIN
     * @throws RuntimeException if the SHA-256 algorithm is not available
     */
    public static String hashPin(String pin) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pin.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 saknas", e);
        }
    }


    public boolean verifyPin(User user, String pin) {
        return hashPin(pin).equals(user.pinHash);
    }

    public User getDemoUser() {
        User demo = new User();
        demo.id        = 1;
        demo.firstName = "Henry";
        demo.imagePath = "oldmanexample.jpg";
        demo.role      = RoleType.USER;
        demo.pinHash   = "";
        return demo;
    }
}
