package client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SuccessView {
	private static JFrame successFrame;
	private static JLabel successMessage1;
	private static JLabel successMessage2;

	public static void makeSuccessPopup(int successCase, ActionListener closeAL) {
		System.out.println("Creating success message popup...");
		successFrame = new JFrame("Success!");
		successFrame.setLayout(new GridLayout(3,1));

		switch (successCase) {
			case 0: successMessage1 = new JLabel("Account registered successfully!");
					successMessage2 = new JLabel("You can now login with your new account.");
					break;
			case 1: successMessage1 = new JLabel("Changed password successfully!");
					successMessage2 = new JLabel("You can now login using your new password.");
					break;
			case 2: successMessage1 = new JLabel("You won!");
					successMessage2 = new JLabel("Your bet was paid out!");
					break;
			case 3: successMessage1 = new JLabel("You lost!");
					successMessage2 = new JLabel("Luck was not on your side and you lost your bet.");
					break;
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

	public static void makeResultsPopup(ActionListener closeAL, double payout, String mode) {
		System.out.println("Creating success message popup...");
		successFrame = new JFrame("Success!");
		successFrame.setLayout(new GridLayout(3,1));

		if(payout == 0) {
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