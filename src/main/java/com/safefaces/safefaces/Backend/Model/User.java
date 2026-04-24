package com.safefaces.safefaces.Backend.Model;

import com.safefaces.safefaces.Backend.Model.Enums.RoleType;

public class User {
    public int id;
    public String firstName;
    public String lastName;
    public int age;
    public RoleType role;
    public String pinHash;
    public String imagePath;

    public User() {

    }
}
