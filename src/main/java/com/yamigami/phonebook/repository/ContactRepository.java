package com.yamigami.phonebook.repository;

import com.yamigami.phonebook.model.Contact;
import java.util.List;

public interface ContactRepository {
    void save(Contact contact);
    List<Contact> findAll(int limit, int offset);
    List<Contact> findByPhone(String phonePart);
    List<Contact> findByName(String namePart);
    boolean deleteByPhone(String phone);
    Contact findByPhoneExact(String phone);
    long count();
}