import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PasswordView {
    private JTextField passUsername;
    private JTextField oldPassword;
    private JTextField newPassword;

    private JFrame passwordFrame;

    public PasswordView() {

    }

    /*
     * PARAMETERS: ActionListeners for Confirm Change, Cancel buttons
     * RETURN TYPE: VOID
     * DESCRIPTION:
     * Builds the Change Password GUI
     */
    public void openChangePassword(ActionListener confirmPassAL, ActionListener cancelAL) {
        System.out.println("Opening Change Password GUI...");
        passwordFrame = new JFrame("Change Password");
        passwordFrame.setLayout(new GridLayout(4,2));

        JLabel username = new JLabel("USERNAME:");
        username.setHorizontalAlignment(JLabel.CENTER);
        passwordFrame.add(username);
        passUsername = new JTextField();
        passwordFrame.add(passUsername);

        JLabel oldpass = new JLabel("OLD PASSWORD:");
        oldpass.setHorizontalAlignment(JLabel.CENTER);
        passwordFrame.add(oldpass);
        oldPassword = new JTextField();
        passwordFrame.add(oldPassword);

        JLabel newpass = new JLabel("NEW PASSWORD:");
        newpass.setHorizontalAlignment(JLabel.CENTER);
        passwordFrame.add(newpass);
        newPassword = new JTextField();
        passwordFrame.add(newPassword);

        JButton confirmNewPassButton = new JButton("Confirm New Password");
        confirmNewPassButton.addActionListener(confirmPassAL);
        passwordFrame.add(confirmNewPassButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(cancelAL);
        passwordFrame.add(cancelButton);

        passwordFrame.setSize(500,300);
        passwordFrame.setVisible(true);
        System.out.println("Change Password GUI Initialized!");
    }

    /*
     * PARAMETERS: NONE
     * RETURN TYPE: VOID
     * DESCRIPTION:
     * Closes the Change Password GUI
     */
    public void closeChangePassword() {
        System.out.println("Closing Change Password GUI...");
        passwordFrame.setVisible(false);
    }

}
