import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    public LoginView loginView;
    public GameView gameView;
    public PasswordView passwordView;
    public Model model;

    //Login GUI ActionListeners
    private final loginActionListener loginAL;
    private final registerActionListener registerAL;
    private final changePasswordActionListener passwordAL;
    private final exitActionListener exitAL;

    //Change Password GUI ActionListeners
    private final confirmPasswordActionListener confirmPassAL;
    private final cancelActionListener cancelAL;

    //Game GUI ActionListeners
    private final flipActionListener flipAL;
    private final logoutActionListener logoutAL;
    private final submitFlipsActionListener submitFlipsAL;
    private final submitPredictionActionListener submitPredicAL;
    private final headsActionListener headsAL;
    private final tailsActionListener tailsAL;
    private final submitBetActionListener submitBetAL;

    //Leaderboard GUI ActionListeners
    private final refreshActionListener refreshAL;

    public Controller() {
        //Login GUI ActionListeners
        loginAL = new loginActionListener();
        registerAL = new registerActionListener();
        passwordAL = new changePasswordActionListener();
        exitAL = new exitActionListener();

        //Change Password GUI ActionListener
        confirmPassAL = new confirmPasswordActionListener();
        cancelAL = new cancelActionListener();

        //Game GUI ActionListeners
        flipAL = new flipActionListener();
        logoutAL = new logoutActionListener();
        submitFlipsAL = new submitFlipsActionListener();
        submitPredicAL = new submitPredictionActionListener();
        headsAL = new headsActionListener();
        tailsAL = new tailsActionListener();
        submitBetAL = new submitBetActionListener();

        //Leaderboard GUI ActionListeners
        refreshAL = new refreshActionListener();

        loginView = new LoginView(loginAL,registerAL,passwordAL,exitAL);
        passwordView = new PasswordView();
        gameView = new GameView();
        model = new Model();
    }

    private class loginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.enterUsername.getText();
            String password = loginView.enterPassword.getText();
            System.out.println("Login button was pressed!");
            System.out.println("Username: " + username + ". Password: " + password + ".");
            loginView.closeLogin();
            loginView.openGame(flipAL,logoutAL,submitFlipsAL,submitPredicAL,headsAL,tailsAL,submitBetAL,refreshAL);
        }
    }

    private class registerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.enterUsername.getText();
            String password = loginView.enterPassword.getText();
            System.out.println("Register button was pressed!");
            if (model.doesUserExist(username) && checkUserValidity(username, password)) {
                model.addUser(username, password);
            }
            System.out.println("Username: " + username + ". Password: " + password + ".");
        }
    }

    private class changePasswordActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Change Password button was pressed!");
            loginView.openChangePassword(confirmPassAL,cancelAL);
        }
    }

    private class exitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Exit button was pressed!");
            loginView.closeLogin();
            System.out.println("Closing Program...");
            System.exit(0);
        }
    }

    private class flipActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Coin Flip button was pressed!");
            loginView.updateFlipStatus();
        }
    }

    private class logoutActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Logout button was pressed!");
            loginView.closeGame();
            loginView.openLogin(loginAL,registerAL,passwordAL,exitAL);
        }
    }

    private class refreshActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Refresh button was pressed!");
        }
    }

    private class submitFlipsActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int totalFlips = Integer.parseInt(loginView.numOfFlips.getText());
            System.out.println("Coin will flip " + totalFlips + " times!");
        }
    }

    private class headsActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Heads button was pressed!");
        }
    }

    private class tailsActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Tails button was pressed!");
        }
    }

    private class submitPredictionActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int prediction = Integer.parseInt(loginView.numOfResults.getText());
            System.out.println("Your prediction is that " + prediction + " coins will land correctly!");
        }
    }

    private class submitBetActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int bet = Integer.parseInt(loginView.bettingAmount.getText());
            if(bet > 0) {
                System.out.println("Your bet is $" + bet + "!");
            }
            else {
                System.out.println("That is an invalid bet! You have to bet at least $1.");
            }
        }
    }

    private class confirmPasswordActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.passUsername.getText();
            String oldPassword = loginView.oldPassword.getText();
            String newPassword = loginView.newPassword.getText();
            System.out.println("Changed password " + oldPassword + " for user " + username + " to be " + newPassword + ".");
            loginView.closeChangePassword();
        }
    }

    private class cancelActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Cancel button was pressed!");
            loginView.closeChangePassword();
        }
    }

    private boolean checkUserValidity(String username, String password) {
        if (username.isEmpty()) {
            System.out.println("Username is empty!");
        }
        if (password.isEmpty()) {
            System.out.println("Password is empty!");
        }
        if (password.length() > 8) {
            System.out.println("Password must be 8 characters or longer!");
        }
        return false;
    }
}
