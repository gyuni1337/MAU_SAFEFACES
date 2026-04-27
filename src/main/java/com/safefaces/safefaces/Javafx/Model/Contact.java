package com.safefaces.safefaces.Javafx.Model;

public class Contact {
    private String name;
    private String phoneNumber;
    private String imagePath;
    private String voicePath;

    public Contact(String name, String phoneNumber,
                   String imagePath, String voicePath) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.imagePath = imagePath;
        this.voicePath = voicePath;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getVoicePath() {
        return voicePath;
    }
}
