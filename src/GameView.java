import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameView {
    private JTextField numOfFlips;
    private JTextField prediction;
    private JTextField bettingAmount;

    private JLabel userFlips;
    private JLabel headsOrTails;
    private JLabel userPrediction;
    private JLabel userBet;
    private JLabel flipStatusLabel;

    private boolean flipStatus = false;
    private int wallet = 0;
    private JFrame gameFrame;
    private DefaultListModel<String> listModel;

    public GameView() {
        listModel = new DefaultListModel<String>();
    }

    /*
     * TODO
     * Add functionality for dice betting
     *       -> Bubble to select coin, or coin and dice
     *       -> Coin and dice bubble edits GUI accordingly
     *              -> Adds another panel for dice bets
     *              -> Updates text in other panels
     * Add coin flip animation
     */

    public JTextField getNumOfFlips() {
        return numOfFlips;
    }

    public JTextField getPrediction() {
        return prediction;
    }

    public JTextField getBettingAmount() {
        return bettingAmount;
    }

    public void updateFlips() {
        int flips = Integer.parseInt(numOfFlips.getText());
        userFlips.setText("Times to Flip: " + flips);
    }

    public void updateHeadsOrTails(boolean heads) {
        if(heads) {
            headsOrTails.setText("Predicted Result: HEADS");
        } else {
            headsOrTails.setText("Predicted Result: TAILS");
        }
    }

    public void updatePrediction() {
        int predict = Integer.parseInt(prediction.getText());
        userPrediction.setText("Predicted Number of Times Correct: " + predict);
    }

    public void updateBet() {
        int bet = Integer.parseInt(bettingAmount.getText());
        userBet.setText("Bet: $" + bet);
    }

    public void updateFlipStatus() {
        flipStatusLabel.setText("The coin was flipped!");
        flipStatus = true;
    }

    public void openGame(ActionListener flipAL, ActionListener logoutAL, ActionListener submitFlipsAL,
                         ActionListener submitPredicAL, ActionListener headsAL, ActionListener tailsAL,
                         ActionListener submitBetAL,ActionListener refreshAL) {
        //wallet = balance;
        System.out.println("Initializing Game GUI...");
        gameFrame = new JFrame("Coin Flip Game");
        JTabbedPane tabs = new JTabbedPane();

        tabs.add("GAME", makeGameTab(flipAL,logoutAL,submitFlipsAL,submitPredicAL,headsAL,tailsAL,submitBetAL));
        tabs.add("LEADERBOARD", makeLeaderboardTab(refreshAL));

        gameFrame.add(tabs);
        gameFrame.setSize(1100,850);
        gameFrame.setVisible(true);
        System.out.println("Game GUI Initialized Successfully!");
    }

    public void closeGame() {
        System.out.println("Closing Game GUI...");
        gameFrame.setVisible(false);
    }

    public void informInvalidFlips(ActionListener closeAL) {
        numOfFlips.setBorder(BorderFactory.createLineBorder(Color.red));
        ErrorView.makeErrorPopup(4,closeAL);
    }

    public void informInvalidPrediction(ActionListener closeAL) {
        prediction.setBorder(BorderFactory.createLineBorder(Color.red));
        ErrorView.makeErrorPopup(5,closeAL);
    }

    public void informInvalidBet(ActionListener closeAL) {
        bettingAmount.setBorder(BorderFactory.createLineBorder(Color.red));
        ErrorView.makeErrorPopup(6,closeAL);
    }

    private JPanel makeGameTab(ActionListener flipAL, ActionListener logoutAL, ActionListener submitFlipsAL, ActionListener submitPredicAL,
                               ActionListener headsAL, ActionListener tailsAL, ActionListener submitBetAL) {
        JPanel game = new JPanel();
        game.setLayout(new GridLayout(5,1,0,5));

        JPanel title = makeGameTitle();
        JPanel gamemodes = makeGameModeSelect();
        JPanel betting = makeGameUserInput(submitFlipsAL, headsAL, tailsAL, submitPredicAL, submitBetAL);
        JPanel input = makeGameStatusText();
        JPanel buttons = makeGamePrimaryButtons(flipAL, logoutAL);

        //Compiling panels into Game Panel
        game.add(title);
        game.add(gamemodes);
        game.add(betting);
        game.add(input);
        game.add(buttons);

        return game;
    }

    private JPanel makeGameTitle() {
        JPanel title = new JPanel();
        title.setLayout(new GridLayout(5,1));

        JLabel titleLabel = new JLabel("COIN FLIP GAME");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial",Font.PLAIN,40));
        title.add(titleLabel);

        String instructText1 = "Choose how many times you want the coin to flip. Then, decide whether you want to guess heads or tails.";
        String instructText2 = "Then, make a guess on how many times the coin lands on the side you predicted.";
        String instructText3 = "Finally, submit an amount on how much money you'd like to bet that your prediction is correct.";
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

        JLabel playerBal = new JLabel("Your Balance: $" + wallet);
        playerBal.setHorizontalAlignment(JLabel.CENTER);
        title.add(playerBal);

        return title;
    }

    private JPanel makeGameModeSelect() {
        JPanel gamemode = new JPanel();
        gamemode.setLayout(new GridLayout(1,3));

        JRadioButton singleCoinButton = new JRadioButton("Single Coin",true);
        singleCoinButton.setHorizontalAlignment(JRadioButton.CENTER);
        gamemode.add(singleCoinButton);

        JRadioButton multipleCoinsButton = new JRadioButton("Multiple Coins");
        multipleCoinsButton.setHorizontalAlignment(JRadioButton.CENTER);
        gamemode.add(multipleCoinsButton);

        JRadioButton coinAndDie = new JRadioButton("Coin & Dice");
        coinAndDie.setHorizontalAlignment(JRadioButton.CENTER);
        gamemode.add(coinAndDie);

        return gamemode;
    }

    private JPanel makeGameUserInput(ActionListener submitFlipsAL, ActionListener headsAL, ActionListener tailsAL,
                                     ActionListener submitPredicAL, ActionListener submitBetAL) {
        JPanel betting = new JPanel();
        betting.setLayout(new GridLayout(4,3,5,15));

        JLabel flipsPrompt = new JLabel("How many times should the coin be flipped?");
        flipsPrompt.setHorizontalAlignment(JLabel.CENTER);
        numOfFlips = new JTextField();
        JButton submitFlipsButton = new JButton("Submit Flips");
        submitFlipsButton.addActionListener(submitFlipsAL);
        betting.add(flipsPrompt);
        betting.add(numOfFlips);
        betting.add(submitFlipsButton);

        JLabel headsOrTails = new JLabel("Will you guess Heads or Tails?");
        headsOrTails.setHorizontalAlignment(JLabel.CENTER);
        JButton headsButton = new JButton("HEADS");
        headsButton.addActionListener(headsAL);
        JButton tailsButton = new JButton("TAILS");
        tailsButton.addActionListener(tailsAL);
        betting.add(headsOrTails);
        betting.add(headsButton);
        betting.add(tailsButton);

        JLabel resultsPrompt = new JLabel("How many coins will land on the side you predicted?");
        resultsPrompt.setHorizontalAlignment(JLabel.CENTER);
        prediction = new JTextField();
        JButton submitPredictionButton = new JButton("Submit Prediction");
        submitPredictionButton.addActionListener(submitPredicAL);
        betting.add(resultsPrompt);
        betting.add(prediction);
        betting.add(submitPredictionButton);

        JLabel betPrompt = new JLabel("How much money do you want to bet?");
        betPrompt.setHorizontalAlignment(JLabel.CENTER);
        bettingAmount = new JTextField();
        JButton submitBetButton = new JButton("Submit Bet");
        submitBetButton.addActionListener(submitBetAL);
        betting.add(betPrompt);
        betting.add(bettingAmount);
        betting.add(submitBetButton);

        return betting;
    }

    private JPanel makeUserInputText() {
        JPanel input = new JPanel();
        input.setLayout(new GridLayout(2,2,5,5));

        userFlips = new JLabel("Times to Flip: -");
        userFlips.setHorizontalAlignment(JLabel.CENTER);
        input.add(userFlips);

        headsOrTails = new JLabel("Predicted Result: -");
        headsOrTails.setHorizontalAlignment(JLabel.CENTER);
        input.add(headsOrTails);

        userPrediction = new JLabel("Predicted Number of Times Correct: -");
        userPrediction.setHorizontalAlignment(JLabel.CENTER);
        input.add(userPrediction);

        userBet = new JLabel("Bet: -");
        userBet.setHorizontalAlignment(JLabel.CENTER);
        input.add(userBet);

        return input;
    }

    private JPanel makeGameStatusText() {
        JPanel text = new JPanel();
        text.setLayout(new GridLayout(2,1));

        JPanel input = makeUserInputText();
        text.add(input);

        flipStatusLabel = new JLabel("Awaiting coin flip...");
        flipStatusLabel.setHorizontalAlignment(JLabel.CENTER);
        flipStatusLabel.setFont(new Font("Arial",Font.PLAIN,20));
        text.add(flipStatusLabel);

        return text;
    }

    private JPanel makeGamePrimaryButtons(ActionListener flipAL, ActionListener logoutAL) {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(2,1,10,10));

        JButton flipButton = new JButton("Flip Coin!");
        flipButton.addActionListener(flipAL);
        buttons.add(flipButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(logoutAL);
        buttons.add(logoutButton);

        return buttons;
    }

    private JPanel makeLeaderboardTab(ActionListener refreshAL) {
        JPanel leaderboard = new JPanel();

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(refreshAL);
        JList<String> leaderboardList = new JList<String>(listModel);

        leaderboard.add(leaderboardList);
        leaderboard.add(refreshButton);

        return leaderboard;
    }

}
