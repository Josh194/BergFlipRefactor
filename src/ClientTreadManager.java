import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.Objects;
import java.util.Random;

public class ClientTreadManager extends Thread {
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final Socket socket;
    private final Model model;
    private final Random rand = new Random();

    public ClientTreadManager(BufferedReader reader, PrintWriter writer, Socket socket, Model model) {
        this.reader = reader;
        this.writer = writer;
        this.socket = socket;
        this.model = model;
    }

    public void run() {
        String msg;

        System.out.println("Waiting for client to send data...");

        try {
            while ((msg = reader.readLine()) != null) {
                System.out.println("Server Message Received : " + msg);
                switch (msg) {
                    case "exit":
                        this.socket.close();
                        break;
                    case "login": {
                        String line = reader.readLine();
                        String line2 = reader.readLine();
                        System.out.println("Lines : " + line + " " + line2 + " [Line End]");
                        loginUser(line, line2);
                    }
                        break;
                    case "register": {
                        String line = reader.readLine();
                        String line2 = reader.readLine();
                        System.out.println("Lines : " + line + " " + line2 + " [Line End]");
                        registerUser(line, line2);
                    }
                        break;
                    case "changePass": {
                        String line = reader.readLine();
                        String line2 = reader.readLine();
                        String line3 = reader.readLine();
                        System.out.println("Lines : " + line + " " + line2 + " " + line3 + " [Line End]");
                        changeUserPassword(line, line2, line3);
                    }
                        break;
                    case "flip": {
                        String line = reader.readLine();
                        String line2 = reader.readLine();
                        System.out.println("Lines : " + line + " " + line2 + " [Line End]");
                        flipCoin(line, line2);
                    }
                        break;
                    case "balance": {
                        String line = reader.readLine();
                        System.out.println("Lines : " + line + " [Line End]");
                        writer.println(model.getBalance(line));
                        writer.flush();
                    }
                        break;
                    case "update": {
                        String line = reader.readLine();
                        String line2 = reader.readLine();
                        System.out.println("Lines : " + line + " " +  line2 + " [Line End]");
                        model.updateUserBalance(line, line2);
                    }
                        break;
                    default:
                        // TODO: reimplement when other prints removed
                        // System.out.println("Received: " + msg);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected unexpectedly!");
            e.printStackTrace();
        }
    }

    private void flipCoin (String predictedResult, String bet) {
        double payout = 0.0;

        if (rand.nextBoolean()) {
            if ((Objects.equals(predictedResult, "HEADS"))) {
                payout = (Integer.parseInt(bet) * 0.5);
            }
        } else {
            if ((Objects.equals(predictedResult, "TAILS"))) {
                payout = (Integer.parseInt(bet) * 0.5);
            }
        }

        System.out.println("Payout : " + payout);
        writer.println(payout);
        writer.flush();
    }

    private void loginUser (String username, String password) {
        if (model.checkLoginCredentials(username, password)) {
            System.out.println("Logged in user for client " + socket.toString());
            sendMessageToClient("valid user");
        } else {
            System.out.println("Invalid username or password for client " + socket.toString());
            sendMessageToClient("invalid user");
        }
    }

    private void registerUser (String username, String password) {
     if (model.doesUserExist(username)) {
         System.out.println("Username already taken for client " + socket.toString());
         sendMessageToClient("username already taken");
     } else if (username.length() < 8 || password.length() < 8) {
         System.out.println("username or password is too short!");
         sendMessageToClient("credentialError");
     } else {
         System.out.println("Successfully registered user for client " + socket.toString());
         model.addUser(username, password);
         sendMessageToClient("registered user");
     }
    }

    private void changeUserPassword (String username, String oldPassword, String newPassword) {
        if ((model.checkLoginCredentials(username, oldPassword)) && newPassword.length() > 7) {
            model.updatePassword(username, newPassword);
            sendMessageToClient("changed password");
            System.out.println("Changed password for client " + socket.toString());
        } else {
            sendMessageToClient("password error");
        }
    }

    private void sendMessageToClient (String msg) {
        writer.println(msg);
        writer.flush();
    }
}
