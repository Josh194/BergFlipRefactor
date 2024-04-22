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
            case 0: errorMessage1 = new JLabel("New Username is invalid.");
                    errorMessage2 = new JLabel("Username must be at least 8 characters.");
                    break;
            case 1: errorMessage1 = new JLabel("New Password is invalid.");
                    errorMessage2 = new JLabel("Password must be at least 8 characters.");
                    break;
            case 2: errorMessage1 = new JLabel("Username provided does not exist.");
                    errorMessage2 = new JLabel("Make sure you typed in the correct Username.");
                    break;
            case 3: errorMessage1 = new JLabel("Incorrect Password.");
                    errorMessage2 = new JLabel("Make sure you typed in the Password correctly.");
                    break;
            case 4: errorMessage1 = new JLabel("Requested number of flips is invalid.");
                    errorMessage2 = new JLabel("The coin can only be flipped between 1 and 5.");
                    break;
            case 5: errorMessage1 = new JLabel("Predicted number of correctly guesses flips is invalid.");
                    errorMessage2 = new JLabel("The predicted number must be less than or equal to the number " +
                            "of times the coin will flip.");
                    break;
            case 6: errorMessage1 = new JLabel("Amount to bet is invalid.");
                    errorMessage2 = new JLabel("Your bet must be greater than 0, and less than or equal to the " +
                            "amount left in your wallet.");
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
