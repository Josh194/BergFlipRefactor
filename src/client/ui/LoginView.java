package client.ui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView {
	private JTextField enterUsername;
	private JTextField enterPassword;
	private JFrame loginFrame;

	public LoginView(ActionListener loginAL, ActionListener registerAL, ActionListener passwordAL, ActionListener exitAL) {
		openLogin(loginAL,registerAL,passwordAL,exitAL);
	}

	public JTextField getEnterUsername() {
		return enterUsername;
	}

	public JTextField getEnterPassword() {
		return enterPassword;
	}

	public void openLogin(ActionListener loginAL, ActionListener registerAL, ActionListener passwordAL, ActionListener exitAL) {
		System.out.println("Initializing Login GUI...");
		loginFrame = new JFrame("Login or Register");
		loginFrame.setLayout(new GridLayout(4,2,8,8));

		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(loginAL);
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(exitAL);
		JButton registerButton = new JButton("Register");
		registerButton.addActionListener(registerAL);
		JButton newPasswordButton = new JButton("Change Password");
		newPasswordButton.addActionListener(passwordAL);

		enterUsername = new JTextField(20);
		enterPassword = new JTextField(20);
		JLabel usernameLabel = new JLabel("USERNAME:");
		usernameLabel.setHorizontalAlignment(JLabel.CENTER);
		JLabel passwordLabel = new JLabel("PASSWORD:");
		passwordLabel.setHorizontalAlignment(JLabel.CENTER);

		loginFrame.add(usernameLabel);
		loginFrame.add(enterUsername);
		loginFrame.add(passwordLabel);
		loginFrame.add(enterPassword);

		loginFrame.add(loginButton);
		loginFrame.add(registerButton);
		loginFrame.add(newPasswordButton);
		loginFrame.add(exitButton);

		loginFrame.setSize(500,300);
		loginFrame.setVisible(true);
		System.out.println("Login GUI Initialized!");
	}
	
	public void closeLogin() {
		System.out.println("Closing Login GUI...");
		loginFrame.setVisible(false);
	}

	public void informUsernameAlreadyExists(ActionListener closeAL) {
		enterUsername.setBorder(BorderFactory.createLineBorder(Color.red));
		ErrorView.makeErrorPopup(0,closeAL);
	}

	public void informGeneralLoginError(ActionListener closeAL) {
		ErrorView.makeErrorPopup(10,closeAL);
	}
}