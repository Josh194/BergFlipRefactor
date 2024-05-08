package client.ui;

import javax.swing.*;

import client.ui.ErrorView.ErrorPair;
import client.ui.style.Style.InvalidElementTypeException;
import client.ui.style.common.CommonStyle;

import java.awt.*;
import java.awt.event.ActionListener;

public class GameView {
	private JFrame gameFrame;
	private JTable leaderboardTable;
	private JTextField coinBettingAmount;
	private JTextField diceBettingAmount;
	private JTextField dicePrediction;

	private JLabel headsOrTails;
	private JLabel userCoinBet;
	private JLabel userDiceBet;
	private JLabel diceResult;
	private JLabel flipStatusLabel;
	private JLabel rollStatusLabel;
	private final JLabel playerBalCoin = new JLabel();
	private final JLabel playerBalDice = new JLabel();

	private int predictedUserHeads = -1; // -1: invalid, 0: false, 1: true
	private static double wallet = 0;

	public int getPredictedUserResult() {
		return predictedUserHeads;
	}

	public JTextField getDicePrediction() {
		return dicePrediction;
	}

	public JTextField getCoinBettingAmount() {
		return coinBettingAmount;
	}

	public JTextField getDiceBettingAmount() {
		return diceBettingAmount;
	}

	public static double getWallet() {
		return wallet;
	}

	static final String coinStrings[] = new String[] {"TAILS", "HEADS"}; // ? will this be constantly reinitialized if placed in `updatePredictedUserResult()` without the `static` modifier?

	public void updatePredictedUserResult(boolean heads) {
		predictedUserHeads = (heads ? 1 : 0);

		headsOrTails.setText("Predicted Result: " + coinStrings[predictedUserHeads]);
	}

	static final char diceStrings[] = new char[] {'1', '2', '3', '4', '5', '6'}; // not really necessary, but at least stops constant re-initialization

	public void updateDicePredictedResult(int side) {
		// only call-site guarantees `side` is in range, but may be worth considering checking more locally for increased safety
		diceResult.setText("Predicted Result: " + diceStrings[side - 1]);
	}

	public void updateCoinBet() {
		double bet = Double.parseDouble(coinBettingAmount.getText());
		userCoinBet.setText("Bet: $" + bet);
	}

	public void updateDiceBet() {
		double bet = Double.parseDouble(diceBettingAmount.getText());
		userDiceBet.setText("Bet: $" + bet);
	}

	public void updateWallet(double newBalance) {
		wallet = newBalance;
		playerBalCoin.setText("Your Balance: $" + wallet);
		playerBalDice.setText("Your Balance: $" + wallet);
	}

	public void updateFlipStatus() {
		flipStatusLabel.setText("The coin was flipped!");
	}

	public void updateRollStatus() {
		rollStatusLabel.setText("The die was rolled!");
	}

	public void informInvalidBetNonpositive(ActionListener closeAL) {
		try {
			ErrorView.makeErrorPopup(ErrorPair.NEGATIVE_BET, closeAL);
		} catch (InvalidElementTypeException e) {
			e.printStackTrace();
		}
	}

	public void informInvalidBetTooLarge(ActionListener closeAL) {
		try{
			ErrorView.makeErrorPopup(ErrorPair.BET_EXCESSIVE, closeAL);
		} catch (InvalidElementTypeException e) {
			e.printStackTrace();
		}
	}

	public void informInvalidBetNotNumber(ActionListener closeAL) {
		try {
			ErrorView.makeErrorPopup(ErrorPair.BET_FORMAT, closeAL);
		} catch (InvalidElementTypeException e) {
			e.printStackTrace();
		}
	}

	public void informNoPrediction(ActionListener closeAL) {
		try {
			ErrorView.makeErrorPopup(ErrorPair.NO_PREDICTION, closeAL);
		} catch (InvalidElementTypeException e) {
			e.printStackTrace();
		}
	}

