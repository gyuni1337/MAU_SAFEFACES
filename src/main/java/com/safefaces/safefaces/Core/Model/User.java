package com.safefaces.safefaces.Core.Model;

import com.safefaces.safefaces.Core.Model.Enums.RoleType;

/**
 * Represents a user within the system.
 * Stores personal information, authentication details, and role.
 *
 * @author Gyundyuz Sadulov
 * @author Noor Nabi
 * @author Hamdi Ahmed
 */
public class User {

    /** The unique identifier of the user. */
    public int id;

    /** The user's first name. */
    public String firstName;

    /** The user's last name. */
    public String lastName;

    /** The user's age. */
    public int age;

    /** The role assigned to the user. */
    public RoleType role;

    /** The hashed PIN code used for authentication. */
    public String pinHash;

    /** The file path to the user's profile image. */
    public String imagePath;

    /** The user's home location (e.g. city). */
    public String location;

    /**
     * Default constructor for creating an empty User object.
     */
    public User() {
    }

    /**
     * Returns the unique identifier of the user.
     *
     * @return the user's ID
     */
    public int getId() {
        return id;
    }
}
