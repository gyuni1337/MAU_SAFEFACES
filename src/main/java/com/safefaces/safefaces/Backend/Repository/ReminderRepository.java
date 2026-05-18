package com.safefaces.safefaces.Backend.Repository;
import com.safefaces.safefaces.Backend.Model.Reminder;
import com.safefaces.safefaces.Backend.DatabaseConnection;
import java.sql.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * This class imports reminders from the reminder table at safefaces database.
 */


public class ReminderRepository {


    private final int userId;

    public ReminderRepository(int userId){
        this.userId = userId;
    }
    /**
     * Collects all the active reminders for the user
     * sorted after time
     */

    public List<Reminder> getActiveReminders(){
        List<Reminder> list = new ArrayList<>();

        String sql = """
                
                SELECT title, description, reminder_time, reminder_type
                FROM safefaces.reminders
                WHERE user_id = ? AND is_active = true
                ORDER BY reminder_time ASC
                """;
        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
            Reminder r = new Reminder();
            r.title = rs.getString("title");
            r.description = rs.getString("description");
            r.startTime = rs.getTime("reminder_time");
            list.add(r);
            }
        } catch (SQLException e) {
            System.out.println(" ERROR in getActiveReminders: " + e.getMessage());
        }
        return list;
    }


}
