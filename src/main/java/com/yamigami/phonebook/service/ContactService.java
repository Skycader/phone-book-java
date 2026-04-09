package com.yamigami.phonebook.service;

import com.yamigami.phonebook.model.Contact;
import com.yamigami.phonebook.repository.ContactRepository;

import java.util.List;

public class ContactService {
    private final ContactRepository repository;
    private static final int PAGE_SIZE = 5;

    // Dependency Injection через конструктор
    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    public void addContact(String phone, String name, String address) {
        Contact existing = repository.findByPhoneExact(phone);
        if (existing != null) {
            throw new IllegalArgumentException("Контакт с таким номером уже существует.");
        }
        repository.save(new Contact(phone, name, address));
    }

    public List<Contact> listPage(int pageNum) {
        int offset = pageNum * PAGE_SIZE;
        return repository.findAll(PAGE_SIZE, offset);
    }

    public int getTotalPages() {
        long count = repository.count();
        return (int) Math.ceil((double) count / PAGE_SIZE);
    }

    public List<Contact> searchByPhone(String query) {
        return repository.findByPhone(query);
    }

    public List<Contact> searchByName(String query) {
        return repository.findByName(query);
    }

    public boolean deleteContact(String phone) {
        return repository.deleteByPhone(phone);
    }

    public void editContact(String phone, String newName, String newAddress) {
        Contact contact = repository.findByPhoneExact(phone);
        if (contact == null) {
            throw new IllegalArgumentException("Контакт не найден.");
        }
        contact.name = newName;
        contact.address = newAddress;
        repository.save(contact);
    }
}