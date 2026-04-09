package com.yamigami.phonebook.model;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

@Entity
public class Contact {
    @Id
    public long id;

    @Index
    public String phone;

    public String name;
    public String address;

    public Contact() {}

    public Contact(String phone, String name, String address) {
        this.phone = phone;
        this.name = name;
        this.address = address;
    }

    @Override
    public String toString() {
        return String.format("%-15s | %-20s | %s", phone, name, address != null ? address : "");
    }
}