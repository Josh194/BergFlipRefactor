import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket coinFlipServer = new ServerSocket(6000);
            String msg;

            while (true) {
                Socket socket = null;

                try {
                    socket = coinFlipServer.accept();
                    System.out.println("Connected to client: " + socket.toString());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(socket.getOutputStream());

                    System.out.println("Creating thread for client...");
                    Thread clientThread = new ClientTreadManager(reader, writer, socket);
                    clientThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                System.out.println("Waiting for client to connect...");
//                Socket client = coinFlipServer.accept();
//                System.out.println("Connected to client " + client.toString());
//
//                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
//                PrintWriter writer = new PrintWriter(client.getOutputStream());
//
//                System.out.println("Waiting for client to sent data...");
//                while ((msg = reader.readLine()) != null) {
//                    if (msg.equals("e")) {
//                        client.close();
//                        break;
//                    }
//                    System.out.println("Received: " + msg);
//                }
//                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}