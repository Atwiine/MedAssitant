package com.dcinspirations.medassistant.models;

public class UserModel {
    public String Name, Email, Disorder, Gender;

    public UserModel(String name, String email, String disorder, String gender) {
        Name = name;
        Email = email;
        Disorder = disorder;
        Gender = gender;
    }

    public UserModel() {
    }
}
