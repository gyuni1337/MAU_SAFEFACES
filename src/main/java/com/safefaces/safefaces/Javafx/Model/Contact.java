package com.safefaces.safefaces.Javafx.Model;

public class Contact {
    private String name;
    private String phoneNumber;
    private String relation;
    private String imagePath;
    private String voicePath;

    public Contact(String name, String phoneNumber,
                   String relation, String imagePath, String voicePath) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.relation = relation;
        this.imagePath = imagePath;
        this.voicePath = voicePath;
    }

    // Bakåtkompatibel konstruktor (för gammal kod som inte skickar relation)
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