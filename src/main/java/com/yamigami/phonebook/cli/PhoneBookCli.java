package com.yamigami.phonebook.cli;

import com.yamigami.phonebook.model.Contact;
import com.yamigami.phonebook.service.ContactService;

import java.util.List;
import java.util.Scanner;

public class PhoneBookCli {
    private final ContactService service;
    private final Scanner scanner;
    private boolean running = true;

    public PhoneBookCli(ContactService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== Справочник номеров ===");
        System.out.println("Введите 'help' для списка команд\n");

        while (running) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split("\\s+", 2);
            String cmd = parts[0];
            String args = parts.length > 1 ? parts[1] : "";

            execute(cmd, args);
        }
    }

    private void execute(String cmd, String args) {
        try {
            switch (cmd) {
                case "help" -> printHelp();
                case "exit", "quit" -> running = false;
                case "add" -> handleAdd(args);
                case "list" -> handleList(args);
                case "search" -> handleSearch(args);
                case "delete" -> handleDelete(args);
                case "edit" -> handleEdit(args);
                default -> System.out.println("Неизвестная команда. Введите 'help'.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
        }
    }

    private void handleAdd(String args) {
        String[] p = args.split("\\s+", 3);
        if (p.length < 2) throw new IllegalArgumentException("Использование: add <телефон> <ФИО> [адрес]");
        String phone = p[0];
        String name = p[1];
        String address = p.length > 2 ? p[2] : "";
        service.addContact(phone, name, address);
        System.out.println("Контакт успешно добавлен.");
    }

    private void handleList(String args) {
        int page = 0;
        if (!args.isEmpty()) {
            try { page = Integer.parseInt(args); }
            catch (NumberFormatException e) { throw new IllegalArgumentException("Номер страницы должен быть числом."); }
        }

        List<Contact> contacts = service.listPage(page);
        if (contacts.isEmpty()) {
            System.out.println("Список пуст или страница не существует.");
            return;
        }

        System.out.println("=== Страница " + page + " ===");
        for (Contact c : contacts) {
            System.out.println(c);
        }
        System.out.println("(Всего страниц: " + service.getTotalPages() + ")");
    }

    private void handleSearch(String args) {
        if (args.startsWith("tel:")) {
            String query = args.substring(4);
            List<Contact> results = service.searchByPhone(query);
            printResults(results);
        } else if (args.startsWith("name:")) {
            String query = args.substring(5);
            List<Contact> results = service.searchByName(query);
            printResults(results);
        } else {
            throw new IllegalArgumentException("Использование: search tel:<...> или search name:<...>");
        }
    }

    private void handleDelete(String args) {
        if (!args.startsWith("num:")) {
            throw new IllegalArgumentException("Использование: delete num:<телефон>");
        }
        String phone = args.substring(4);
        if (service.deleteContact(phone)) {
            System.out.println("Контакт удален.");
        } else {
            System.out.println("Контакт не найден.");
        }
    }

    private void handleEdit(String args) {
        String[] p = args.split("\\s+", 3);
        if (p.length < 2 || !p[0].startsWith("num:")) {
            throw new IllegalArgumentException("Использование: edit num:<телефон> <ФИО> [адрес]");
        }
        String phone = p[0].substring(4);
        String name = p[1];
        String address = p.length > 2 ? p[2] : "";
        service.editContact(phone, name, address);
        System.out.println("Контакт обновлен.");
    }

    private void printResults(List<Contact> contacts) {
        if (contacts.isEmpty()) {
            System.out.println("Ничего не найдено.");
        } else {
            for (Contact c : contacts) {
                System.out.println(c);
            }
        }
    }

    private void printHelp() {
        System.out.println("Доступные команды:");
        System.out.println("  add <телефон> <ФИО> [адрес]      — добавить контакт");
        System.out.println("  list [страница]                  — показать список (по 5 на странице)");
        System.out.println("  search tel:<часть номера>        — поиск по телефону");
        System.out.println("  search name:<часть имени>        — поиск по имени");
        System.out.println("  delete num:<телефон>             — удалить контакт");
        System.out.println("  edit num:<телефон> <ФИО> [адрес] — редактировать контакт");
        System.out.println("  help                             — эта справка");
        System.out.println("  exit / quit                      — выйти");
    }
}