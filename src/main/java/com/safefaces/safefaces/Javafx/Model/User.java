package com.safefaces.safefaces.Javafx.Model;

/**
 * Abstract base class representing a user in the system.
 * Defines common properties and behavior shared by all user types,
 * such as identification, authentication, and profile information.
 *
 * This class is intended to be extended by specific user roles
 * (e.g., caregivers, relatives) that provide their own implementation
 * of the {@link #setupView()} method.
 *
 * @author Emma Yousif
 */
public abstract class User {

    /** The unique identifier of the user. */
    private int userID;

    /** The display name of the user. */
    private String name;

    /** The file path to the user's profile image. */
    private String imagePath;

    /** The username used for authentication. */
    private String username;

    /** The user's password (should be stored securely). */
    private String password;

    /**
     * Protected default constructor to allow subclass instantiation.
     */
    protected User() {
    }


    /**
     * Constructs a user with all required properties.
     *
     * @param userID the unique identifier of the user
     * @param name the user's name
     * @param imagePath the path to the user's profile image
     * @param username the username used for login
     * @param password the user's password
     */
    public User(int userID, String name, String imagePath,
                String username, String password) {
        this.userID = userID;
        this.name = name;
        this.imagePath = imagePath;
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the user's unique identifier.
     *
     * @return the user ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Returns the user's name.
     *
     * @return the user's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the path to the user's profile image.
     *
     * @return the image file path
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Returns the username used for login.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the user's password.
     *
     * @return the password (should be handled securely)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the path to the user's profile image.
     *
     * @param imagePath the new image path
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * Sets the username used for login.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the user's password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Abstract method used to set up the UI or behavior
     * associated with a specific user type.
     * Must be implemented by all subclasses.
     */
    public abstract void setupView();
}
