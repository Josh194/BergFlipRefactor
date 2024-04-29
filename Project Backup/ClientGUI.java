import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class ClientGUI {
    private final LoginView loginView;
    private final GameView gameView;
    private final PasswordView passwordView;
    private final closeErrorActionListener closeAL;

    private Socket socket = null;
    private BufferedReader reader = null;
    private PrintWriter writer = null;
    private String serverMsg = null;


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

    public ClientGUI() {
        try {
            System.out.println("Connecting to 127.0.0.1...");
            this.socket = new Socket("127.0.0.1", 6000);
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
        submitFlipsAL = new submitFlipsActionListener();
        submitPredicAL = new submitPredictionActionListener();
        headsAL = new headsActionListener();
        tailsAL = new tailsActionListener();
        submitBetAL = new submitBetActionListener();

        //Leaderboard GUI ActionListeners
        refreshAL = new refreshActionListener();

        closeAL = new closeErrorActionListener();
        loginView = new LoginView(loginAL,registerAL,passwordAL,exitAL);
        passwordView = new PasswordView();
        gameView = new GameView();
    }

    private class loginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.getEnterUsername().getText();
            String password = loginView.getEnterPassword().getText();
            System.out.println("Login button was pressed!");
            System.out.println("Username: " + username + ". Password: " + password + ".");

            writer.println("login");
            sendLoginInfoToServer(username, password);

            try {
                if (reader.readLine().equals("valid user")) {
                    loginView.closeLogin();
                    gameView.openGame(flipAL,logoutAL,submitFlipsAL,submitPredicAL,headsAL,tailsAL,submitBetAL,refreshAL);
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
            String username = loginView.getEnterUsername().getText();
            String password = loginView.getEnterPassword().getText();
            System.out.println("Register button was pressed!");

            writer.println("register");
            sendLoginInfoToServer(username, password);
            try {
                serverMsg = reader.readLine();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (serverMsg.equals("username already taken")) {
                //TODO: Need more extensive error checking.
                loginView.informUsernameAlreadyExists(closeAL);
            } else if (serverMsg.equals("registered user")) {
                //TODO: Add conformation that user was registered in client GUI
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
            writer.println("flip");
            requestServerCoinFlip(gameView.getNumOfFlips().getText(), gameView.getPredictedUserResult(), gameView.getPrediction().getText(), gameView.getBettingAmount().getText());
            try {
                serverMsg = reader.readLine();
                if (serverMsg.equals("success")) {
                    System.out.println("coin flipped");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            gameView.updateFlipStatus();
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

    private class submitFlipsActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO: error if gameView.getNumOfFlips() is empty
            int totalFlips = Integer.parseInt(gameView.getNumOfFlips().getText());
            System.out.println("Coin will flip " + totalFlips + " times!");
            if (totalFlips > 5 || totalFlips < 1) {
                //TODO: update error message, or add logic so that error message is correct
                gameView.informInvalidFlips(closeAL);
            } else {
                gameView.updateFlips();
            }
        }
    }

    private class headsActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Heads button was pressed!");
            gameView.updateHeadsOrTails(true);
        }
    }

    private class tailsActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Tails button was pressed!");
            gameView.updateHeadsOrTails(false);
        }
    }

    private class submitPredictionActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int totalFlips = Integer.parseInt(gameView.getNumOfFlips().getText());
            int prediction = Integer.parseInt(gameView.getPrediction().getText());
            System.out.println("Your prediction is that " + prediction + " coins will land correctly!");

            if (prediction > totalFlips || prediction < 1) {
                gameView.informInvalidPrediction(closeAL);
            } else {
                gameView.updatePrediction();
            }
        }
    }

    private class submitBetActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int bet = Integer.parseInt(gameView.getBettingAmount().getText());
            if(bet > 0) {
                System.out.println("Your bet is $" + bet + "!");
                gameView.updateBet();
            } else {
                System.out.println("That is an invalid bet! You have to bet at least $1.");
                gameView.informInvalidBet(closeAL);
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

    private void requestServerCoinFlip (String timesToFlip, String predictedResult, String predictedTimesCorrect, String Bet) {
        writer.println(timesToFlip);
        writer.println(predictedResult);
        writer.println(predictedTimesCorrect);
        writer.println(Bet);
        writer.flush();
    }

    private void sendLoginInfoToServer (String username, String password) {
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
}
