import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ErrorView {
    private static JFrame errorFrame;
    private static JLabel errorMessage1;
    private static JLabel errorMessage2;

    public static void makeErrorPopup(int error, ActionListener closeAL) {
        System.out.println("Creating Error Message Popup...");
        errorFrame = new JFrame("An Error Occurred");
        errorFrame.setLayout(new GridLayout(3,1));
        switch (error) {
            case 0: errorMessage1 = new JLabel("Username provided already exists.");
                    errorMessage2 = new JLabel("This Username is already taken. Try a different username.");
                    break;
            case 1: errorMessage1 = new JLabel("New Username is invalid.");
                    errorMessage2 = new JLabel("Username must be at least 8 characters.");
                    break;
            case 2: errorMessage1 = new JLabel("New Password is invalid.");
                    errorMessage2 = new JLabel("Password must be at least 8 characters.");
                    break;
            case 3: errorMessage1 = new JLabel("Username provided does not exist.");
                    errorMessage2 = new JLabel("Make sure you typed in the correct Username.");
                    break;
            case 4: errorMessage1 = new JLabel("Incorrect Password.");
                    errorMessage2 = new JLabel("Make sure you typed in the Password correctly.");
                    break;
            case 5: errorMessage1 = new JLabel("Amount to bet is invalid.");
                    errorMessage2 = new JLabel("Your bet must be greater than 0.");
                    break;
            case 6: errorMessage1 = new JLabel("Amount to bet is invalid.");
                    errorMessage2 = new JLabel("Your bet less than your current balance: " + GameView.getWallet() + ".");
                    break;
            case 7: errorMessage1 = new JLabel("Amount to bet is invalid.");
                    errorMessage2 = new JLabel("Your bet must be a number. Please avoid using letters.");
                    break;
            case 8: errorMessage1 = new JLabel("No prediction was submitted.");
                    errorMessage2 = new JLabel("Please submit a prediction before playing the game.");
                    break;
            case 9: errorMessage1 = new JLabel("No bet was submitted.");
                    errorMessage2 = new JLabel("Please submit a bet before playing the game.");
                    break;
        }

        errorMessage1.setHorizontalAlignment(JLabel.CENTER);
        errorMessage2.setHorizontalAlignment(JLabel.CENTER);

        JButton closeErrorButton = new JButton("Close");
        closeErrorButton.addActionListener(closeAL);

        errorFrame.add(errorMessage1);
        errorFrame.add(errorMessage2);
        errorFrame.add(closeErrorButton);

        errorFrame.setSize(400,150);
        errorFrame.setVisible(true);
    }

    public static void closeErrorPopup() {
        System.out.println("Closing Error Message Popup...");
        errorFrame.setVisible(false);
    }

}
