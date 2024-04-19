import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    public View view;
    public Model model;

    private final loginActionListener loginAL;
    private final registerActionListener registerAL;
    private final changePasswordActionListener passwordAL;
    private final exitActionListener exitAL;
    private final playActionListener playAL;
    private final logoutActionListener logoutAL;
    private final refreshActionListener refreshAL;

    public Controller() {
        loginAL = new loginActionListener();
        registerAL = new registerActionListener();
        passwordAL = new changePasswordActionListener();
        exitAL = new exitActionListener();
        playAL = new playActionListener();
        logoutAL = new logoutActionListener();
        refreshAL = new refreshActionListener();

        view = new View(loginAL,registerAL,passwordAL,exitAL);
        model = new Model();
    }

    private class loginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Login button was pressed!");
            view.closeLogin();
            view.runGame(playAL,logoutAL,refreshAL);
        }
    }

    private class registerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Register button was pressed!");
        }
    }

    private class changePasswordActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Change Password button was pressed!");
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

    private class playActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Play button was pressed!");
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

}
