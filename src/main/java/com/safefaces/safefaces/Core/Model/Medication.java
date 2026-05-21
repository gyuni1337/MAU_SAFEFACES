package com.safefaces.safefaces.Core.Model;

/**
 * Represents a medication entry for a user.
 *
 * @author Gyundyuz Sadulov
 */
public class Medication {
    public int id;
    public int userId;
    public String name;
    public String dose;
    public String timeOfDay;
    public boolean isActive;
}