	public void fillLeaderboard(String[][] leaderboardData) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 2; j++) {
				leaderboardTable.setValueAt(leaderboardData[i][j], i, j);
			}
		}
	}

	public void openGame(ActionListener coinFlipAL, ActionListener rollDieAL, ActionListener logoutAL, ActionListener headsAL, ActionListener tailsAL, ActionListener submitCoinBetAL, ActionListener submitDiceBetAL,ActionListener refreshAL, ActionListener dicePredictionAL) throws InvalidElementTypeException{
		System.out.println("Initializing Game GUI...");
		gameFrame = new JFrame("Gambling Game");
		JTabbedPane tabs = new JTabbedPane();

		tabs.add("COIN GAME", makeCoinGameTab(coinFlipAL,logoutAL,headsAL,tailsAL,submitCoinBetAL));
		tabs.add("DICE GAME", makeDiceGameTab(dicePredictionAL,submitDiceBetAL,rollDieAL,logoutAL));
		tabs.add("LEADERBOARD", makeLeaderboardTab(refreshAL));

		gameFrame.add(tabs);
		gameFrame.setSize(1000,750);
		gameFrame.setVisible(true);

		System.out.println("Game GUI Initialized Successfully!");
	}

	public void closeGame() {
		System.out.println("Closing Game GUI...");
		gameFrame.setVisible(false);
	}

	private JPanel makeCoinGameTab(ActionListener flipCoinAL, ActionListener logoutAL, ActionListener headsAL, ActionListener tailsAL, ActionListener submitCoinBetAL) throws InvalidElementTypeException {
		JPanel game = new JPanel();
		game.setLayout(new GridLayout(4,1,0,5));

		game.add(makeCoinGameTitle());
		game.add(makeCoinGameUserInput(headsAL, tailsAL, submitCoinBetAL));
		game.add(makeCoinGameStatus());
		game.add(makeCoinGameButtons(flipCoinAL, logoutAL));

		return game;
	}

	private JPanel makeDiceGameTab(ActionListener dicePredictionAL, ActionListener submitDiceBetAL, ActionListener rollDieAL, ActionListener logoutAL) throws InvalidElementTypeException {
		JPanel game = new JPanel();
		game.setLayout(new GridLayout(4,1,0,5));

		game.add(makeDiceGameTitle());
		game.add(makeDiceGameUserInput(dicePredictionAL, submitDiceBetAL));
		game.add(makeDiceGameStatus());
		game.add(makeDiceGameButtons(rollDieAL, logoutAL));

		return game;
	}

	private JPanel makeCoinGameTitle() throws InvalidElementTypeException {
		JPanel title = new JPanel();
		title.setLayout(new GridLayout(5,1));

		JLabel titleLabel = new JLabel("COIN FLIP GAME");
		CommonStyle.TITLE_LABEL.style.apply(titleLabel);
		title.add(titleLabel);

		JLabel instructions1 = new JLabel("First, choose whether you think the coin will land on Heads or Tails.");
		JLabel instructions2 = new JLabel("Then, place a bet on your prediction.");
		JLabel instructions3 = new JLabel("Finally, flip the coin and see if you can win big!");
		CommonStyle.DEFAULT_LABEL.style.apply(instructions1, instructions2, instructions3);
		title.add(instructions1);
		title.add(instructions2);
		title.add(instructions3);

		playerBalCoin.setText("Your Balance: $" + wallet);
		CommonStyle.CENTERED_LABEL.style.apply(playerBalCoin);
		title.add(playerBalCoin);

		return title;
	}

	private JPanel makeDiceGameTitle() throws InvalidElementTypeException {
		JPanel title = new JPanel();
		title.setLayout(new GridLayout(5,1));

		JLabel titleLabel = new JLabel("DICE GAME");
		CommonStyle.TITLE_LABEL.style.apply(titleLabel);
		title.add(titleLabel);

		JLabel instructions1 = new JLabel("First, choose what side you think the 6-sided die will land on.");
		JLabel instructions2 = new JLabel("Then, place a bet on your prediction.");
		JLabel instructions3 = new JLabel("Finally, roll the die and see if you can win big!");
		CommonStyle.DEFAULT_LABEL.style.apply(instructions1, instructions2, instructions3);
		title.add(instructions1);
		title.add(instructions2);
		title.add(instructions3);

		playerBalDice.setText("Your Balance: $" + wallet);
		CommonStyle.CENTERED_LABEL.style.apply(playerBalDice);
		title.add(playerBalDice);

		return title;
	}

	private JPanel makeCoinGameUserInput(ActionListener headsAL, ActionListener tailsAL, ActionListener submitCoinBetAL) {
		JPanel betting = new JPanel();
		betting.setLayout(new GridLayout(2,3,5,15));

		JLabel headsOrTails = new JLabel("Will you guess Heads or Tails?");
		headsOrTails.setHorizontalAlignment(JLabel.CENTER);
		JButton headsButton = new JButton("HEADS");
		headsButton.addActionListener(headsAL);
		JButton tailsButton = new JButton("TAILS");
		tailsButton.addActionListener(tailsAL);
		betting.add(headsOrTails);
		betting.add(headsButton);
		betting.add(tailsButton);

		JLabel betPrompt = new JLabel("How much money do you want to bet?");
		betPrompt.setHorizontalAlignment(JLabel.CENTER);
		coinBettingAmount = new JTextField();
		JButton submitBetButton = new JButton("Submit Bet");
		submitBetButton.addActionListener(submitCoinBetAL);
		betting.add(betPrompt);
		betting.add(coinBettingAmount);
		betting.add(submitBetButton);

		return betting;
	}

	private JPanel makeDiceGameUserInput(ActionListener dicePredictionAL, ActionListener submitDiceBetAL) {
		JPanel betting = new JPanel();
		betting.setLayout(new GridLayout(2,3,5,15));

		JLabel dicePrompt = new JLabel("Which side will the die land on?");
		dicePrompt.setHorizontalAlignment(JLabel.CENTER);
		dicePrediction = new JTextField();
		JButton submitDicePredictionButton = new JButton("Submit Prediction");
		submitDicePredictionButton.addActionListener(dicePredictionAL);
		betting.add(dicePrompt);
		betting.add(dicePrediction);
		betting.add(submitDicePredictionButton);

		JLabel betPrompt = new JLabel("How much money do you want to bet?");
		betPrompt.setHorizontalAlignment(JLabel.CENTER);
		diceBettingAmount = new JTextField();
		JButton submitBetButton = new JButton("Submit Bet");
		submitBetButton.addActionListener(submitDiceBetAL);
		betting.add(betPrompt);
		betting.add(diceBettingAmount);
		betting.add(submitBetButton);

		return betting;
	}

	private JPanel makeCoinGameStatus() throws InvalidElementTypeException {
		JPanel text = new JPanel();
		text.setLayout(new GridLayout(2,1));

		JPanel input = new JPanel();
		input.setLayout(new GridLayout(1,2,5,5));

		headsOrTails = new JLabel("Predicted Result: -");
		headsOrTails.setHorizontalAlignment(JLabel.CENTER);
		input.add(headsOrTails);

		userCoinBet = new JLabel("Bet: -");
		userCoinBet.setHorizontalAlignment(JLabel.CENTER);
		input.add(userCoinBet);
		text.add(input);

		flipStatusLabel = new JLabel("Awaiting coin flip...");
		CommonStyle.LARGE_LABEL.style.apply(flipStatusLabel);
		text.add(flipStatusLabel);

		return text;
	}

	private JPanel makeDiceGameStatus() throws InvalidElementTypeException  {
		JPanel text = new JPanel();
		text.setLayout(new GridLayout(2,1));

		JPanel input = new JPanel();
		input.setLayout(new GridLayout(1,2,5,5));

		diceResult = new JLabel("Predicted Result: -");
		diceResult.setHorizontalAlignment(JLabel.CENTER);
		input.add(diceResult);

		userDiceBet = new JLabel("Bet: -");
		userDiceBet.setHorizontalAlignment(JLabel.CENTER);
		input.add(userDiceBet);
		text.add(input);

		rollStatusLabel = new JLabel("Awaiting die roll...");
		CommonStyle.LARGE_LABEL.style.apply(rollStatusLabel);
		text.add(rollStatusLabel);

		return text;
	}

	private JPanel makeCoinGameButtons(ActionListener flipCoinAL, ActionListener logoutAL) {
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(2,1,10,10));

		JButton flipButton = new JButton("Flip Coin!");
		flipButton.addActionListener(flipCoinAL);
		buttons.add(flipButton);

		JButton logoutButton = new JButton("Logout");
		logoutButton.addActionListener(logoutAL);
		buttons.add(logoutButton);

		return buttons;
	}

	private JPanel makeDiceGameButtons(ActionListener rollDieAL, ActionListener logoutAL) {
		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(2,1,10,10));

		JButton flipButton = new JButton("Roll Die!");
		flipButton.addActionListener(rollDieAL);
		buttons.add(flipButton);

		JButton logoutButton = new JButton("Logout");
		logoutButton.addActionListener(logoutAL);
		buttons.add(logoutButton);

		return buttons;
	}

	private JPanel makeLeaderboardTab(ActionListener refreshAL) {
		JPanel leaderboard = new JPanel();
		leaderboard.setLayout(new BoxLayout(leaderboard, BoxLayout.Y_AXIS));

		JButton refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(refreshAL);

		String[] columnNames = {"Username", "Score"};
		Object[][] data = {{" ", " "}, {" ", " "}, {" ", " "}};
		leaderboardTable = new JTable(data, columnNames);

		leaderboard.add(refreshButton);
		leaderboard.add(leaderboardTable);

		return leaderboard;
	}
}