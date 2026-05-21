package com.safefaces.safefaces.Javafx.Model;
/**
 * Represents a caregiver user in the system.
 * Extends the base {@link User} class and can be customized
 * with caregiver-specific behavior and UI setup.
 *
 * This class is intended to override view-related functionality
 * specific to caregiver roles.
 *
 * @author Emma Yousif
 */
public class CareGiverUser extends UserViewModel {

    /**
     * Sets up the UI view associated with a caregiver user.
     * This method is intended to be overridden with functionality
     * specific to caregiver interactions and presentation logic.
     */
    @Override
    public void setupView() {
        // Implementation for caregiver-specific UI setup
    }
}
