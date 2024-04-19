import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class View {
    private JFrame gameFrame;
    private JFrame loginFrame;
    private DefaultListModel<String> listModel;

    public View(ActionListener loginAL, ActionListener registerAL, ActionListener passwordAL, ActionListener exitAL) {
        listModel = new DefaultListModel<String>();

        openLogin(loginAL,registerAL,passwordAL,exitAL);
    }

    /*
     * PARAMETERS: ActionListeners for Play, Logout buttons
     * RETURN TYPE: JPanel Game Tab
     * DESCRIPTION:
     * Builds the Game Tab in the game GUI
     */
    private JPanel makeGameTab(ActionListener playAL, ActionListener logoutAL) {
        JPanel game = new JPanel();

        JButton playButton = new JButton("Play");
        playButton.addActionListener(playAL);
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(logoutAL);

        game.add(playButton);
        game.add(logoutButton);

        return game;
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
    public void runGame(ActionListener playActionListner, ActionListener logoutActionListener, ActionListener refreshActionListener) {
        System.out.println("Initializing Game GUI...");

        gameFrame = new JFrame();

        JTabbedPane tabs = new JTabbedPane();

        tabs.add("GAME", makeGameTab(playActionListner,logoutActionListener));
        tabs.add("LEADERBOARD", makeLeaderboardTab(refreshActionListener));

        gameFrame.add(tabs);

        gameFrame.setSize(1000,600);
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
        loginFrame.setLayout(new GridLayout(4,2));

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(loginAL);
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(exitAL);
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(registerAL);
        JButton newPasswordButton = new JButton("Change Password");
        newPasswordButton.addActionListener(passwordAL);

        JTextField enterUsername = new JTextField(20);
        JTextField enterPassword = new JTextField(20);
        loginFrame.add(new JLabel("USERNAME:"));
        loginFrame.add(enterUsername);
        loginFrame.add(new JLabel("PASSWORD:"));
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
