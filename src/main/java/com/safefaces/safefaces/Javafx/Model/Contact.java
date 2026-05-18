package com.safefaces.safefaces.Javafx.Model;

/**
 * Represents a contact in the application.
 * Stores personal information such as name, phone number,
 * relationship, profile image, and associated voice message.
 *
 * This model is primarily used in the UI layer to display
 * contact-related data.
 *
 * @author Noor Nabi
 * @author Emma Yousif
 * @author Hamdi Ahmed
 */
public class Contact {

    /** The name of the contact. */
    private final String name;

    /** The contact's phone number. */
    private final String phoneNumber;

    /** The relationship to the contact (e.g., family, friend). */
    private final String relation;

    /** The file path to the contact's profile image. */
    private final String imagePath;

    /** The file path to the contact's voice message. */
    private final String voicePath;

    /**
     * Creates a new Contact with full details.
     *
     * @param name the name of the contact
     * @param phoneNumber the contact's phone number
     * @param relation the relationship to the contact
     * @param imagePath the file path to the contact's profile image
     * @param voicePath the file path to the contact's voice message
     */
    public Contact(String name, String phoneNumber,
                   String relation, String imagePath, String voicePath) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.relation = relation;
        this.imagePath = imagePath;
        this.voicePath = voicePath;
    }

    /**
     * Creates a new Contact without specifying a relation.
     * A default relation value ("Kontakt") will be used.
     *
     * Maintained for backward compatibility with older code.
     *
     * @param name the name of the contact
     * @param phoneNumber the contact's phone number
     * @param imagePath the file path to the contact's profile image
     * @param voicePath the file path to the contact's voice message
     */
    public Contact(String name, String phoneNumber,
                   String imagePath, String voicePath) {
        this(name, phoneNumber, "Kontakt", imagePath, voicePath);
    }


    /**
     * Returns the name of the contact.
     *
     * @return the contact's name
     */
    public String getName()        { return name; }

    /**
     * Returns the contact's phone number.
     *
     * @return the phone number of the contact
     */
    public String getPhoneNumber() { return phoneNumber; }

    /**
     * Returns the relationship to the contact.
     *
     * @return the relation (e.g., family, friend)
     */
    public String getRelation()    { return relation; }

    /**
     * Returns the path to the contact's profile image.
     *
     * @return the image file path
     */
    public String getImagePath()   { return imagePath; }

    /**
     * Returns the path to the contact's voice message.
     *
     * @return the voice message file path
     */
    public String getVoicePath()   { return voicePath; }
}