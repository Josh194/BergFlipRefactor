package server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import server.main.Model;
import server.net.ClientThreadManager;

public class Server {
    public static final int SERVER_PORT = 7000;

    public static void main(String[] args) {
        try {
            ServerSocket coinFlipServer = new ServerSocket(SERVER_PORT);
            Model model = new Model();

            while (true) {
                try {
                    Socket socket = coinFlipServer.accept();
                    System.out.println("Connected to client: " + socket.toString());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(socket.getOutputStream());

                    System.out.println("Creating thread for client...");
                    Thread clientThread = new ClientThreadManager(reader, writer, socket, model);
                    clientThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}