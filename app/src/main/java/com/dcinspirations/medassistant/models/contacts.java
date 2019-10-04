package com.dcinspirations.medassistant.models;

public class contacts {
    public String name,status,email, number;
    public int id;

    public contacts(int id,String name, String status, String email,  String number) {
        this.name = name;
        this.status = status;
        this.email = email;
        this.id = id;
        this.number = number;
    }
}
