package com.example.motherscaremod;


/**
 * User model class
 */
public class User
{

    // Fields

    public String name, password;
    public UserRole userRole;


    // Constructor

    public User(String name, String password, UserRole userRole)
    {
        this.name = name;
        this.password = password;
        this.userRole = userRole;
    }

}
