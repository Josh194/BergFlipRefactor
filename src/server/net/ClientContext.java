package server.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Random;

import server.main.Model;
import server.net.messages.BalanceMessage;
import server.net.messages.ChangePasswordMessage;
import server.net.messages.ExitMessage;
import server.net.messages.FlipMessage;
import server.net.messages.LeaderboardMessage;
import server.net.messages.LoginMessage;
import server.net.messages.RegisterMessage;
import server.net.messages.RollMessage;
import server.net.messages.UpdateMessage;
import shared.net.message.Message;

public class ClientContext extends Thread {
	private final DataInputStream inputStream;
	private final PrintWriter writer;
	private final Socket socket;
	private final Model model;
	private final Random rand = new Random();

	public ClientContext(InputStream inputStream, PrintWriter writer, Socket socket, Model model) {
		this.inputStream = new DataInputStream(inputStream);
		this.writer = writer;
		this.socket = socket;
		this.model = model;
	}

	public void run() {
		String msg;
		model.getLeaderboardScores();

		System.out.println("Waiting for client to send data...");

		// TODO: this now just excepts when the stream ends; should deal with this in a cleaner way
		outer:
		while (true) {
			ServerMessage message;

			try {
				switch (MessageID.ordinals[Message.readMessageType(inputStream)]) {
					case MessageID.EXIT               -> message = new ExitMessage(); // technically makes the call to `readFrom()` unnecessary (same for all other zero-length messages)
					case MessageID.LOGIN              -> message = new LoginMessage();
					case MessageID.REGISTER           -> message = new RegisterMessage();
					case MessageID.CHANGE_PASSWORD    -> message = new ChangePasswordMessage();
					case MessageID.FLIP               -> message = new FlipMessage();
					case MessageID.BALANCE            -> message = new BalanceMessage();
					case MessageID.UPDATE             -> message = new UpdateMessage();
					case MessageID.LEADERBOARD        -> message = new LeaderboardMessage();
					case MessageID.ROLL               -> message = new RollMessage();
					default -> { break outer; } // TODO: terminate the connection; this is logically fatal, regardless if we can still access the socket
				}

				message.readFrom(inputStream);
			} catch (IOException e) {
				System.out.println("Client disconnected unexpectedly!");
				e.printStackTrace();
				break;
			} catch (Message.InvalidFieldTypeException e) {
				e.printStackTrace();
				break;
			}
			
			switch (message.handle(this)) {
				case ServerMessage.Status.EXIT: { break outer; }
				case ServerMessage.Status.NORMAL:
				default: { break; }
			}
		}
	}
	
	public void updateBalance(String username, String balanceNew) {
		model.updateUserBalance(username, balanceNew);
	}

	public void balance(String username) {
		writer.println(model.getBalance(username));
		writer.flush();
	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Client disconnected unexpectedly!");
			e.printStackTrace();
		}
	}

	public void flipCoin(String predictedResult, String bet) {
		double payout = 0.0;

		if (rand.nextBoolean()) {
			if ((Objects.equals(predictedResult, "HEADS"))) {
				payout = (Integer.parseInt(bet) * 0.5);
			}
		} else {
			if ((Objects.equals(predictedResult, "TAILS"))) {
				payout = (Integer.parseInt(bet) * 0.5);
			}
		}

		System.out.println("Payout : " + payout);
		writer.println(payout);
		writer.flush();
	}

	public void rollDie(String predictedResult, String bet) {
		double payout = 0.0;
		int roll = 1 + rand.nextInt(6);

		if (roll == Integer.parseInt(predictedResult)) {
			payout = Integer.parseInt(bet) * 2;
		}

		System.out.println("Payout: " + payout);
		writer.println(payout);
		writer.flush();
	}

	public void loginUser(String username, String password) {
		if (model.checkLoginCredentials(username, password)) {
			System.out.println("Logged in user for client " + socket.toString());
			sendMessageToClient("valid user");
		} else {
			System.out.println("Invalid username or password for client " + socket.toString());
			sendMessageToClient("invalid user");
		}
	}

	public void getLeaderboardScores() {
		String[][] retArr = model.getLeaderboardScores();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 2; j++) {
				writer.println(retArr[i][j]);
			}
		}

		writer.flush();
	}

	public void registerUser(String username, String password) {
		if (model.doesUserExist(username)) {
			System.out.println("Username already taken for client " + socket.toString());
			sendMessageToClient("username already taken");
		} else if (username.length() < 8 || password.length() < 8) {
			System.out.println("username or password is too short!");
			sendMessageToClient("credentialError");
		} else {
			System.out.println("Successfully registered user for client " + socket.toString());
			model.addUser(username, password);
			sendMessageToClient("registered user");
		}
	}

	public void changeUserPassword(String username, String oldPassword, String newPassword) {
		if ((model.checkLoginCredentials(username, oldPassword)) && newPassword.length() > 7) {
			model.updatePassword(username, newPassword);
			sendMessageToClient("changed password");
			System.out.println("Changed password for client " + socket.toString());
		} else {
			sendMessageToClient("password error");
		}
	}

	private void sendMessageToClient(String msg) {
		writer.println(msg);
		writer.flush();
	}
}