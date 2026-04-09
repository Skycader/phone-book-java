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
        System.out.println("=== Phone Book ===");
        System.out.println("Type 'help' for available commands\n");

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
                default -> System.out.println("Unknown command. Type 'help' for usage.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private void handleAdd(String args) {
        String[] p = args.split("\\s+", 3);
        if (p.length < 2) throw new IllegalArgumentException("Usage: add <phone> <name> [address]");
        String phone = p[0];
        String name = p[1];
        String address = p.length > 2 ? p[2] : "";
        service.addContact(phone, name, address);
        System.out.println("Contact added successfully.");
    }

    private void handleList(String args) {
        int page = 0;
        if (!args.isEmpty()) {
            try { page = Integer.parseInt(args); }
            catch (NumberFormatException e) { throw new IllegalArgumentException("Page number must be an integer."); }
        }

        List<Contact> contacts = service.listPage(page);
        if (contacts.isEmpty()) {
            System.out.println("List is empty or page does not exist.");
            return;
        }

        System.out.println("=== Page " + page + " ===");
        for (Contact c : contacts) {
            System.out.println(c);
        }
        System.out.println("(Total pages: " + service.getTotalPages() + ")");
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
            throw new IllegalArgumentException("Usage: search tel:<...> or search name:<...>");
        }
    }

    private void handleDelete(String args) {
        if (!args.startsWith("num:")) {
            throw new IllegalArgumentException("Usage: delete num:<phone>");
        }
        String phone = args.substring(4);
        if (service.deleteContact(phone)) {
            System.out.println("Contact deleted.");
        } else {
            System.out.println("Contact not found.");
        }
    }

    private void handleEdit(String args) {
        String[] p = args.split("\\s+", 3);
        if (p.length < 2 || !p[0].startsWith("num:")) {
            throw new IllegalArgumentException("Usage: edit num:<phone> <name> [address]");
        }
        String phone = p[0].substring(4);
        String name = p[1];
        String address = p.length > 2 ? p[2] : "";
        service.editContact(phone, name, address);
        System.out.println("Contact updated.");
    }

    private void printResults(List<Contact> contacts) {
        if (contacts.isEmpty()) {
            System.out.println("No results found.");
        } else {
            for (Contact c : contacts) {
                System.out.println(c);
            }
        }
    }

    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  add <phone> <name> [address]     - Add a contact");
        System.out.println("  list [page]                      - Show contacts (5 per page)");
        System.out.println("  search tel:<part>                - Search by phone");
        System.out.println("  search name:<part>               - Search by name");
        System.out.println("  delete num:<phone>               - Delete a contact");
        System.out.println("  edit num:<phone> <name> [addr]   - Edit a contact");
        System.out.println("  help                             - Show this help");
        System.out.println("  exit / quit                      - Exit application");
    }
}