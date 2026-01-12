package de.haw.vs.termin3.client;

import java.util.Scanner;

public class Terminal {

    private final Client client;
    private final Scanner sc = new Scanner(System.in);
    private String name;

    public Terminal(Client client) {
        this.client = client;
    }

    public Terminal() throws Exception {
        this.client = new Client();
        this.name = "";
    }

    public void start() throws Exception {
        login();
        terminalLoop();
    }

    private void login() {
        System.out.println("Please enter a user name");
        while (true) {
            System.out.print("> ");
            String name = sc.nextLine().trim();
            try {
                client.register(name);
                this.name = name;
                return;
            } catch (RuntimeException e) {
                System.err.println("Login failed: " + e.getMessage());
            }
        }
    }

    public void terminalLoop() throws Exception {
        help();
        while (true) {
            System.out.print("> ");
            String cmd = sc.nextLine().trim();

            switch (cmd) {
                case "help" -> help();
                case "list" -> list();
                case "quit" -> {
                    client.unregister(this.name);
                    client.stop();
                    return;
                }
                default -> System.out.println("Unknown command.");
            }
        }
    }

    private void help() {
        System.out.println("\nCommands: list | quit");
    }

    public void list() throws Exception {
        System.out.print("List type (robot/client/all): ");
        String type = sc.nextLine().trim();
        client.list(type);
    }

}
