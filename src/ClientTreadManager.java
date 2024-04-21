import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTreadManager extends Thread {
    final BufferedReader reader;
    final PrintWriter writer;
    final Socket socket;


    public ClientTreadManager(BufferedReader reader, PrintWriter writer, Socket socket) {
        this.reader = reader;
        this.writer = writer;
        this.socket = socket;
    }

     public void run() {
        String msg;

        System.out.println("Waiting for client to send data...");

        try {
            while ((msg = reader.readLine()) != null) {
                if (msg.equals("exit")) {
                    this.socket.close();
                    break;
                }
                System.out.println("Received: " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
     }
}
