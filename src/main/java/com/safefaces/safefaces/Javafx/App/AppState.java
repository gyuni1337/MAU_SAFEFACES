package com.safefaces.safefaces.Javafx.App;

import com.safefaces.safefaces.Backend.Model.User;

public class AppState {
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
