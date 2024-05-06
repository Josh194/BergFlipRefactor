package client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

import client.net.messages.BalanceMessage;
import client.net.messages.LeaderboardDataMessage;
import client.net.messages.LoginValidationMessage;
import client.net.messages.PasswordChangeValidation;
import client.net.messages.PayoutMessage;
import client.net.messages.RegisterValidationMessage;
import server.Server;
import server.net.messages.*;
import shared.net.message.Message;
import shared.net.message.Message.InvalidFieldTypeException;
import shared.net.message.Message.Serialize;
import test.message.TestMessage;

public class ClientGUI {
	private final LoginView loginView;
	private final GameView gameView;
	private final PasswordView passwordView;
	private final closeErrorActionListener closeErrorAL;
	private final closeSuccessActionListener closeSuccessAL;
	private String[][] leaderboardData = new String[3][2];

	private Socket socket = null;
	private DataInputStream inputStream = null;
	private DataOutputStream outputStream = null;
	private Boolean validBet = false;
	private String username;
	private double balance;

	private final loginActionListener loginAL;
	private final registerActionListener registerAL;
	private final changePasswordActionListener passwordAL;
	private final exitActionListener exitAL;

	private final confirmPasswordActionListener confirmPassAL;
	private final cancelActionListener cancelAL;

	private final coinFlipActionListener flipCoinAL;
	private final rollDieActionListener rollDieAL;
	private final headsActionListener headsAL;
	private final tailsActionListener tailsAL;
	private final submitDicePredictionActionListener dicePredictionAL;
	private final submitCoinBetActionListener submitCoinBetAL;
	private final submitDiceBetActionListener submitDiceBetAL;
	private final logoutActionListener logoutAL;

	private final refreshActionListener refreshAL;

	public ClientGUI() {
		loginAL = new loginActionListener();
		registerAL = new registerActionListener();
		passwordAL = new changePasswordActionListener();
		exitAL = new exitActionListener();

		confirmPassAL = new confirmPasswordActionListener();
		cancelAL = new cancelActionListener();

		flipCoinAL = new coinFlipActionListener();
		rollDieAL = new rollDieActionListener();
		headsAL = new headsActionListener();
		tailsAL = new tailsActionListener();
		dicePredictionAL = new submitDicePredictionActionListener();
		submitCoinBetAL = new submitCoinBetActionListener();
		submitDiceBetAL = new submitDiceBetActionListener();
		logoutAL = new logoutActionListener();

		refreshAL = new refreshActionListener();

		closeErrorAL = new closeErrorActionListener();
		closeSuccessAL = new closeSuccessActionListener();
		loginView = new LoginView(loginAL,registerAL,passwordAL,exitAL);
		passwordView = new PasswordView();
		gameView = new GameView();

		try {
			System.out.println("Connecting to 127.0.0.1...");
			this.socket = new Socket("127.0.0.1", Server.SERVER_PORT);
			outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			System.out.println("Success!");

			try {
				LeaderboardMessage msg = new LeaderboardMessage();

				msg.writeTo(outputStream);
				outputStream.flush();
			} catch (InvalidFieldTypeException exception) {
				exception.printStackTrace();
			}

			LeaderboardDataMessage msg = new LeaderboardDataMessage();

			try {
				Message.readMessageType(inputStream);
				msg.readFrom(inputStream);
			} catch (Exception exception) {
				exception.printStackTrace();
			}

			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 2; j++) {
					leaderboardData[i][j] = msg.data[i * 2 + j];
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class loginActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			username = loginView.getEnterUsername().getText();
			String password = loginView.getEnterPassword().getText();
			System.out.println("Username: " + username + ". Password: " + password + ".");

			try {
				LoginMessage msg = new LoginMessage();

				msg.username = username;
				msg.password = password;

				msg.writeTo(outputStream);
				outputStream.flush();
			} catch (Exception exception) {
				exception.printStackTrace();
			}

			LoginValidationMessage msg = new LoginValidationMessage();

			try {
				Message.readMessageType(inputStream);
				msg.readFrom(inputStream);
			} catch (Exception exception) {
				exception.printStackTrace();
			}

			if (msg.isValid) {
				loginView.closeLogin();
				loadUserBalance(username);
				gameView.openGame(flipCoinAL,rollDieAL,logoutAL,headsAL,tailsAL,submitCoinBetAL,submitDiceBetAL,refreshAL,dicePredictionAL);
			} else {
				loginView.informGeneralLoginError(closeErrorAL);
			}

			gameView.fillLeaderboard(leaderboardData);
		}
	}

