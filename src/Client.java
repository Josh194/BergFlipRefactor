import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        try {
            System.out.println("Connecting to 127.0.0.1...");
            Socket socket = new Socket("127.0.0.1", 6000);
            System.out.println("Connected!");

            InputStreamReader stream = new InputStreamReader(socket.getInputStream());
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String msgToServer = scanner.nextLine();
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                writer.println(msgToServer);
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Disconnected.");
    }
}
