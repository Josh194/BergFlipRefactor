package client.ui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;

public class ErrorView {
    private static JFrame errorFrame;
    private static JLabel errorMessage1;
    private static JLabel errorMessage2;

	public enum ErrorPair {
		USERNAME_EXISTS		("Username provided already exists.",				"This Username is already taken. Try a different username."),
		USERNAME_INVALID	("New Username is invalid.",						"Username must be at least 8 characters."),
		PASSWORD_INVALID	("New Password is invalid.",						"Password must be at least 8 characters."),
		USERNAME_NOT_FOUND	("Username provided does not exist.",				"Make sure you typed in the correct Username."),
		WRONG_PASSWORD		("Incorrect Password.",							"Make sure you typed in the Password correctly."),
		NEGATIVE_BET		("Amount to bet is invalid.",						"Your bet must be greater than 0."),
		BET_EXCESSIVE		("Amount to bet is invalid.",						"Your bet is greater than your current balance: "),
		BET_FORMAT			("Amount to bet is invalid.",						"Your bet must be a number. Please avoid using letters."),
		NO_PREDICTION		("No prediction was submitted.",					"Please submit a prediction before playing the game."),
		NO_BET				("No bet was submitted.",							"Please submit a bet before playing the game."),
		INVALID_CREDENTIALS	("Username or Password incorrect or invalid.",	"Make sure credentials are typed correctly."),
		INVALID_ROLL		("Prediction was invalid.",						"Choose a side 1 through 6 to predict.");

		private ErrorPair(String error, String hint) {
			this.error = error;
			this.hint = hint;
		}

		public final String error, hint;
	}

    public static void makeErrorPopup(ErrorPair error, ActionListener closeAL) {
		System.out.println("Creating Error Message Popup...");
		errorFrame = new JFrame("An Error Occurred");
		errorFrame.setLayout(new GridLayout(3,1));

		switch (error) {
		case BET_EXCESSIVE: {
			errorMessage1 = new JLabel(error.error);
			errorMessage2 = new JLabel(error.hint + GameView.getWallet() + ".");
			break;
		}
		default: {
			errorMessage1 = new JLabel(error.error);
			errorMessage2 = new JLabel(error.hint);
			break;
		}
		}

		errorMessage1.setHorizontalAlignment(JLabel.CENTER);
		errorMessage2.setHorizontalAlignment(JLabel.CENTER);

		JButton closeErrorButton = new JButton("Close");
		closeErrorButton.addActionListener(closeAL);

		errorFrame.add(errorMessage1);
		errorFrame.add(errorMessage2);
		errorFrame.add(closeErrorButton);

		errorFrame.setSize(400,150);
		errorFrame.setVisible(true);
    }

    public static void closeErrorPopup() {
		System.out.println("Closing Error Message Popup...");
		errorFrame.setVisible(false);
    }
}