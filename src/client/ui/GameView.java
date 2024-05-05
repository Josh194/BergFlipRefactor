package client.ui;
import javax.swing.*;

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

    private String predictedUserResult;
    private static double wallet = 0;

    public String getPredictedUserResult() {
        return predictedUserResult;
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
    public void updatePredictedUserResult(boolean heads) {
        if (heads) {
            predictedUserResult = "HEADS";
            headsOrTails.setText("Predicted Result: HEADS");
        } else {
            predictedUserResult = "TAILS";
            headsOrTails.setText("Predicted Result: TAILS");
        }
    }
    public void updateDicePredictedResult(int side) {
        switch (side) {
            case 1: diceResult.setText("Predicted Result: 1");
                    break;
            case 2: diceResult.setText("Predicted Result: 2");
                    break;
            case 3: diceResult.setText("Predicted Result: 3");
                    break;
            case 4: diceResult.setText("Predicted Result: 4");
                    break;
            case 5: diceResult.setText("Predicted Result: 5");
                    break;
            case 6: diceResult.setText("Predicted Result: 6");
                    break;
        }
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
        ErrorView.makeErrorPopup(5,closeAL);
    }
    public void informInvalidBetTooLarge(ActionListener closeAL) {
        ErrorView.makeErrorPopup(6,closeAL);
    }
    public void informInvalidBetNotNumber(ActionListener closeAL) {
        ErrorView.makeErrorPopup(7,closeAL);
    }
    public void informNoPrediction(ActionListener closeAL) {
        ErrorView.makeErrorPopup(8,closeAL);
    }
    public void fillLeaderboard (String[][] leaderboardData) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                leaderboardTable.setValueAt(leaderboardData[i][j], i, j);
            }
        }
    }
    public void openGame(ActionListener coinFlipAL, ActionListener rollDieAL, ActionListener logoutAL, ActionListener headsAL, ActionListener tailsAL,
                         ActionListener submitCoinBetAL, ActionListener submitDiceBetAL,ActionListener refreshAL, ActionListener dicePredictionAL) {
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
    private JPanel makeCoinGameTab(ActionListener flipCoinAL, ActionListener logoutAL, ActionListener headsAL,
                                   ActionListener tailsAL, ActionListener submitCoinBetAL) {
        JPanel game = new JPanel();
        game.setLayout(new GridLayout(4,1,0,5));

        JPanel title = makeCoinGameTitle();
        JPanel betting = makeCoinGameUserInput(headsAL, tailsAL, submitCoinBetAL);
        JPanel status = makeCoinGameStatus();
        JPanel buttons = makeCoinGameButtons(flipCoinAL, logoutAL);

        game.add(title);
        game.add(betting);
        game.add(status);
        game.add(buttons);

        return game;
    }
    private JPanel makeDiceGameTab(ActionListener dicePredictionAL, ActionListener submitDiceBetAL, ActionListener rollDieAL, ActionListener logoutAL) {
        JPanel game = new JPanel();
        game.setLayout(new GridLayout(4,1,0,5));

        JPanel title = makeDiceGameTitle();
        JPanel betting = makeDiceGameUserInput(dicePredictionAL, submitDiceBetAL);
        JPanel status = makeDiceGameStatus();
        JPanel buttons = makeDiceGameButtons(rollDieAL, logoutAL);

        game.add(title);
        game.add(betting);
        game.add(status);
        game.add(buttons);

        return game;
    }
    private JPanel makeCoinGameTitle() {
        JPanel title = new JPanel();
        title.setLayout(new GridLayout(5,1));

        JLabel titleLabel = new JLabel("COIN FLIP GAME");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial",Font.PLAIN,40));
        title.add(titleLabel);

        String instructText1 = "First, choose whether you think the coin will land on Heads or Tails.";
        String instructText2 = "Then, place a bet on your prediction.";
        String instructText3 = "Finally, flip the coin and see if you can win big!";
        JLabel instructions1 = new JLabel(instructText1);
        JLabel instructions2 = new JLabel(instructText2);
        JLabel instructions3 = new JLabel(instructText3);
        instructions1.setHorizontalAlignment(JLabel.CENTER);
        instructions2.setHorizontalAlignment(JLabel.CENTER);
        instructions3.setHorizontalAlignment(JLabel.CENTER);
        instructions1.setFont(new Font("Arial",Font.PLAIN,12));
        instructions2.setFont(new Font("Arial",Font.PLAIN,12));
        instructions3.setFont(new Font("Arial",Font.PLAIN,12));
        title.add(instructions1);
        title.add(instructions2);
        title.add(instructions3);

        playerBalCoin.setText("Your Balance: $" + wallet);
        playerBalCoin.setHorizontalAlignment(JLabel.CENTER);
        title.add(playerBalCoin);

        return title;
    }
    private JPanel makeDiceGameTitle() {
        JPanel title = new JPanel();
        title.setLayout(new GridLayout(5,1));

        JLabel titleLabel = new JLabel("DICE GAME");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial",Font.PLAIN,40));
        title.add(titleLabel);

        String instructText1 = "First, choose what side you think the 6-sided die will land on.";
        String instructText2 = "Then, place a bet on your prediction.";
        String instructText3 = "Finally, roll the die and see if you can win big!";
        JLabel instructions1 = new JLabel(instructText1);
        JLabel instructions2 = new JLabel(instructText2);
        JLabel instructions3 = new JLabel(instructText3);
        instructions1.setHorizontalAlignment(JLabel.CENTER);
        instructions2.setHorizontalAlignment(JLabel.CENTER);
        instructions3.setHorizontalAlignment(JLabel.CENTER);
        instructions1.setFont(new Font("Arial",Font.PLAIN,12));
        instructions2.setFont(new Font("Arial",Font.PLAIN,12));
        instructions3.setFont(new Font("Arial",Font.PLAIN,12));
        title.add(instructions1);
        title.add(instructions2);
        title.add(instructions3);

        playerBalDice.setText("Your Balance: $" + wallet);
        playerBalDice.setHorizontalAlignment(JLabel.CENTER);
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
    private JPanel makeCoinGameStatus() {
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
        flipStatusLabel.setHorizontalAlignment(JLabel.CENTER);
        flipStatusLabel.setFont(new Font("Arial",Font.PLAIN,20));
        text.add(flipStatusLabel);

        return text;
    }
    private JPanel makeDiceGameStatus() {
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
        rollStatusLabel.setHorizontalAlignment(JLabel.CENTER);
        rollStatusLabel.setFont(new Font("Arial",Font.PLAIN,20));
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