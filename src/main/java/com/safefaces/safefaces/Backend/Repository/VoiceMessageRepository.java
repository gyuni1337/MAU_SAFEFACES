package com.safefaces.safefaces.Backend.Repository;
import com.safefaces.safefaces.Backend.DatabaseConnection;

import java.sql.*;

/**
 * This class handles CRUD for the voice_message tables
 */

public class VoiceMessageRepository {




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
    public int save(int contactId, String filePath,int durationSec) {
        String sql =
                """
                INSERT INTO safefaces.
                (contact_id, file_path, duration_se
                VALUES
                 """;

        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1,contactId);
            stmt.setString(2 ,filePath);
            stmt.setInt(3, durationSec);
            ResultSet rs = stmt.executeQuery() ;
            if(rs.next()) return rs.getInt("id");


    }catch (SQLException e){
            System.out.println(" Error in save: " + e.getMessage(

        ));

    }
     return -1;

  }

}
