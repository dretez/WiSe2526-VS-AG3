package de.haw.vs.termin3.client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Client client = new Client();

        try {
            client.regist("Niko", "client");
            client.regist("Niko", "client");
            client.unregist("Niko");

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}