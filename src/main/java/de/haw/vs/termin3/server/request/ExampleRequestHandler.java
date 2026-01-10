package de.haw.vs.termin3.server.request;

import de.haw.vs.termin3.common.json.JSONReader;

import java.net.Socket;

public final class ExampleRequestHandler extends RequestHandler {
    @Override
    protected void handle(JSONReader reader, Socket client) {
        System.out.println("Received a request");
    }
}
