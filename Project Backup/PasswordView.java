import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PasswordView {
    private JTextField passUsername;
    private JTextField oldPassword;
    private JTextField newPassword;
    private JFrame passwordFrame;

    public JTextField getPassUsername() {
        return passUsername;
    }

    public JTextField getOldPassword() {
        return oldPassword;
    }

    public JTextField getNewPassword() {
        return newPassword;
    }

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

    public void closeChangePassword() {
        System.out.println("Closing Change Password GUI...");
        passwordFrame.setVisible(false);
    }

    public void informUsernameDoesNotExist(ActionListener closeAL) {
        passUsername.setBorder(BorderFactory.createLineBorder(Color.red));
        ErrorView.makeErrorPopup(2,closeAL);
    }

    public void informIncorrectPassword(ActionListener closeAL) {
        oldPassword.setBorder(BorderFactory.createLineBorder(Color.red));
        ErrorView.makeErrorPopup(3,closeAL);
    }

    public void informInvalidPassword(ActionListener closeAL) {
        newPassword.setBorder(BorderFactory.createLineBorder(Color.red));
        ErrorView.makeErrorPopup(1,closeAL);
    }

}
