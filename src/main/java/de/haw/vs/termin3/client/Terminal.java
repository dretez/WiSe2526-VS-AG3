package de.haw.vs.termin3.client;

import de.haw.vs.termin3.client.command.ClientCommand;

import java.io.IOException;
import java.util.Scanner;

public class Terminal {
    private final Scanner sc;
    private final Client client;
    private boolean running;

    private Terminal(Scanner scanner, Client client) {
        this.sc = scanner;
        this.client = client;
        this.running = true;
    }

    public Client client() {
        return client;
    }

    public static void start(String registryIP, int port) {
        login(registryIP, port).terminalLoop();
    }

    private static Terminal login(String registryIP, int port) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter a user name");
        while (true) {
            System.out.print("> ");
            String name = sc.nextLine().trim();
            try {
                Client client = new Client(name, registryIP, port);
                client.register();
                return new Terminal(sc, client);
            } catch (LoginFailureException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void terminalLoop() {
        welcome();
        while (running) {
            System.out.print("> ");
            String cmd = sc.nextLine().trim();
            ClientCommand.handle(cmd.trim().split("\\s+"), this);
        }
    }

    private void welcome() {
        System.out.println("\nCommands: list | quit");
    }

    public void stop() {
        this.running = false;
    }
}
