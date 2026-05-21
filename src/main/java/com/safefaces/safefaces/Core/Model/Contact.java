package com.safefaces.safefaces.Core.Model;

/**
 * Represents a contact in the application.
 * Stores personal information such as name, phone number,
 * relationship, profile image, and associated voice message.
 *
 * @author Noor Nabi
 * @author Emma Yousif
 * @author Hamdi Ahmed
 */
public class Contact {

    private final String name;
    private final String phoneNumber;
    private final String relation;
    private final String imagePath;
    private final String voicePath;

    /**
     * Creates a new Contact with full details.
     *
     * @param name        the name of the contact
     * @param phoneNumber the contact's phone number
     * @param relation    the relationship to the contact
     * @param imagePath   the file path to the contact's profile image
     * @param voicePath   the file path to the contact's voice message
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
     * Defaults the relation to "Kontakt".
     *
     * @param name        the name of the contact
     * @param phoneNumber the contact's phone number
     * @param imagePath   the file path to the contact's profile image
     * @param voicePath   the file path to the contact's voice message
     */
    public Contact(String name, String phoneNumber,
                   String imagePath, String voicePath) {
        this(name, phoneNumber, "Kontakt", imagePath, voicePath);
    }

    public String getName()        { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getRelation()    { return relation; }
    public String getImagePath()   { return imagePath; }
    public String getVoicePath()   { return voicePath; }
}
