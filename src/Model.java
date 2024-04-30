import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class Model {
    //Connection conn;
    private final static int initialBalance = 100;
    private final static int leaderboardRows = 3;
    private final static int leaderboardColumns = 2;

    public Model() {
        createInitialLeaderboard();
        createInitialUserTable();
    }

    private synchronized void createInitialUserTable() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:gameData.db")) {
            String cmd = "CREATE TABLE IF NOT EXISTS userData (" +
                    "userID INTEGER PRIMARY KEY," +
                    "username STRING," +
                    "password STRING," +
                    "balance INTEGER);";
            conn.createStatement().executeUpdate(cmd);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to create table for user data!");
        }
    }
    private synchronized void createInitialLeaderboard() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:gameData.db")) {
            String cmd = "CREATE TABLE IF NOT EXISTS leaderboard (" +
                    "userRank INTEGER PRIMARY KEY," +
                    "score INTEGER," +
                    "username STRING);";
            conn.createStatement().executeUpdate(cmd);

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:gameData.db")) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO userData (username, password, balance) VALUES (?, ?, " + initialBalance + ");");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void updatePassword (String username, String newPassword) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:gameData.db")) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE userData SET password = ? WHERE username = ?;");
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addLeaderboardScore (String username, int score) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:gameData.db")) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO leaderboard (username, score) VALUES (?, ?);");
            stmt.setString(1, username);
            stmt.setInt(2, score);
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized String[][] getLeaderboardScores() {
        String[][] retArr = new String[leaderboardRows][leaderboardColumns];
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:gameData.db")) {
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT username, balance from userData ORDER BY balance DESC LIMIT " + leaderboardRows);
            int row = 0;
            while (resultSet.next()) {
                for (int i = 0; i < leaderboardColumns; i++) {
                    retArr[row][i] = resultSet.getString(i + 1);
                }
                row++;
            }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retArr;
    }

    public synchronized double getBalance(String username) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:gameData.db")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT balance FROM userData WHERE username = ?;");
            stmt.setString(1, username);

            double balance = stmt.executeQuery().getDouble(1);

            stmt.close();
            conn.close();

            return balance;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private synchronized ArrayList<String> getUsers() {
        ArrayList<String> users = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:gameData.db")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT username FROM userData");
            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                users.add(resultSet.getString(1));
            }

            resultSet.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public boolean doesUserExist(String username) {
        ArrayList<String> Users = this.getUsers();
        for (String user : Users) {
            if (Objects.equals(user, username)) {
                return true;
            }
        }
        return false;
    }

    public synchronized boolean checkLoginCredentials (String username, String password) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:gameData.db")) {
            PreparedStatement stmt = conn.prepareStatement("SELECT username, password FROM userData WHERE username = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet resultSet = stmt.executeQuery();

            boolean condition = (resultSet.next() && (resultSet.getString(1).equals(username)) && (resultSet.getString(2).equals(password)));
            resultSet.close();
            stmt.close();
            conn.close();

            if (username.isEmpty() || password.isEmpty()) {
                return false;
            } else if (condition) {
               return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized void updateUserBalance (String username, String addedBalance) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:gameData.db")) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE userData SET balance = balance + ? WHERE username = ?");
            stmt.setString(1, addedBalance);
            stmt.setString(2, username);
            stmt.executeUpdate();

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
