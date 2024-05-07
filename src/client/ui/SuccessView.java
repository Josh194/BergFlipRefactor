package client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SuccessView {
	private static JFrame successFrame;
	private static JLabel successMessage1;
	private static JLabel successMessage2;

	public enum SuccessPair {
		ACCOUNT_REGISTERED	("Account registered successfully!",	"You can now login with your new account."),
		PASSWORD_CHANGED	("Changed password successfully!",		"You can now login using your new password."),
		BET_WIN				("You won!",							"Your bet was paid out!"),
		BET_LOSE			("You lost!",							"Luck was not on your side and you lost your bet.");

		private SuccessPair(String message, String description) {
			this.message = message;
			this.description = description;
		}

		public final String message, description;
	}

	public static void makeSuccessPopup(SuccessPair successCase, ActionListener closeAL) {
		System.out.println("Creating success message popup...");
		successFrame = new JFrame("Success!");
		successFrame.setLayout(new GridLayout(3,1));

		successMessage1 = new JLabel(successCase.message);
		successMessage2 = new JLabel(successCase.description);

		successMessage1.setHorizontalAlignment(JLabel.CENTER);
		successMessage2.setHorizontalAlignment(JLabel.CENTER);

		JButton closeSuccessButton = new JButton("Close");
		closeSuccessButton.addActionListener(closeAL);

		successFrame.add(successMessage1);
		successFrame.add(successMessage2);
		successFrame.add(closeSuccessButton);

		successFrame.setSize(400,150);
		successFrame.setVisible(true);
	}

	public static void makeResultsPopup(ActionListener closeAL, double payout, String mode) {
		System.out.println("Creating success message popup...");
		successFrame = new JFrame("Success!");
		successFrame.setLayout(new GridLayout(3,1));

		// TODO: maybe cleanup
		if (payout == 0) {
			successMessage1 = new JLabel("You lost!");
			successMessage2 = new JLabel("Luck was not on your side and you lost your bet.");
		} else if(mode.equals("DICE")) {
			successMessage1 = new JLabel("You won!");
			successMessage2 = new JLabel("You got 200% of your bet back!");
		} else {
			successMessage1 = new JLabel("You won!");
			successMessage2 = new JLabel("You got 150% of your bet back!");
		}

		successMessage1.setHorizontalAlignment(JLabel.CENTER);
		successMessage2.setHorizontalAlignment(JLabel.CENTER);

		JButton closeSuccessButton = new JButton("Close");
		closeSuccessButton.addActionListener(closeAL);

		successFrame.add(successMessage1);
		successFrame.add(successMessage2);
		successFrame.add(closeSuccessButton);

		successFrame.setSize(400,150);
		successFrame.setVisible(true);
	}

	public static void closeSuccessPopup() {
		System.out.println("Closing Success Message Popup...");
		successFrame.setVisible(false);
	}
}