package de.haw.vs.termin3.client;

import de.haw.vs.termin3.common.network.Port;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            Terminal.start("127.0.0.1", Port.DEFAULT.port());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}