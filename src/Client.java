import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;

public class Client implements DataStore{
    private static final int DEFAULT_PORT = 3000;
    private static final String DEFAULT_HOST = "127.0.0.1";

    private final Socket clientSocket;

    private int nextId = 1;

    public Client(String host, int port) throws IOException {
        this.clientSocket = new Socket(host, port);
    }
    public Client(String host) throws IOException {
        this(host, DEFAULT_PORT);
    }
    public Client() throws IOException {
        this(DEFAULT_HOST, DEFAULT_PORT);
    }

    public void stop() throws IOException {
        this.clientSocket.close();
    }

    @Override
    public void write(int index, String data) {
        int id = nextId++;
        String request = "{\n" +
                "\"id\":" + id + ",\n" +
                "\"method\":\"write\",\n" +
                "\"index\":" + index + ",\n" +
                "\"data\":\"" + data + "\"\n" +
                "}\n";
        try {
            RPC.sendRequest(clientSocket, request);
            String response= RPC.awaitReply(clientSocket);
            var reader = JSONReader.fromString(response);
            if((boolean)reader.get("success"))
                System.out.println("Sucess!");
            else
                System.out.println("Failed!");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    @Override
    public String read(int index) throws NoSuchElementException {
        int id = nextId++;
        String request = "{\n" +
                "\"id\":" + id + ",\n" +
                "\"method\":\"read\",\n" +
                "\"index\":" + index + "\n" +
                "}\n";
        try {
            RPC.sendRequest(clientSocket, request);
            String response = RPC.awaitReply(clientSocket);
            var reader = JSONReader.fromString(response);
            if (!(boolean)reader.get("success"))
                if ("NoSuchElementException".equals(reader.get("exception.type")))
                    throw new NoSuchElementException((String) reader.get("exception.message"));
                else {
                    System.err.println("Unexpected exception received: " + reader.get("exception.type"));
                    System.err.println((String) reader.get("exception.message"));
                }
            return (String) reader.get("return");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
