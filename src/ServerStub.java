import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;

public class ServerStub extends Thread {
    private final Socket clientSocket;
    private final ServerDataStore dataStore;

    public ServerStub(Socket socket, ServerDataStore dataStore) {
        this.clientSocket = socket;
        this.dataStore = dataStore;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String call = RPC.awaitReply(this.clientSocket);
                if (call == null) return;
                handleCall(JSONReader.fromString(call));
            }
        } catch (IOException _) {
        }
    }

    private void handleCall(JSONReader json) {
        switch ((String) json.get("method")) {
            case "write" -> replyToWriteCall(json);
            case "read" -> replyToReadCall(json);
            default -> throw new IllegalStateException("Unexpected method: " + json.get("method"));
        }
    }

    private void replyToReadCall(JSONReader json) {
        String value, reply;
        try {
            value = dataStore.read((Integer) json.get("index"));
            reply = "{\"success\":true,\"return\":\"" + value + "\"}";
        } catch (NoSuchElementException e) {
            reply = "{\"success\":false,\"exception\":\n"+
                        "{\"type\":\"NoSuchElementException\", \"message\":\""+e.getMessage()+"\"}"+
                    "}";
        }
        try {
            RPC.sendRequest(clientSocket, reply);
        } catch (IOException _) {
        }
    }

    private void replyToWriteCall(JSONReader json) {
        dataStore.write((Integer) json.get("index"), (String) json.get("data"));
        String reply;
        reply = "{\"success\":true}";
        try {
            RPC.sendRequest(clientSocket, reply);
        } catch (IOException _) {
        }
    }
}
