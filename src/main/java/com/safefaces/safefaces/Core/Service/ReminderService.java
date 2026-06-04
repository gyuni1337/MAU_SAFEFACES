package com.safefaces.safefaces.Core.Service;
import com.safefaces.safefaces.Core.Model.Reminder;
import com.safefaces.safefaces.Core.Repository.ReminderRepository;
import java.util.List;

/**
 * Service class responsible for handling business logic
 * related to reminders. Acts as a bridge between the controller
 * layer and the repository layer.
 *
 * @author Hamdi Ahmed
 */
public class ReminderService {

    /** Repository used to access reminder data. */
    private final ReminderRepository repo;

    /**
     * Creates a ReminderService for a specific user.
     *
     * @param userId the unique identifier of the user whose reminders will be accessed
     */
    public ReminderService(int userId){
       this.repo = new ReminderRepository(userId) ;

    }

    /**
     * Retrieves all active reminders for the user.
     *
     * @return a list of active {@link Reminder} objects
     */
    public List<Reminder> getActiveReminders(){
        return repo.getActiveReminders();
    }

    public void deleteById(int reminderId) {
        repo.deleteById(reminderId);
    }
}
