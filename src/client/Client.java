package client;

import client.ui.ClientGUI;
import client.ui.style.Style.InvalidElementTypeException;

public class Client {
	public static void main(String[] args) {
		System.out.println("Launching the Program...");

		try {
			new ClientGUI();
		} catch (InvalidElementTypeException exception) {
			exception.printStackTrace();
		}
	}
}