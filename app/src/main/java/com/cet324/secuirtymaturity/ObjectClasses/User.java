package com.cet324.secuirtymaturity.ObjectClasses;

/**
 * Created by DanielWilkinson on 1/1/2017.
 */
public class User {
    //user variables

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    String salt;

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    String original;
    public String getId() {
        return id;
    }public void setId(String id) {
        this.id = id;
    } String email,pass,key,id;
    //user getter and setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    //constructor 1
    public User(){}
    //constructor 2
    public User(String email, String pass, String key, String salt, String original){
        this.email = email;
        this.pass = pass;
        this.key = key;
        this.salt = salt;
        this.original = original;
    }

}
