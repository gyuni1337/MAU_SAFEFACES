package com.safefaces.safefaces.Core.Model;

import com.safefaces.safefaces.Core.Model.Enums.RoleType;

public class User {
    public int id;
    public String firstName;
    public String lastName;
    public int age;
    public RoleType role;
    public String pinHash;
    public String imagePath;
    public String location;

    public User() {}

    public int getId() {
        return id;
    }
}
