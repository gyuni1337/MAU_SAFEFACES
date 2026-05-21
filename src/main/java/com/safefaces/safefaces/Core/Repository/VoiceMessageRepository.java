package com.safefaces.safefaces.Backend.Repository;
import com.safefaces.safefaces.Backend.DatabaseConnection;

import java.sql.*;

/**
 * Repository class responsible for handling CRUD operations
 * related to voice messages in the database.
 *
 * @author Hamdi Ahmed
 */
public class VoiceMessageRepository {

    /**
     * Retrieves the most recent voice message file path for a given contact ID.
     *
     * @param contactId the ID of the contact whose voice message is requested
     * @return the file path of the most recent voice message,
     *         or {@code null} if none is found or an error occurs
     */
    public String getFilePathByContactId(int contactId){

        String sql = """ 
                    SELECT file_path FROM safefaces.voice_messages Where contact_id =?
                    ORDER BY created_at DESC LIMIT 1
                    """;
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1,contactId);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()){
                return rs.getString("file_path");
            }

        }catch(SQLException e){
            System.out.println(" Error in getFilePathByContactID: " + e.getMessage());
        }

        return null;
    }


    /**
     * Saves a new voice message in the database.
     *
     * @param contactId the ID of the contact associated with the voice message
     * @param filePath the file path where the voice message is stored
     * @param durationSec the duration of the voice message in seconds
     * @return the generated ID of the saved voice message,
     *         or {@code -1} if the save operation fails
     */
    public int save(int contactId, String filePath,int durationSec) {
        String sql =
                """
                INSERT INTO safefaces.voice_messages (contact_id, file_path, duration_sec)
                VALUES (?, ?, ?) RETURNING id
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, contactId);
            stmt.setString(2, filePath);
            stmt.setInt(3, durationSec);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Error in save: " + e.getMessage());
        }
        return -1;
    }
}
