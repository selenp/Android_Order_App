package com.example.bohan.finaldemo;

/**
 * Created by bohan on 11/24/17.
 */

public class UserAccount {

    public String username;
    public String password;
    public String email;

    public UserAccount(){

    }

    public UserAccount(String username, String password, String email)
    {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
