import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    private final LoginView loginView;
    private final GameView gameView;
    private final PasswordView passwordView;
    private final Model model;
    private final closeErrorActionListener closeAL;

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

        closeAL = new closeErrorActionListener();
        loginView = new LoginView(loginAL,registerAL,passwordAL,exitAL);
        passwordView = new PasswordView();
        gameView = new GameView();
        model = new Model();
    }

    private class loginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.getEnterUsername().getText();
            String password = loginView.getEnterPassword().getText();
            System.out.println("Login button was pressed!");
            System.out.println("Username: " + username + ". Password: " + password + ".");

           if (model.checkLoginCredentials(username, password)) {
                loginView.closeLogin();
                gameView.openGame(flipAL,logoutAL,submitFlipsAL,submitPredicAL,headsAL,tailsAL,submitBetAL,refreshAL);
            } else {
                System.out.println("Invalid username or password");
            }
        }
    }

    private class registerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = loginView.getEnterUsername().getText();
            String password = loginView.getEnterPassword().getText();
            System.out.println("Register button was pressed!");

            if (checkUserValidity(username, password)) {
                if (model.doesUserExist(username)) {
                    System.out.println("Username already taken");
                    loginView.informUsernameAlreadyExists(closeAL);
                } else {
                    model.addUser(username, password);
                }
            }

            System.out.println("Username: " + username + ". Password: " + password + ".");
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
            System.out.println("Coin Flip button was pressed!");
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
            int totalFlips = Integer.parseInt(gameView.getNumOfFlips().getText());
            System.out.println("Coin will flip " + totalFlips + " times!");
            if(totalFlips > 5 | totalFlips < 0) {
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
            if(prediction > totalFlips | prediction < 0) {
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
            }
            else {
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

            if (model.checkLoginCredentials(username, oldPassword)) {
                System.out.println("Username or password is incorrect!");
            } else if (model.doesUserExist(username)) {
                System.out.println("Username does not exist!");
                passwordView.informUsernameDoesNotExist(closeAL);
            } else {
                model.updatePassword(username, newPassword);
                System.out.println("Changed password " + oldPassword + " for user " + username + " to be " + newPassword + ".");
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

    private boolean checkUserValidity(String username, String password) {
       if (username.length() < 8) {
            System.out.println("username is too short!");
            loginView.informInvalidUsername(closeAL);
            return false;
       } else if(password.length() < 8) {
            System.out.println("password is too short!");
            loginView.informInvalidPassword(closeAL);
            return false;
       }
       return true;
    }
}
