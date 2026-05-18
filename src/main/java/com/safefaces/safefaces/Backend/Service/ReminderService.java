package com.safefaces.safefaces.Backend.Service;
import com.safefaces.safefaces.Backend.Model.Reminder;
import com.safefaces.safefaces.Backend.Repository.ReminderRepository;
import java.util.List;

public class ReminderService {

    private final ReminderRepository repo;

    public ReminderService(int userId){
       this.repo = new ReminderRepository(userId) ;

    }
    public List<Reminder> getActiveReminders(){
        return repo.getActiveReminders();
    }
}
