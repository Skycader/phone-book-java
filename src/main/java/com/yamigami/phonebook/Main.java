package com.yamigami.phonebook;

import com.yamigami.phonebook.cli.PhoneBookCli;
import com.yamigami.phonebook.model.MyObjectBox;
import com.yamigami.phonebook.repository.ContactRepository;
import com.yamigami.phonebook.repository.ObjectBoxRepository;
import com.yamigami.phonebook.service.ContactService;
import io.objectbox.BoxStore;

public class Main {
    public static void main(String[] args) {
        // 1. Инициализация базы данных
        BoxStore boxStore = MyObjectBox.builder()
                .name("phonebook-db")
                .build();

        // 2. Создание репозитория
        // ПОЛИМОРФИЗМ: Чтобы сменить базу, замените эту строку на new RestApiRepository(...)
        ContactRepository repository = new ObjectBoxRepository(boxStore);

        // 3. Создание сервиса с внедрением репозитория
        ContactService service = new ContactService(repository);

        // 4. Запуск CLI
        PhoneBookCli cli = new PhoneBookCli(service);
        cli.start();

        // 5. Закрытие ресурсов
        boxStore.close();
    }
}