	private class registerActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			username = loginView.getEnterUsername().getText();
			String password = loginView.getEnterPassword().getText();
			System.out.println("Register button was pressed!");

			RegisterValidationMessage response = new RegisterValidationMessage();

			try {
				RegisterMessage request = new RegisterMessage();

				request.username = username;
				request.password = password;

				request.writeTo(outputStream);
				outputStream.flush();

				Message.readMessageType(inputStream);
				response.readFrom(inputStream);
			} catch (Exception exception) {
				exception.printStackTrace();
			}

			// TODO: does this need to handle ERROR_CREDENTIAL?
			if (response.code == RegisterValidationMessage.ResponseType.ERROR_TAKEN.ordinal()) {
				loginView.informUsernameAlreadyExists(closeErrorAL);
			} else if (response.code == RegisterValidationMessage.ResponseType.SUCCESS.ordinal()) {
				SuccessView.makeSuccessPopup(0,closeSuccessAL);
				System.out.println("Successfully registered user!");
			}
		}
	}

	private class changePasswordActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			loginView.closeLogin();
			passwordView.openChangePassword(confirmPassAL,cancelAL);
		}
	}

	private class closeErrorActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ErrorView.closeErrorPopup();
		}
	}

	private class closeSuccessActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			SuccessView.closeSuccessPopup();
		}
	}

	private class exitActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Client Closing Program...");
			loginView.closeLogin();

			try {
				ExitMessage msg = new ExitMessage();

				msg.writeTo(outputStream);
				outputStream.flush();
			} catch (Exception exception) {
				exception.printStackTrace();
			}

			System.exit(0);
		}
	}

	private class coinFlipActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int bet = Integer.parseInt(gameView.getCoinBettingAmount().getText());

			int prediction = gameView.getPredictedUserResult();

			if (prediction == -1) {
				gameView.informNoPrediction(closeErrorAL);
				return;
			}

			if (requestServerCoinFlip(prediction == 1, bet)) {
				PayoutMessage msg = new PayoutMessage();

				try {
					Message.readMessageType(inputStream);
					msg.readFrom(inputStream);
				} catch (Exception exception) {
					exception.printStackTrace();
				}

				if (msg.payout != 0.0) {
					updateUserBalance(msg.payout);
				} else {
					updateUserBalance((double) -bet);
				}

				System.out.println("Paid out $" + msg.payout);
				SuccessView.makeResultsPopup(closeSuccessAL, msg.payout, "COIN");
				gameView.updateFlipStatus();
			}
		}
	}

	private class rollDieActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int bet = Integer.parseInt(gameView.getDiceBettingAmount().getText());

			String prediction = gameView.getDicePrediction().getText();

			if (prediction == null) {
				gameView.informNoPrediction(closeErrorAL);
				return;
			}

			if (requestServerDieRoll(Integer.parseInt(prediction), bet)) {
				PayoutMessage msg = new PayoutMessage();

				try {
					Message.readMessageType(inputStream);
					msg.readFrom(inputStream);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				
				if (msg.payout != 0.0) {
					updateUserBalance(msg.payout);
				} else {
					updateUserBalance((double) -bet);
				}

				System.out.println("Paid out $" + msg.payout);
				SuccessView.makeResultsPopup(closeSuccessAL, msg.payout, "DICE");
				gameView.updateRollStatus();
			}
		}
	}

	private class logoutActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			gameView.closeGame();
			loginView.openLogin(loginAL,registerAL,passwordAL,exitAL);
		}
	}

	private class refreshActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				LeaderboardMessage msg = new LeaderboardMessage();

				msg.writeTo(outputStream);
				outputStream.flush();
			} catch (Exception exception) {
				exception.printStackTrace();
			}

			LeaderboardDataMessage msg = new LeaderboardDataMessage();

			try {
				Message.readMessageType(inputStream);
				msg.readFrom(inputStream);
			} catch (Exception exception) {
				exception.printStackTrace();
			}

			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 2; j++) {
					leaderboardData[i][j] = msg.data[i * 2 + j];
				}
			}

			gameView.fillLeaderboard(leaderboardData);
			System.out.println("Leaderboard refreshed.");
		}
	}

	private class headsActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			gameView.updatePredictedUserResult(true);
		}
	}

	private class tailsActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			gameView.updatePredictedUserResult(false);
		}
	}

	private class submitDicePredictionActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int userDicePrediction = Integer.parseInt(gameView.getDicePrediction().getText());

			if(userDicePrediction < 1 || userDicePrediction > 6) {
				ErrorView.makeErrorPopup(11,closeErrorAL);
			} else {
				gameView.updateDicePredictedResult(userDicePrediction);
			}
		}
	}

	private class submitCoinBetActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			double bet = Double.parseDouble(gameView.getCoinBettingAmount().getText());

			if(bet > 0 & bet <= GameView.getWallet()) {
				gameView.updateCoinBet();
				validBet = true;
			} else if (bet < 0) {
				gameView.informInvalidBetNonpositive(closeErrorAL);
				validBet = false;
			} else if(bet > balance) {
				gameView.informInvalidBetTooLarge(closeErrorAL);
				validBet = false;
			} else {
				gameView.informInvalidBetNotNumber(closeErrorAL);
				validBet = false;
			}
		}
	}

	private class submitDiceBetActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			double bet = Double.parseDouble(gameView.getDiceBettingAmount().getText());

			if(bet > 0 & bet <= GameView.getWallet()) {
				gameView.updateDiceBet();
				validBet = true;
			} else if (bet < 0) {
				gameView.informInvalidBetNonpositive(closeErrorAL);
				validBet = false;
			} else if(bet > balance) {
				gameView.informInvalidBetTooLarge(closeErrorAL);
				validBet = false;
			} else {
				gameView.informInvalidBetNotNumber(closeErrorAL);
				validBet = false;
			}
		}
	}

	private class confirmPasswordActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String username = passwordView.getPassUsername().getText();
			String oldPassword = passwordView.getOldPassword().getText();
			String newPassword = passwordView.getNewPassword().getText();

			PasswordChangeValidation response = new PasswordChangeValidation();

			try {
				ChangePasswordMessage request = new ChangePasswordMessage();

				request.username = username;
				request.passwordOld = oldPassword;
				request.passwordNew = newPassword;

				request.writeTo(outputStream);
				outputStream.flush();

				Message.readMessageType(inputStream);
				response.readFrom(inputStream);
			} catch (Exception exception) {
				exception.printStackTrace();
			}

			if (!response.success) {
				loginView.informGeneralLoginError(closeErrorAL);
			} else {
				passwordView.closeChangePassword();
				loginView.openLogin(loginAL,registerAL,passwordAL,exitAL);
				SuccessView.makeSuccessPopup(1,closeSuccessAL);
			}
		}
	}

	private class cancelActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			passwordView.closeChangePassword();
			loginView.openLogin(loginAL,registerAL,passwordAL,exitAL);
		}
	}

	private boolean requestServerCoinFlip(boolean predictedHeads, int bet) {
		if(!validBet) {
			return false;
		}

		double tempBet = bet;

		if (!(tempBet > 0 & tempBet <= GameView.getWallet()) || (tempBet < 1)) {
			System.out.println("Invalid bet!");

			return false;
		} else {
			try {
				FlipMessage msg = new FlipMessage();

				msg.predictedHeads = predictedHeads;
				msg.bet = bet;

				msg.writeTo(outputStream);
				outputStream.flush();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		return true;
	}

	private void loadUserBalance(String username) {
		BalanceMessage response = new BalanceMessage();

		try {
			BalanceRequestMessage request = new BalanceRequestMessage();

			request.username = username;

			request.writeTo(outputStream);
			outputStream.flush();

			Message.readMessageType(inputStream);
			response.readFrom(inputStream);
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		balance = response.balance;
		gameView.updateWallet(balance);
	}

	private void updateUserBalance(double newBalance) {
		try {
			UpdateMessage msg = new UpdateMessage();

			msg.username = username;
			msg.balanceNew = newBalance;

			msg.writeTo(outputStream);
			outputStream.flush();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		balance += newBalance;
		System.out.println("Balance from updateUserBalance: " + balance);
		gameView.updateWallet(balance);
	}

	private boolean requestServerDieRoll(int predictedRoll, int bet) {
		if(!validBet) {
			return false;
		}

		try {
			RollMessage msg = new RollMessage();

			msg.predictedRoll = predictedRoll;
			msg.bet = bet;

			msg.writeTo(outputStream);
			outputStream.flush();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return true;
	}
}