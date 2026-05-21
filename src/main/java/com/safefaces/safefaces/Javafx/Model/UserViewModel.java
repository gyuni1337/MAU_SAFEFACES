package com.safefaces.safefaces.Javafx.Model;

/**
 * Abstract base class for UI-specific user representations.
 * Defines common properties and a hook for role-specific view setup.
 *
 * Subclasses (e.g. {@link RelativeUser}, {@link CareGiverUser}) implement
 * {@link #setupView()} to provide role-specific UI behaviour.
 *
 * @author Emma Yousif
 */
public abstract class UserViewModel {

    private int userID;
    private String name;
    private String imagePath;
    private String username;
    private String password;

    protected UserViewModel() {}

    public UserViewModel(int userID, String name, String imagePath,
                         String username, String password) {
        this.userID = userID;
        this.name = name;
        this.imagePath = imagePath;
        this.username = username;
        this.password = password;
    }

    public int getUserID()       { return userID; }
    public String getName()      { return name; }
    public String getImagePath() { return imagePath; }
    public String getUsername()  { return username; }
    public String getPassword()  { return password; }

    public void setName(String name)           { this.name = name; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public void setUsername(String username)   { this.username = username; }
    public void setPassword(String password)   { this.password = password; }

    /** Override to configure role-specific UI elements after login. */
    public abstract void setupView();
}
