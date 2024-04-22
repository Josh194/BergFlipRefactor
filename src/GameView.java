import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameView {
    private JTextField numOfFlips;
    private JTextField prediction;
    private JTextField bettingAmount;

    private JLabel flipStatusLabel;
    private JFrame gameFrame;
    private DefaultListModel<String> listModel;

    public GameView() {
        listModel = new DefaultListModel<String>();
    }

    /*
     * TODO
     * Create Getter functions
     * Create Setter functions
     * Reorganize functions -> public first, private second
     * Add functionality for dice betting
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

    public void openGame(ActionListener flipAL, ActionListener logoutAL, ActionListener submitFlipsAL, ActionListener submitPredicAL,
                         ActionListener headsAL, ActionListener tailsAL, ActionListener submitBetAL, ActionListener refreshAL) {
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

    public void updateFlipStatus() {
        flipStatusLabel.setText("The coin was flipped!");
    }

    //Compiles separate panels into one Game Tab
    private JPanel makeGameTab(ActionListener flipAL, ActionListener logoutAL, ActionListener submitFlipsAL, ActionListener submitPredicAL,
                               ActionListener headsAL, ActionListener tailsAL, ActionListener submitBetAL) {
        JPanel game = new JPanel();
        game.setLayout(new GridLayout(3,1,0,30));

        JPanel title = makeTopGameTab();
        JPanel betting = makeMiddleGameTab(submitFlipsAL, headsAL, tailsAL, submitPredicAL, submitBetAL);
        JPanel buttons = makeBottomGameTab(flipAL, logoutAL);

        //Compiling panels into Game Panel
        game.add(title);
        game.add(betting);
        game.add(buttons);

        return game;
    }

    private JPanel makeTopGameTab() {
        JPanel title = new JPanel();
        title.setLayout(new GridLayout(4,1,0,10));

        JLabel titleLabel = new JLabel("THE GAME");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial",Font.PLAIN,50));
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
        instructions1.setFont(new Font("Arial",Font.PLAIN,15));
        instructions2.setFont(new Font("Arial",Font.PLAIN,15));
        instructions3.setFont(new Font("Arial",Font.PLAIN,15));
        title.add(instructions1);
        title.add(instructions2);
        title.add(instructions3);

        return title;
    }

    private JPanel makeMiddleGameTab(ActionListener submitFlipsAL, ActionListener headsAL, ActionListener tailsAL,
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

    private JPanel makeBottomGameTab(ActionListener flipAL, ActionListener logoutAL) {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(3,1));

        JButton flipButton = new JButton("Flip Coin!");
        flipButton.addActionListener(flipAL);
        buttons.add(flipButton);

        flipStatusLabel = new JLabel("Awaiting coin flip...");
        flipStatusLabel.setHorizontalAlignment(JLabel.CENTER);
        flipStatusLabel.setFont(new Font("Arial",Font.PLAIN,20));
        buttons.add(flipStatusLabel);

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
