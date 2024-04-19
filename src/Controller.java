import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    public View view;
    public Model model;

    public loginActionListener loginAL;
    public registerActionListener registerAL;
    public changePasswordActionListener passwordAL;
    public exitActionListener exitAL;
    public playActionListener playAL;
    public logoutActionListener logoutAL;
    public refreshActionListener refreshAL;

    Controller() {
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

    public class loginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Login button was pressed!");
            view.closeLogin();
            view.runGame(playAL,logoutAL,refreshAL);
        }
    }

    public class registerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Register button was pressed!");
        }
    }

    public class changePasswordActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Change Password button was pressed!");
        }
    }

    public class exitActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Exit button was pressed!");
            view.closeLogin();
            System.out.println("Closing Program...");
            System.exit(0);
        }
    }

    public class playActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Play button was pressed!");
        }
    }

    public class logoutActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Logout button was pressed!");
            view.closeGame();
            view.openLogin(loginAL,registerAL,passwordAL,exitAL);
        }
    }

    public class refreshActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Refresh button was pressed!");
        }
    }

}
