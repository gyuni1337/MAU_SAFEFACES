package com.safefaces.safefaces.Core.Model;

public class Contact {

    private final String name;
    private final String phoneNumber;
    private final String relation;
    private final String imagePath;
    private final String voicePath;

    public Contact(String name, String phoneNumber,
                   String relation, String imagePath, String voicePath) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.relation = relation;
        this.imagePath = imagePath;
        this.voicePath = voicePath;
    }

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
