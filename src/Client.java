import java.io.IOException;
import java.net.Socket;

public class Client {
    void connectToServer(int port) {
        try {
            System.out.println("Connecting to 127.0.0.1...");
            Socket socket = new Socket("127.0.0.1", port);
            System.out.println("Success!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
