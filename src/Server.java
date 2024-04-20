import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    void startServer() {
        try {
            ServerSocket coinFlipServer = new ServerSocket(5000);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}