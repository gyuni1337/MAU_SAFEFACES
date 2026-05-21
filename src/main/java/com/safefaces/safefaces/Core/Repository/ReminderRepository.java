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
                SELECT id, title, description, start_time, end_time, reminder_type
                FROM safefaces.reminders
                WHERE user_id = ? AND is_active = true
                ORDER BY start_time ASC
                """;
        try(Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
            Reminder r = new Reminder();
            r.id          = rs.getInt("id");
            r.title       = rs.getString("title");
            r.description = rs.getString("description");
            r.startTime   = rs.getTime("start_time");
            r.endTime     = rs.getTime("end_time");
            list.add(r);
            }
        } catch (SQLException e) {
            System.out.println(" ERROR in getActiveReminders: " + e.getMessage());
        }
        return list;
    }

    /**
     * Inserts a new reminder for a given user.
     *
     * @param userId   the patient's user ID
     * @param reminder the reminder to save
     */
    /**
     * Deletes a reminder by its ID.
     *
     * @param reminderId the ID of the reminder to delete
     */
    public void deleteById(int reminderId) {
        String sql = "DELETE FROM safefaces.reminders WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reminderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ReminderRepository.deleteById error: " + e.getMessage());
        }
    }

    public void save(int userId, Reminder reminder) {
        String sql = """
                INSERT INTO safefaces.reminders
                    (user_id, title, description, reminder_type, start_time, is_active)
                VALUES (?, ?, ?, ?, ?, TRUE)
                """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, reminder.title);
            stmt.setString(3, reminder.description);
            stmt.setString(4, reminder.reminderType);
            stmt.setTime(5, reminder.startTime);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("ReminderRepository.save error: " + e.getMessage());
        }
    }
}
