package com.yamigami.phonebook;

import com.yamigami.phonebook.cli.PhoneBookCli;
import com.yamigami.phonebook.repository.ContactRepository;
import com.yamigami.phonebook.repository.OrmLiteRepository;
import com.yamigami.phonebook.service.ContactService;

public class Main {
    public static void main(String[] args) {
        // Инициализация репозитория с SQLite
        ContactRepository repository = new OrmLiteRepository("phonebook.db");

        // Создание сервиса и CLI
        ContactService service = new ContactService(repository);
        PhoneBookCli cli = new PhoneBookCli(service);

        // Запуск приложения
        cli.start();
    }
}