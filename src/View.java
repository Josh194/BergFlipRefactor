import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class View {
    //Login GUI Text Fields
    public JTextField enterUsername;
    public JTextField enterPassword;

    //Game GUI Text Fields
    public JTextField numOfFlips;
    public JTextField numOfResults;
    public JTextField bettingAmount;

    private JFrame gameFrame;
    private JFrame loginFrame;
    private DefaultListModel<String> listModel;

    public View(ActionListener loginAL, ActionListener registerAL, ActionListener passwordAL, ActionListener exitAL) {
        listModel = new DefaultListModel<String>();

        openLogin(loginAL,registerAL,passwordAL,exitAL);
    }

    /*
     * PARAMETERS: ActionListeners for Coin Flip, Logout, Flips, Prediction, Heads, Tails, Bet buttons
     * RETURN TYPE: JPanel Game Tab
     * DESCRIPTION:
     * Builds the Game Tab in the game GUI
     */
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

    /*
     * PARAMETERS: NONE
     * RETURN TYPE: JPanel Text Panel
     * DESCRIPTION:
     * Builds the Text Portion of the Game Tab
     */
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

    /*
     * PARAMETERS: ActionListeners for Flips, Heads, Tails, Prediction, Bet buttons
     * RETURN TYPE: JPanel Body Panel
     * DESCRIPTION:
     * Builds the body of the Game Tab
     */
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
        numOfResults = new JTextField();
        JButton submitPredictionButton = new JButton("Submit Prediction");
        submitPredictionButton.addActionListener(submitPredicAL);
        betting.add(resultsPrompt);
        betting.add(numOfResults);
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

    /*
     * PARAMETERS: ActionsListeners for Coin Flip, Logout buttons
     * RETURN TYPE: JPanel Bottom Panel
     * DESCRIPTION:
     * Builds the bottom portion of the Game Tab
     */
    private JPanel makeBottomGameTab(ActionListener flipAL, ActionListener logoutAL) {
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(2,1,10,10));

        JButton flipButton = new JButton("Flip Coin!");
        flipButton.addActionListener(flipAL);
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(logoutAL);

        buttons.add(flipButton);
        buttons.add(logoutButton);

        return buttons;
    }

    /*
     * PARAMETERS: ActionListener for Refresh button
     * RETURN TYPE: JPanel Leaderboard Tab
     * DESCRIPTION:
     * Builds the Leaderboard Tab in the game GUI
     */
    private JPanel makeLeaderboardTab(ActionListener refreshAL) {
        JPanel leaderboard = new JPanel();

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(refreshAL);
        JList<String> leaderboardList = new JList<String>(listModel);

        leaderboard.add(leaderboardList);
        leaderboard.add(refreshButton);

        return leaderboard;
    }

    /*
     * PARAMETERS: ActionListeners for Play, Logout, Refresh buttons
     * RETURN TYPE: VOID
     * DESCRIPTION:
     * Builds the game GUI
     */
    public void runGame(ActionListener flipAL, ActionListener logoutAL, ActionListener submitFlipsAL, ActionListener submitPredicAL,
                        ActionListener headsAL, ActionListener tailsAL, ActionListener submitBetAL, ActionListener refreshAL) {
        System.out.println("Initializing Game GUI...");
        gameFrame = new JFrame();
        JTabbedPane tabs = new JTabbedPane();

        tabs.add("GAME", makeGameTab(flipAL,logoutAL,submitFlipsAL,submitPredicAL,headsAL,tailsAL,submitBetAL));
        tabs.add("LEADERBOARD", makeLeaderboardTab(refreshAL));

        gameFrame.add(tabs);
        gameFrame.setSize(1100,850);
        gameFrame.setVisible(true);

        System.out.println("Game GUI Initialized Successfully!");
    }

    /*
     * PARAMETERS: NONE
     * RETURN TYPE: VOID
     * DESCRIPTION:
     * Closes the game GUI
     */
    public void closeGame() {
        System.out.println("Closing Game GUI...");
        gameFrame.setVisible(false);
    }

    /*
     * PARAMETERS: ActionListeners for Login, Register, Password, Exit buttons
     * RETURN TYPE: VOID
     * DESCRIPTION:
     * Builds the login GUI
     */
    public void openLogin(ActionListener loginAL, ActionListener registerAL, ActionListener passwordAL, ActionListener exitAL) {
        System.out.println("Initializing Login GUI...");
        loginFrame = new JFrame();
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

    /*
     * PARAMETERS: NONE
     * RETURN TYPE: VOID
     * DESCRIPTION:
     * Closes the login GUI
     */
    public void closeLogin() {
        System.out.println("Closing Login GUI...");
        loginFrame.setVisible(false);
    }

}
