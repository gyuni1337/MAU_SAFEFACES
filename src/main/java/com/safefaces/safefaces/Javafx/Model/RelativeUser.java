package com.safefaces.safefaces.Javafx.Model;

/**
 * Represents a relative user in the system.
 * Extends the base {@link User} class and allows for
 * role-specific customization related to relatives.
 *
 * This class can be used to implement behavior or UI
 * logic specific to users with the "relative" role.
 *
 * @author Emma Yousif
 */
public class RelativeUser extends UserViewModel {

    /**
     * Sets up the UI view associated with a relative user.
     * This method is intended to be overridden with logic
     * specific to relatives, such as displaying relevant
     * information or controls.
     */
    @Override
    public void setupView() {
        // Implementation for relative-specific UI setup
    }
}
