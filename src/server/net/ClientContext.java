package server.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Random;

import client.net.messages.BalanceMessage;
import client.net.messages.LoginValidationMessage;
import client.net.messages.PasswordChangeValidation;
import client.net.messages.PayoutMessage;
import client.net.messages.RegisterValidationMessage;
import server.main.Model;
import server.net.messages.BalanceRequestMessage;
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
	private final DataOutputStream outputStream;
	private final Socket socket;
	private final Model model;
	private final Random rand = new Random();

	public ClientContext(InputStream inputStream, OutputStream outputStream, Socket socket, Model model) {
		this.inputStream = new DataInputStream(inputStream);
		this.outputStream = new DataOutputStream(outputStream);
		this.socket = socket;
		this.model = model;
	}

	public void run() {
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
					case MessageID.BALANCE            -> message = new BalanceRequestMessage();
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
	
	public void updateBalance(String username, double balanceNew) {
		model.updateUserBalance(username, Double.toString(balanceNew));
	}

	public void balance(String username) {
		try {
			BalanceMessage msg = new BalanceMessage();

			msg.balance = model.getBalance(username);

			msg.writeTo(outputStream);
			outputStream.flush();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Client disconnected unexpectedly!");
			e.printStackTrace();
		}
	}

	public void flipCoin(boolean predictedHeads, int bet) {
		double payout = 0.0;

		if (rand.nextBoolean() == predictedHeads) {
			payout = (bet * 0.5);
		}

		System.out.println("Payout : " + payout);

		try {
			PayoutMessage msg = new PayoutMessage();

			msg.payout = payout;

			msg.writeTo(outputStream);
			outputStream.flush();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void rollDie(int predictedRoll, int bet) {
		double payout = 0.0;
		int roll = 1 + rand.nextInt(6);

		if (roll == predictedRoll) {
			payout = bet * 2;
		}

		System.out.println("Payout: " + payout);

		try {
			PayoutMessage msg = new PayoutMessage();

			msg.payout = payout;

			msg.writeTo(outputStream);
			outputStream.flush();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void loginUser(String username, String password) {
		LoginValidationMessage msg = new LoginValidationMessage();

		if (model.checkLoginCredentials(username, password)) {
			System.out.println("Logged in user for client " + socket.toString());
			msg.isValid = true;
		} else {
			System.out.println("Invalid username or password for client " + socket.toString());
			msg.isValid = false;
		}

		try {
			msg.writeTo(outputStream);
			outputStream.flush();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void getLeaderboardScores() {
		client.net.messages.LeaderboardDataMessage msg = new client.net.messages.LeaderboardDataMessage();

		String[][] retArr = model.getLeaderboardScores();

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 2; j++) {
				msg.data[i * 2 + j] = retArr[i][j];
			}
		}

		try {
			msg.writeTo(outputStream);
			outputStream.flush();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void registerUser(String username, String password) {
		RegisterValidationMessage msg = new RegisterValidationMessage();

		if (model.doesUserExist(username)) {
			System.out.println("Username already taken for client " + socket.toString());
			msg.code = RegisterValidationMessage.ResponseType.ERROR_TAKEN.ordinal();
		} else if (username.length() < 8 || password.length() < 8) {
			System.out.println("username or password is too short!");
			msg.code = RegisterValidationMessage.ResponseType.ERROR_CREDENTIAL.ordinal();
		} else {
			System.out.println("Successfully registered user for client " + socket.toString());
			model.addUser(username, password);
			msg.code = RegisterValidationMessage.ResponseType.SUCCESS.ordinal();
		}

		try {
			msg.writeTo(outputStream);
			outputStream.flush();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void changeUserPassword(String username, String oldPassword, String newPassword) {
		PasswordChangeValidation msg = new PasswordChangeValidation();

		if ((model.checkLoginCredentials(username, oldPassword)) && newPassword.length() > 7) {
			model.updatePassword(username, newPassword);
			msg.success = true;
			System.out.println("Changed password for client " + socket.toString());
		} else {
			msg.success = false;
		}

		try {
			msg.writeTo(outputStream);
			outputStream.flush();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}