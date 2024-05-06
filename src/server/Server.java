package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import server.main.Model;
import server.net.ClientContext;

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

					BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
					BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream());

					System.out.println("Creating thread for client...");
					Thread clientThread = new ClientContext(inputStream, outputStream, socket, model);
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