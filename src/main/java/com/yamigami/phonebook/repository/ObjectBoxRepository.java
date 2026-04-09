package com.yamigami.phonebook.repository;

import com.yamigami.phonebook.model.Contact;
import com.yamigami.phonebook.model.Contact_;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.QueryBuilder.StringOrder; // 🔥 Импорт StringOrder

import java.util.List;

public class ObjectBoxRepository implements ContactRepository {
    private final Box<Contact> contactBox;

    public ObjectBoxRepository(BoxStore boxStore) {
        this.contactBox = boxStore.boxFor(Contact.class);
    }

    @Override
    public void save(Contact contact) {
        contactBox.put(contact);
    }

    @Override
    public List<Contact> findAll(int limit, int offset) {
        return contactBox.query().build().find(offset, limit);
    }

    @Override
    public List<Contact> findByPhone(String phonePart) {
        // 🔥 Третий аргумент: StringOrder
        return contactBox.query()
                .contains(Contact_.phone, phonePart, StringOrder.CASE_INSENSITIVE)
                .build()
                .find();
    }

    @Override
    public List<Contact> findByName(String namePart) {
        return contactBox.query()
                .contains(Contact_.name, namePart, StringOrder.CASE_INSENSITIVE)
                .build()
                .find();
    }

    @Override
    public boolean deleteByPhone(String phone) {
        Contact contact = findByPhoneExact(phone);
        if (contact != null) {
            contactBox.remove(contact);
            return true;
        }
        return false;
    }

    @Override
    public Contact findByPhoneExact(String phone) {
        // 🔥 equal тоже требует StringOrder
        return contactBox.query()
                .equal(Contact_.phone, phone, StringOrder.CASE_INSENSITIVE)
                .build()
                .findFirst();
    }

    @Override
    public long count() {
        return contactBox.count();
    }
}