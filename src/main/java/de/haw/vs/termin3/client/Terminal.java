package de.haw.vs.termin3.client;

import java.util.Scanner;

public class Terminal {

    private final Client client;
    private final Scanner sc = new Scanner(System.in);

    public Terminal(Client client) {
        this.client = client;
    }

    public Terminal() throws Exception {
        this.client = new Client();
    }

    public void start() throws Exception {
        terminalLoop();
    }

    public void terminalLoop() throws Exception {
        while (true) {
            System.out.println("\nCommands: regist | unregist | list | quit");
            System.out.print("> ");
            String cmd = sc.nextLine().trim();

            switch (cmd) {
                case "regist" -> regist();
                case "unregist" -> unregist();
                case "list" -> list();
                case "quit" -> {
                    client.stop();
                    return;
                }
                default -> System.out.println("Unknown command.");
            }
        }
    }

    public void regist() throws Exception {
        System.out.print("Enter name: ");
        String name = sc.nextLine().trim();

        System.out.print("Type (client/robot): ");
        String type = sc.nextLine().trim();

        client.register(name, type);
    }

    public void unregist() throws Exception {
        System.out.print("Enter name: ");
        String name = sc.nextLine().trim();
        client.unregister(name);
    }

    public void list() throws Exception {
        System.out.print("List type (robot/client/all): ");
        String type = sc.nextLine().trim();
        client.list(type);
    }

}
