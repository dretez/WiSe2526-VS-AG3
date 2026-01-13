package de.haw.vs.termin3.client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            Terminal.start("localhost", 8000);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}