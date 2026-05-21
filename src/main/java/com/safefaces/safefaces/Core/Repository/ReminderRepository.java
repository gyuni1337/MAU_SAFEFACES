package com.safefaces.safefaces.Core.Repository;
import com.safefaces.safefaces.Core.Model.Reminder;
import com.safefaces.safefaces.Core.DatabaseConnection;
import java.sql.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class responsible for retrieving reminder data from the database.
 * Provides methods to access active reminders for a specific user.
 *
 * @author Hamdi Ahmed
 */
public class ReminderRepository {

    /** The ID of the user whose reminders are being accessed. */
    private final int userId;


    /**
     * Creates a new ReminderRepository for a specific user.
     *
     * @param userId the unique identifier of the user
     */
    public ReminderRepository(int userId){
        this.userId = userId;
    }


    /**
     * Retrieves all active reminders for the user from the database,
     * sorted by reminder time in ascending order.
     *
     * @return a list of active {@link Reminder} objects;
     *         returns an empty list if no reminders are found or an error occurs
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
