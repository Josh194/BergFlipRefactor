import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class ClientGUI {
    private final LoginView loginView;
    private final GameView gameView;
    private final PasswordView passwordView;
    private final closeErrorActionListener closeErrorAL;
    private final closeSuccessActionListener closeSuccessAL;

    private Socket socket = null;
    private BufferedReader reader = null;
    private PrintWriter writer = null;
    private String serverMsg = null;
    private Boolean validBet = false;
    private String username;
    private double balance;
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
    private final headsActionListener headsAL;
    private final tailsActionListener tailsAL;
    private final submitBetActionListener submitBetAL;

    //Leaderboard GUI ActionListeners
    private final refreshActionListener refreshAL;

    public ClientGUI() {
        try {
            System.out.println("Connecting to 127.0.0.1...");
            this.socket = new Socket("127.0.0.1", Server.SERVER_PORT);
            writer = new PrintWriter(socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Success!");
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        headsAL = new headsActionListener();
        tailsAL = new tailsActionListener();
        submitBetAL = new submitBetActionListener();

        //Leaderboard GUI ActionListeners
        refreshAL = new refreshActionListener();

        closeErrorAL = new closeErrorActionListener();
        closeSuccessAL = new closeSuccessActionListener();
        loginView = new LoginView(loginAL,registerAL,passwordAL,exitAL);
        passwordView = new PasswordView();
        gameView = new GameView();
    }

    private class loginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            username = loginView.getEnterUsername().getText();
            String password = loginView.getEnterPassword().getText();
            System.out.println("Login button was pressed!");
            System.out.println("Username: " + username + ". Password: " + password + ".");

            writer.println("login");
            sendLoginInfoToServer(password);

            try {
                if (reader.readLine().equals("valid user")) {
                    loginView.closeLogin();
                    loadUserBalance(username);
                    gameView.openGame(flipAL,logoutAL,headsAL,tailsAL,submitBetAL,refreshAL);
                } else {
                    //TODO: Add more error checking/different error message.
                    System.out.println("Invalid username or password");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }


        }
    }

    private class registerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            username = loginView.getEnterUsername().getText();
            String password = loginView.getEnterPassword().getText();
            System.out.println("Register button was pressed!");

            writer.println("register");
            sendLoginInfoToServer(password);
            try {
                serverMsg = reader.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (serverMsg.equals("username already taken")) {
                //TODO: Need more extensive error checking.
                loginView.informUsernameAlreadyExists(closeErrorAL);
            } else if (serverMsg.equals("registered user")) {
                SuccessView.makeSuccessPopup(0,closeSuccessAL);
                System.out.println("Successfully registered user!");
            }
        }
    }

    private class changePasswordActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Change Password button was pressed!");
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
            System.out.println("Exit button was pressed!");
            loginView.closeLogin();
            System.out.println("Closing Program...");
            System.exit(0);
        }
    }

    private class flipActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String bet = gameView.getBettingAmount().getText();
                if (requestServerCoinFlip(gameView.getPredictedUserResult(), bet)) {
                    serverMsg = reader.readLine();
                    if (!serverMsg.equals("0.0")) {
                        updateUserBalance(Double.parseDouble(serverMsg));
                    } else {
                        updateUserBalance(-Double.parseDouble(bet));
                    }

                    System.out.println("Paid out $" + serverMsg);
                    gameView.updateFlipStatus();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private class logoutActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Logout button was pressed!");
            gameView.closeGame();
            loginView.openLogin(loginAL,registerAL,passwordAL,exitAL);
        }
    }

    private class refreshActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Refresh button was pressed!");
        }
    }

    private class headsActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Heads button was pressed!");
            gameView.updatePredictedUserResult(true);
        }
    }

    private class tailsActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Tails button was pressed!");
            gameView.updatePredictedUserResult(false);
        }
    }

    private class submitBetActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int bet = Integer.parseInt(gameView.getBettingAmount().getText());
            //TODO: Error checking if this is not an integer.
            if(bet > 0 & bet <= GameView.getWallet()) {
                System.out.println("Your bet is $" + bet + "!");
                gameView.updateBettingAmount();
                validBet = true;
            } else if(bet < 1) {
                System.out.println("That is an invalid bet! You have to bet at least $1.");
                gameView.informInvalidBetNonpositive(closeErrorAL);
                validBet = false;
            } else {
                System.out.println("That is an invalid bet! You have to less than your balance.");
                gameView.informInvalidBetTooLarge(closeErrorAL);
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

            writer.println("changePass");
            sendChangePasswordInfoToServer(username, oldPassword, newPassword);

            try {
                serverMsg = reader.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (serverMsg.equals("password error")) {
                System.out.println("Username or password is incorrect!");
                //TODO: add more error checking/different error message
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
            System.out.println("Cancel button was pressed!");
            passwordView.closeChangePassword();
            loginView.openLogin(loginAL,registerAL,passwordAL,exitAL);
        }
    }

    private boolean requestServerCoinFlip (String predictedResult, String Bet) {
        if (predictedResult == null || !validBet) {
            //TODO: Add error to inform user they need to select a betting amount or guess for heads/tails
            System.out.println("Invalid bet");
            return false;
        } else {
            writer.println("flip");
            writer.println(predictedResult);
            writer.println(Bet);
            writer.flush();
            return true;
        }
    }

    private void sendLoginInfoToServer (String password) {
        writer.println(username);
        writer.println(password);
        writer.flush();
    }

    private void sendChangePasswordInfoToServer (String username, String oldPassword, String newPassword) {
        writer.println(username);
        writer.println(oldPassword);
        writer.println(newPassword);
        writer.flush();
    }

    private void loadUserBalance (String username) {
        writer.println("balance");
        writer.println(username);
        writer.flush();

        try {
            balance = Double.parseDouble(reader.readLine());
            gameView.updateWallet(balance);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void updateUserBalance (double newBalance) {
        writer.println("update");
        writer.println(username);
        writer.println(newBalance);
        writer.flush();

        balance += newBalance;
        System.out.println("Balance from updateUserBalance : " + balance);
        gameView.updateWallet(balance);
    }
}