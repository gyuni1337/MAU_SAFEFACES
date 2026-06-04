package com.safefaces.safefaces.Javafx.App;

import com.safefaces.safefaces.Core.Model.User;
public class AppState {
    // Simple session holder shared by the JavaFX controllers.
    private static AppState instance;
    private AppState() {
    }
    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }

        return instance;
    }
    private User currentUser;
    public User getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    public void logout() {
        currentUser = null;
    }
}
