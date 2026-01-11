package de.haw.vs.termin3.client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Client client = new Client();

        try {
            client.register("Niko", "client");
            client.unregister("Niko");
            client.unregister("Niko");
            client.unregister("Niko");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}