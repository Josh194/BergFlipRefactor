import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    public View view;
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

        view = new View(loginAL,registerAL,passwordAL,exitAL);
        model = new Model();
    }

    private class loginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.enterUsername.getText();
            String password = view.enterPassword.getText();
            System.out.println("Login button was pressed!");
            System.out.println("Username: " + username + ". Password: " + password + ".");
            view.closeLogin();
            view.openGame(flipAL,logoutAL,submitFlipsAL,submitPredicAL,headsAL,tailsAL,submitBetAL,refreshAL);
        }
    }

    private class registerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = view.enterUsername.getText();
            String password = view.enterPassword.getText();
            System.out.println("Register button was pressed!");
            System.out.println("Username: " + username + ". Password: " + password + ".");
        }
    }

    private class changePasswordActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Change Password button was pressed!");
            view.openChangePassword(confirmPassAL,cancelAL);
        }
    }

    private class exitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Exit button was pressed!");
            view.closeLogin();
            System.out.println("Closing Program...");
            System.exit(0);
        }
    }

    private class flipActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Coin Flip button was pressed!");
            view.updateFlipStatus();
        }
    }

    private class logoutActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Logout button was pressed!");
            view.closeGame();
            view.openLogin(loginAL,registerAL,passwordAL,exitAL);
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
            int totalFlips = Integer.parseInt(view.numOfFlips.getText());
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
            int prediction = Integer.parseInt(view.numOfResults.getText());
            System.out.println("Your prediction is that " + prediction + " coins will land correctly!");
        }
    }

    private class submitBetActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int bet = Integer.parseInt(view.bettingAmount.getText());
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
            String username = view.passUsername.getText();
            String oldPassword = view.oldPassword.getText();
            String newPassword = view.newPassword.getText();
            System.out.println("Changed password " + oldPassword + " for user " + username + " to be " + newPassword + ".");
            view.closeChangePassword();
        }
    }

    private class cancelActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Cancel button was pressed!");
            view.closeChangePassword();
        }
    }

}
