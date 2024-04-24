import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class Model {
    Connection conn;
    public Model() {
        try {
            this.conn = DriverManager.getConnection("jdbc:sqlite:gameData.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        createInitialLeaderboard();
        createInitialUserTable();
    }

    private void createInitialUserTable() {
        try {
            String cmd = "CREATE TABLE IF NOT EXISTS userData (" +
                    "userID INTEGER PRIMARY KEY," +
                    "username STRING," +
                    "password STRING);";
            conn.createStatement().executeUpdate(cmd);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to create table for user data!");
        }
    }
    private void createInitialLeaderboard() {
        try {
            String cmd = "CREATE TABLE IF NOT EXISTS leaderboard (" +
                    "userRank INTEGER PRIMARY KEY," +
                    "score INTEGER," +
                    "username STRING);";
            conn.createStatement().executeUpdate(cmd);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUser(String username, String password) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO userData (username, password) VALUES (?, ?);");
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePassword (String username, String newPassword) {
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE userData SET password = ? WHERE username = ?");
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void deleteUser(String username) {
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM userData WHERE username = ?");
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void addLeaderboardScore (String username, int score) {
        try {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO leaderboard (username, score) VALUES (?, ?);");
            stmt.setString(1, username);
            stmt.setInt(2, score);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void updateLeaderboardScore (String username, int score) {
        try {
            PreparedStatement stmt = conn.prepareStatement("UPDATE leaderboard SET score = ? WHERE username = ?;");
            stmt.setInt(1, score);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> getUsers() {
        ArrayList<String> users = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT username FROM userData");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(rs.getString(1));
            }
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

    public boolean checkLoginCredentials (String username, String password) {
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT username, password FROM userData WHERE username = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next() && (resultSet.getString(1).equals(username)) && (resultSet.getString(2).equals(password))) {
                return true;
            }

            System.out.println(resultSet.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getWalletBalance() {
        String tempBal = "100";
        return Integer.parseInt(tempBal);
    }
}
