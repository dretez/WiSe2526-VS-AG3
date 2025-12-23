import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        long starttime, stoptime;
        String host = "169.254.159.150";

        // local call
        LocalDataStore lds = new LocalDataStore();
        starttime = System.nanoTime();
        lds.write(1, "Hallo Welt");
        stoptime = System.nanoTime();
        System.out.println("Local write call took " + ((stoptime - starttime) / 10e6) + " ms");
        try {
            starttime = System.nanoTime();
            String val = lds.read(1);
            stoptime = System.nanoTime();
            System.out.println(val);
            System.out.println("Local read call took " + ((stoptime - starttime) / 10e6) + " ms");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // remote call
        try {
            Client client = new Client(host);
            starttime = System.nanoTime();
            client.write(1,"Hallo Welt");
            stoptime = System.nanoTime();
            System.out.println("Remote write call took " + ((stoptime - starttime) / 10e6) + " ms");
            starttime = System.nanoTime();
            String val = client.read(1);
            stoptime = System.nanoTime();
            System.out.println(val);
            System.out.println("Remote read call took " + ((stoptime - starttime) / 10e6) + " ms");
            client.stop();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Command line
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type the index: ");
        int idx = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Type the data: ");
        String data = scanner.nextLine();
        try {
            Client client = new Client(host);
            client.write(idx,data);
            String val = client.read(idx);
            System.out.println(val);
            client.stop();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}