package com.yamigami.phonebook.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "contacts")
public class Contact {
    @DatabaseField(generatedId = true)
    public long id;

    @DatabaseField(canBeNull = false, unique = true, index = true)
    public String phone;

    @DatabaseField(canBeNull = false)
    public String name;

    @DatabaseField
    public String address;

    // ORMLite требует пустой конструктор
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