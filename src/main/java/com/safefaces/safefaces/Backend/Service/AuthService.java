package com.safefaces.safefaces.Backend.Service;

import com.safefaces.safefaces.Backend.Model.User;
import com.safefaces.safefaces.Backend.Repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Base64;

public class AuthService {
    private final UserRepository userRepository = new UserRepository();

    //felhantering för användare
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

    //felhantering för faceId
    public User faceIdLogin() {

        try {
            return userRepository.getPatientUser();
        } catch (SQLException e) {
            System.err.println("Database error while trying logging in with FaceID: " + e.getMessage());
            return null;
        }
    }

    //SHA är en digest algoritm, som felhanterar pinkod osv..
    //Digest kommer från klassbiblioteket MessageDigest
    //klassen "digestar" pinkod
    public static String hashPin(String pin) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pin.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 saknas", e);
        }
    }
}
